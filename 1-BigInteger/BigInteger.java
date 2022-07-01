import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "wrong input.";
  
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("[0-9]+"); //parsing input
  
    public byte[] numberList = new byte[200];
    public Boolean minusFlag = false;
    public int length = 0;
    public byte[] outcome = new byte[200];

    public BigInteger(byte[] num1) //계산을 다 마친 byte 배열 --> outcome
    {
        this.numberList = num1;
    }
  
    public BigInteger(String s) //String --> array of byte
    {
        this.length = s.length();
        for (int i = 0; i<this.length; i++) {
            this.numberList[200-this.length + i] = (byte)((byte)s.charAt(i) - (byte)48);
        }
    }

    public BigInteger add(BigInteger big) //this + big
    {
        int resultLength = Math.max(this.length, big.length);

        byte carry = 0;
        byte[] result = new byte[200];
        int i;

        for (i = 199; i>199-resultLength; i--) {
            byte num = (byte) (this.numberList[i] + big.numberList[i] + carry);
            carry = 0;

            if (num>=10) {
                carry++;
                result[i] += num-10;
            }
            else {
                result[i] += num;
            }
        }

        if (carry == 1) { //다 돌고 carry 남아있으면
            resultLength++;
            result[i] += 1;
        }

        BigInteger finalResult = new BigInteger(result);
        finalResult.length = resultLength;

        return finalResult;
    }

    public int whichIsBigger(BigInteger num2)  //this > num2 : return 1, this < num2 : return 2, this == num2 : return 0
    {
        //절댓값 기준 비교이므로 길이만으로 판단 가능.
        if (this.length > num2.length) {
            return 1;
        }
        else if (this.length < num2.length) { 
            return 2;
        }
        else { //this.length == num2.length 인 경우
            for (int i = 199-this.length+1; i<200; i++) {
                if (this.numberList[i] == num2.numberList[i]) {
                    continue;
                }
                else if (this.numberList[i] > num2.numberList[i]) {
                    return 1;
                }
                else {
                    return 2;
                }
            }
            return 0;
        }
    }

    public BigInteger subtract(BigInteger big) //this - big
    {
        int whichBigger = this.whichIsBigger(big);

        if (whichBigger == 0) {
            BigInteger result = new BigInteger("0");
            result.length = 1;
            return result;
        }

        else if (whichBigger == 1) //this가 더 큼. this - big
        {
            for (int i = 199; i>199-this.length; i--) {
                int num = this.numberList[i] - big.numberList[i];
                if (num < 0) {
                    this.numberList[i-1] -= 1;
                    num+=10;
                }
                this.outcome[i] = (byte)num;
            }

            BigInteger finalResult = new BigInteger(this.outcome);
            finalResult.setResultLength();
            return finalResult;
        }

        else //big가 더 큼. big - this 해주고 결과에 minusFlag = true;
        {
            for (int i = 199; i>199-big.length; i--) {
                int num = big.numberList[i] - this.numberList[i];
                if (num < 0) {
                    big.numberList[i-1] -= 1;
                    num+=10;
                }
                this.outcome[i] = (byte)num;
            }

            BigInteger finalResult = new BigInteger(this.outcome);
            finalResult.minusFlag = true;
            finalResult.setResultLength();
            return finalResult;
        }
    }

    public void setResultLength()  //subtract해서 구한 result의 resultLength를 구함.
    {
        for (int i = 0; i<=199; i++) {
            if (this.numberList[i] == 0) {
                continue;
            }
            else {
                this.length = 200-i;
                return;
            }
        }
        this.length = 1;  //숫자가 0이라는 뜻
    }

    public BigInteger multiply(BigInteger big) //this * big
    {
        int finger = 0;
        byte[] result = new byte[200];
        int carry = 0;

        for (int j = 199; j>199-big.length; j--) {
            for (int i = 199; i>199-this.length; i--) {
                int num = (byte)(this.numberList[i] * big.numberList[j] + result[i-finger]);
                if (num>=10) {
                    carry = num / 10;
                    num = num % 10;
                    result[i-finger] = (byte)num;
                    result[i-finger-1] += carry;
                }
                else {
                    result[i-finger] = (byte)num;
                }
            }
            finger++;
        }

        BigInteger finalResult = new BigInteger(result);
        finalResult.setResultLength();
        return finalResult;
    }

    @Override
    public String toString()
    {
        String output = "";
        Boolean startFlag = false;

        if (this.minusFlag) {
            output += "-";
        }
        for (int i = 199-this.length+1 ; i<=199 ; i++) {
            output += (char)(this.numberList[i] + 48);
        }

        return output;
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        input = input.replaceAll(" ", ""); //공백제거

        Matcher matcher = EXPRESSION_PATTERN.matcher(input); //숫자 두 쌍과 연산자 분리

        String[] numbers = new String[2]; //입력은 항상 2개
        int index = 0; //operator가 있을 index

        for (int i =0; i<2; i++) {
            if (matcher.find()) {
                numbers[i] = matcher.group();
            }
            if (i==0) {
                index = matcher.end();
            }
        }

        char firstSign = input.charAt(0);
        char operator = input.charAt(index);
        char secondSign;


        if (firstSign != '+' && firstSign !='-') {
            firstSign = '+';
        } //first_sign 을 결정

        if (operator == '*') {
            if (input.charAt(index+1) == '-') {
                secondSign = '-';
            }
            else {
                secondSign = '+';
            }
        }

        else { //operator가 *가 아니라 + 혹은 -인 경우
            if (input.charAt(index+1) == '-') {
                secondSign = '-';
            }
            else {
                secondSign = '+';
            }
        }

        BigInteger num1 = new BigInteger(numbers[0]);
        BigInteger num2 = new BigInteger(numbers[1]);
        switch(operator) {
            case '+':
                if ((firstSign == '+') && (secondSign == '+')) {
                    return num1.add(num2);
                }
                else if ((firstSign == '+') && (secondSign == '-')) {
                    return num1.subtract(num2);
                }
                else if ((firstSign == '-') && (secondSign == '+')) {
                    return num2.subtract(num1); //이 경우 num2에서 num1을 빼야함.
                }
                else { //- - 인 경우
                    BigInteger result = num1.add(num2);
                    result.minusFlag = true;
                    return result;
                }
            case '-': 
                if ((firstSign == '+') && (secondSign == '+')) {
                    return num1.subtract(num2);
                }
                else if ((firstSign == '-') && (secondSign == '+')) {
                    BigInteger result = num1.add(num2);
                    result.minusFlag = true;
                    return result;
                }
                else if ((firstSign == '+') && (secondSign == '-')) {
                    return num1.add(num2);
                }
                else { //부호가 순서대로 - - 라서 실질적으로 - + 인 경우
                    return num2.subtract(num1);
                }
            default: //operator가 *인 경우
                if (((firstSign == '+') && (secondSign == '+'))||((firstSign =='-') && (secondSign =='-'))) {
                    return num1.multiply(num2);
                }
                else {
                    BigInteger result = num1.multiply(num2);
                    result.minusFlag = true;
                    return result;
                }
        }
    }
  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString()); //override 된 toString() 함수
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
