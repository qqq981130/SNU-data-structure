import java.io.*;
import java.util.Stack;
/*  java.util.Stack METHODS
	peek()
	pop()
	push()
	search()
 */
import java.util.regex.*;
import java.lang.Math;

class CalException extends Exception{

}

public class CalculatorTest
{
    public static void main(String args[])
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true)
        {
            try
            {
                String input = br.readLine();
                if (input.compareTo("q") == 0)
                    break;

                command(input);
            }
            catch (Exception e)
            {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void command(String input)
    {
        try {
            //입력 전처리-------------------------------------------------
            String[] parsedInput = parse(input);
            //------------------------------------------------------------

            //postfix 형태로 바꾸기---------------------------------------
            Postfix postfix =toPostfix(parsedInput);
            //------------------------------------------------------------

            //postfix 계산해서 결과 내기----------------------------------
            Long calResult = postfix.calculate();
            //------------------------------------------------------------

            //postfix 출력하기--------------------------------------------
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i<postfix.numItems; i++) {
                sb.append(postfix.getItem(i)).append(" ");
            }
            sb.deleteCharAt(sb.length()-1);
            System.out.println(sb);
            //------------------------------------------------------------

            //계산결과 출력하기-------------------------------------------
            System.out.println(calResult);
            //------------------------------------------------------------

        } catch (CalException e) {
            System.out.println("ERROR");
        }
    }

    private static Postfix toPostfix(String[] inputArr) throws CalException {
        Postfix postfix = new Postfix(inputArr.length);
        Stack<String> opS = new Stack<>();
        String prev = null;

        for (int i =0; i<inputArr.length; i++) {
            String curr =inputArr[i];

            if (i==0 & curr.compareTo("~")==0) {opS.push("~"); prev=curr; continue;}

            if (isOp(curr)) {
                if (prev == null || isOp(prev)) {
                    if (curr.compareTo("~") == 0) { //Stack에 추가
                        if (opS.isEmpty()) {
                            opS.push(curr);
                        }
                        else if (setPriority(curr) <= setPriority(opS.peek())) { 
                            opS.push(curr);
                        }
                        else { //curr 우선순위 < stack top의 우선순위 ==> stack 비우기
                            while(!opS.isEmpty()) {
                                String op = opS.pop();
                                postfix.append(op);
                            }
                            opS.push(curr);
                        }
                        prev = curr;
                    }
                    else {
                        throw new CalException();
                    }
                }

                else if (isNum(prev) || prev.compareTo(")")==0) { //Stack에 추가
                    if (opS.isEmpty()) {
                        opS.push(curr);
                    }
                    else if (curr.compareTo("^")==0) { //right-associative, 최고 우선순위 연산자 ==> 무조건 stack에 추가
                        opS.push(curr);
                    }
                    else if (setPriority(curr) < setPriority(opS.peek())) { //curr 우선순위 > stack top의 우선순위 ==> push.
                        opS.push(curr);
                    }
                    else { //curr 우선순위 <= stack top의 우선순위 ==> curr의 우선순위보다 우선순위 낮은 연산자 나올때까지 stack 비우기.
                        while(!opS.isEmpty()) {
                            if (setPriority(opS.peek())<=setPriority(curr)) {
                                postfix.append(opS.pop());
                            }
                            else {
                                break;
                            }
                        }
                        opS.push(curr);
                    }
                    prev = curr;
                }
            }

            else if (isNum(curr)) {
                if (prev==null) {
                    postfix.append(curr);
                    prev = curr;
                }
                else if (isOp(prev)) {
                    if ((curr.compareTo("0") == 0) && (prev.compareTo("/") == 0 || prev.compareTo("%") == 0)) {
                        throw new CalException();
                    }
                    else {
                        postfix.append(curr);
                        prev = curr;
                    }
                }
                else { //prev, curr 모두 num: ERROR!
                    throw new CalException();
                }
            }

            else if (curr.compareTo("(")==0) { //괄호의 시작
                int j = i;
                int openCnt = 0; //counting # of open brackets encountered until the first open bracket is closed

                for (; j<inputArr.length; j++) {
                    if (inputArr[j].compareTo("(")==0) {
                        openCnt++;
                    }
                    else if (inputArr[j].compareTo(")")==0) {
                        openCnt--;
                        if (openCnt == 0) { //이때 처음 열린 괄호가 닫힌 것
                            break;
                        }
                    }
                }

                //j: 괄호가 닫힌 index
                if (j == i+1) { //열리자마자 닫힌 괄호는 ERROR
                    throw new CalException();
                }
                if (isBiOp(inputArr[i+1]) || isOp(inputArr[j-1])) { //괄호 시작부터 BiOperator가 있거나 끝날때 Operator로 끝나면 ERROR
                    throw new CalException();
                }

                int k= i+1;
                MyArrayList temp = new MyArrayList(); //bracket 안쪽에 있는 문자열들을 모아줄 ArrayList

                for (; k<j; k++) {
                    temp.append(inputArr[k]);
                }

                String[] insideBracket = temp.toArray();

                Postfix insideBracketPostfix = toPostfix(insideBracket); //Recursion

                for(int l = 0; l<insideBracketPostfix.numItems; l++) {
                    postfix.append(insideBracketPostfix.getItem(l));
                }

                i = j;
                prev = ")";
            }
            else {
                throw new CalException();
            }
        }

        while(!opS.isEmpty()) {
            String op = opS.pop();
            postfix.append(op);
        }
        return postfix;
    }

    private static boolean isBiOp(String input) {
        Pattern pattern = Pattern.compile("[-%^+/*]{1}");
        return pattern.matcher(input).matches();
    }

    private static boolean isOp(String input) {
        Pattern pattern = Pattern.compile("[-%^+/*~]{1}");
        return pattern.matcher(input).matches();
    }

    private static boolean isNum(String input) {
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(input).matches();
    }

    public static int setPriority(String op) {
        if (op.compareTo("^") == 0) {
            return 1;
        }
        else if (op.compareTo("~") == 0) {
            return 2;
        }
        else if (op.compareTo("*") == 0| op.compareTo("/") == 0 | op.compareTo("%") == 0) {
            return 3;
        }
        else {
            return 4;
        }
    }

    private static String[] parse(String input) throws CalException {
        if (input == null) {
            throw new CalException();
        }

        input = input.trim();
        if (input.compareTo("")==0) {
            throw new CalException();
        }

        if (!checkEdge(input)) {
            throw new CalException();
        }

        if (!checkBracket(input)) {
            throw new CalException();
        }

        Pattern TARGET = Pattern.compile("[0-9]+|[-^+/%*()]{1}");
        Matcher m = TARGET.matcher(input);
        MyArrayList temp = new MyArrayList();

        while(m.find()) {
            temp.append(m.group());
        }

        String[] parsedArray = temp.toArray();



        return  checkUniMinus(parsedArray); //unary minus 있으면 - 에서 ~ 로 바꿔줌
    }

    private static boolean checkEdge(String input) {
        if (input.endsWith("-")) {
            return false;
        }

        String[] operators = {"*", "/", "%", "+", "^"};

        for (String operator : operators) {
            if (input.startsWith(operator)) {
                return false;
            }
            else if (input.endsWith(operator)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkBracket(String input) {
        Stack<Character> brackets = new Stack<>();

        for (int i = 0; i<input.length(); i++) {
            if (input.charAt(i) == '(') {
                brackets.push('(');
            }
            else if (input.charAt(i) == ')') {
                if (brackets.isEmpty()) {
                    return false;
                }
                else {
                    brackets.pop();
                }
            }
            else {
                continue;
            }
        }

        return brackets.isEmpty();
    }

    private static String[] checkUniMinus(String[] inputArr) {
        for (int i = 0; i<inputArr.length; i++) {
            if (inputArr[i].compareTo("-") == 0) {
                if (i==0) {
                    inputArr[i] = "~";
                }
                else if (inputArr[i-1].compareTo("(")==0 | inputArr[i-1].matches("[^0-9)]+")) {
                    inputArr[i] = "~";
                }
            }
        }
        return inputArr;
    }

}

class Postfix {
    String[] list;
    int numItems = 0;

    public Postfix(int inputLen) {
        list = new String[inputLen];
    }

    public void append(String input) {
        list[numItems++] = input;
    }

    public String getItem(int i) {
        return list[i];
    }

    public String[] toArray() {
        String[] out = new String[numItems];
        for (int i = 0; i<numItems; i++) {
            out[i] = list[i];
        }
        return out;
    }

    public Long calculate() throws CalException{
        Stack<Long> S = new Stack<>();
        for (int i = 0 ; i<numItems; i++) {
            if (isNum(list[i])) {
                S.push(Long.parseLong(list[i]));
            }
            else {
                if (list[i].compareTo("+")==0) {
                    Long operand1 = S.pop();
                    Long operand2 = S.pop();
                    S.push(operand1 + operand2);
                }
                else if (list[i].compareTo("-")==0){
                    Long operand1 = S.pop();
                    Long operand2 = S.pop();
                    S.push(operand2 - operand1);
                }
                else if (list[i].compareTo("*")==0) {
                    Long operand1 = S.pop();
                    Long operand2 = S.pop();
                    S.push(operand1 * operand2);
                }
                else if (list[i].compareTo("/")==0) {
                    Long operand1 = S.pop();
                    Long operand2 = S.pop();
                    if (operand1==0) {
                        throw new CalException();
                    }
                    S.push(operand2 / operand1);
                }
                else if (list[i].compareTo("%")==0) {
                    Long operand1 = S.pop();
                    Long operand2 = S.pop();
                    if (operand1==0) {
                        throw new CalException();
                    }
                    S.push(operand2 % operand1);
                }
                else if (list[i].compareTo("^")==0) {
                    Long operand1 = S.pop();
                    Long operand2 = S.pop();
                    if (operand2 == 0 && operand1 < 0) {
                        throw new CalException();
                    }
                    S.push((long)Math.pow(operand2, operand1));
                }
                else { //Op가 ~인 경우
                    Long operand = S.pop();
                    S.push((-1)*operand);
                }
            }
        }
        return S.pop();
    }

    private static boolean isNum(String input) {
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(input).matches();
    }
}

class MyArrayList {
    static final int DEFAULT_SIZE = 32;
    String[] list;
    public int numItems = 0;

    public MyArrayList() {
        list = new String[DEFAULT_SIZE];
    }

    public void append(String input) {
        if (isFull()) {
            String[] newArray = new String[2*list.length];

            for (int i = 0; i< list.length; i++) { 
                newArray[i] = list[i];
            }
            newArray[list.length] = input;

            list = newArray;
            numItems++;
        }
        else {
            list[numItems] = input;
            numItems++;
        }
    }

    private boolean isFull() {
        return numItems == list.length;
    }

    public String[] toArray() {
        String[] tmp = new String[numItems];

        for (int i =0; i<numItems;i++) {
            tmp[i] = list[i];
        }

        return tmp;
    }
}