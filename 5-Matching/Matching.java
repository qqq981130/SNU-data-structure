import java.io.*;
import java.util.*;

public class Matching
{
    static private HashTable<String, Pair> newHash;

    public static void main(String args[])
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true)
        {
            try
            {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;

                command(input);
            }
            catch (IOException e)
            {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void command(String input) throws IOException
    {
        String[] temp = input.split(" ", 2);
        String operation = temp[0];
        String content = temp[1];

        //input data---------------------------------------------------------------------------------------------------
        if (operation.equals("<")) { //input data -> parsing -> save in AVL Tree of the Hash table
            newHash = new HashTable<>(Matching::hashFunc); //initialize Hash table with new data input

            BufferedReader reader = new BufferedReader(new FileReader(content));
            String str;
            int line = 1; //# of line read

            while ((str = reader.readLine()) != null) {
                for (int i = 0; i<=str.length()-6; i++) {
                    String subStr = str.substring(i, i+6);
                    Pair newPair = new Pair(line, i+1);

                    newHash.insert(subStr, newPair);
                }
                line++;
            }
            reader.close();
        }
        //-------------------------------------------------------------------------------------------------------------


        //print out index----------------------------------------------------------------------------------------------
        else if (operation.equals("@")) {
            if (newHash == null) {
                System.out.println("EMPTY");
            }
            else {
                int newKey = Integer.parseInt(content);
                HashNode<String, Pair> targetHashNode = newHash.indexToHashNode(newKey);
                if (targetHashNode == null) {
                    System.out.println("EMPTY");
                }
                else {
                    AVLTree<String, Pair> targetNodeTree = targetHashNode.getTree();

                    StringBuilder sb = new StringBuilder();
                    targetNodeTree.preOrder(targetNodeTree.root, sb);
                    sb.deleteCharAt(sb.length() - 1);
                    System.out.println(sb);
                }
            }
        }
        //-------------------------------------------------------------------------------------------------------------


        //search-------------------------------------------------------------------------------------------------------
        else if (operation.equals("?")) {
            if (content.length() == 6) { //추가적인 작업 없이 위치 반환
                HashNode<String, Pair> targetHashNode = newHash.keyToHashNode(content);
                if (targetHashNode == null) {
                    System.out.println("(0, 0)");
                    return;
                }
                AVLTree<String, Pair> targetTree = targetHashNode.getTree();
                AVLNode<String, Pair> targetAVLNode = targetTree.search(content);
                if (targetAVLNode == AVLTree.NIL) {
                    System.out.println("(0, 0)");
                    return;
                }

                StringBuilder sb = new StringBuilder();
                for (Pair i : targetAVLNode.getList()) {
                    sb.append(i.toString()).append(" ");
                }
                sb.deleteCharAt(sb.length() - 1);
                System.out.println(sb);
            }
            else { //if content.length() > 6
                ArrayList<Pair> start = new ArrayList<>();
                ArrayList<Pair> curr = new ArrayList<>();

                for (int i = 0; i<=content.length()-6; i++) {
                    if (i==0) {
                        String targetStr = content.substring(i, i+6); //char 6개 단위로 끊어서 검색
                        HashNode<String, Pair> targetHashNode = newHash.keyToHashNode(targetStr);
                        if (targetHashNode == null) {
                            System.out.println("(0, 0)");
                            return;
                        }
                        AVLTree<String, Pair> targetTree = targetHashNode.getTree();
                        AVLNode<String, Pair> targetAVLNode = targetTree.search(targetStr);
                        if (targetAVLNode == AVLTree.NIL) {
                            System.out.println("(0, 0)");
                            return;
                        }
                        else {
                            start = targetAVLNode.getList();
                        }
                    }
                    else {
                        String targetStr = content.substring(i, i+6); //char 6개 단위로 끊어서 검색
                        HashNode<String, Pair> targetHashNode = newHash.keyToHashNode(targetStr);

                        if (targetHashNode == null) {
                            System.out.println("(0, 0)");
                            return;
                        }

                        AVLTree<String, Pair> targetTree = targetHashNode.getTree();
                        AVLNode<String, Pair> targetAVLNode = targetTree.search(targetStr);

                        if (targetAVLNode == AVLTree.NIL) {
                            System.out.println("(0, 0)");
                            return;
                        }
                        else {
                            curr = targetAVLNode.getList();

                            int j = start.size() - 1, k;  //finger variable j, k
                            for (;j>=0;j--) {
                                for (k = curr.size() - 1;k>=0;k--) {
                                    if ((start.get(j).line == curr.get(k).line) && (start.get(j).index + i == curr.get(k).index)) {
                                        //이 경우 start의 j-th는 남아있어도 됨
                                        break;
                                    }
                                }
                                if (k==-1) start.remove(j); //목표한 String 위치 후보에서 탈락
                            }
                        }
                    }
                }
                //위의 과정을 다 거치고 나면 ArrayList인 start에는 우리가 찾던 String의 시작위치만 남아있다.
                if (start.isEmpty()) {
                    System.out.println("(0, 0)");
                    return;
                }
                else {
                    StringBuilder sb = new StringBuilder();
                    for (Pair p: start) {
                        sb.append(p.toString()).append(" ");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    System.out.println(sb);
                }

            }
        }
        //-------------------------------------------------------------------------------------------------------------


        else throw new IOException();
    }

    public static Integer hashFunc(String substring) {//k == 6, so substring's length is 6
        int hashcode = 0;
        for (int i = 0; i < 6; i++) {
            hashcode += substring.charAt(i);
        }
        hashcode = hashcode % 100;
        return hashcode;
    }
}


class Pair {
    public int line;
    public int index;

    public Pair(int lineNum, int indexNum) {
        line = lineNum;
        index = indexNum;
    }

    public String toString() {
        return new String("(" + line +", "+index+")");
    }
}