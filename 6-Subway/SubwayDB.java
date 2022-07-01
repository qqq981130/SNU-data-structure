import java.io.*;
import java.util.*;

public class SubwayDB {
    private int size;
    private HashMap<String, Vertex> vertexMap = new HashMap<>(); //key: 지하철역 고유번호. value: Vertex
    private HashMap<String, ArrayList<String>> sameNameMap = new HashMap<>(); //같은 이름을 가진 역들 코드를 저장. 환승역 여부 판단하려고
    private String[] indexArray;  //index만으로 지하철역 고유번호에 바로 접근할 수 있게 만들어줄 배열
    private int[][] graph;

    public SubwayDB(File file) throws IOException {
        size = getStationNum(file); //지하철역 수 계산
        indexArray = new String[size];

        BufferedReader bf = new BufferedReader(new FileReader(file));
        parseVertex(bf, vertexMap, indexArray, sameNameMap, size); //Vertex 생성
        graph = new int[size][size];
        makeGraph(graph, bf, vertexMap, indexArray, sameNameMap, size); //Graph 생성
        bf.close();
    }

    public Vertex codeToVertex(String code) {
        return vertexMap.get(code);
    }

    public ArrayList<String> sameName(String name) {
        return sameNameMap.get(name);
    }

    public String get(int index) {
        return indexArray[index];
    }

    public int size() {
        return size;
    }

    public int[][] getGraph() {
        return graph;
    }

    static int getStationNum(File file) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(file));
        int cnt = 0;

        while (!bf.readLine().equals("")) { //빈 줄이 나오기 전까지 몇줄 있었나 카운팅
            cnt++;
        }
        bf.close();

        return cnt;
    }

    static void parseVertex(BufferedReader bf, HashMap<String, Vertex> codeToVertex, String[] indexArray, HashMap<String, ArrayList<String>> sameName, int n) throws IOException {
        for(int i =0; i<n; i++) {
            String newLine = bf.readLine();// [고유번호] [역이름] [호선] 입력
            String[] content = newLine.split(" ");
            Vertex newVertex = new Vertex(content[0], content[1], i);

            codeToVertex.put(content[0], newVertex); //역 고유번호만으로 해당 Vertex에 접근할수있게 해줌
            indexArray[i] = content[0]; //index에서 역 고유번호로 접근할 수 있게 해줌

            if (sameName.containsKey(content[1])) { //환승역이라면 그 이름을 공유하는 역들의 배열에 그 역의 고유번호를 넣어줌
                sameName.get(content[1]).add(content[0]);
            }
            else { //처음 들어오면
                ArrayList<String> newList = new ArrayList<>();
                newList.add(content[0]);
                sameName.put(content[1], newList);
            }
        }
    }

    static void makeGraph(int[][] graph, BufferedReader bf, HashMap<String, Vertex> codeToVertex, String[] indexArray, HashMap<String, ArrayList<String>> sameName, int n) throws IOException {
        bf.readLine(); //빈줄 없애기

        for (int i =0; i< n; i++) {
            for (int j=0; j< n; j++) {
                graph[i][j] = Integer.MAX_VALUE;
            }
        }

        //1.데이터 받아와서 엣지 만들기
        while(true) {
            String newLine = bf.readLine();
            if (newLine == null) {
                break;
            }
            else {
                String[] content = newLine.split(" ");
                int fromIndex = codeToVertex.get(content[0]).index; //출발역 index
                int toIndex = codeToVertex.get(content[1]).index; //도착역 index
                graph[fromIndex][toIndex] = Integer.parseInt(content[2]);
            }
        }

        //2.환승역들 엣지 설정
        for (String key: sameName.keySet()) {
            ArrayList<String> sameNameStations = sameName.get(key);

            if (sameNameStations.size()>1) { //이 경우 환승역
                for (int i =0; i<sameNameStations.size() -1; i++) {
                    for (int j = i+1; j<sameNameStations.size(); j++) {
                        Vertex transfer1 = codeToVertex.get(sameNameStations.get(i));
                        Vertex transfer2 = codeToVertex.get(sameNameStations.get(j));

                        transfer1.transferFlag = true; //환승가능한 역의 경우 flag로 표시
                        transfer2.transferFlag = true;

                        int fromIndex = transfer1.index;
                        int toIndex = transfer2.index;

                        graph[fromIndex][toIndex] = 5;
                        graph[toIndex][fromIndex] = 5;
                    }
                }
            }
        }
    }
}
