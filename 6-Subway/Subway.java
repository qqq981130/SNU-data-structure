import java.io.*;
import java.util.*;

public class Subway {
    public static void main(String[] args) throws IOException {
        //File file = new File(args[0]); //외부에서 파일 받아올때
        File file = new File("./subway.txt"); //파일 직접 입력해줄때

        SubwayDB db = new SubwayDB(file);


        command(db);
    }

    public static void command(SubwayDB db) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            String input = br.readLine();

            if(input.equals("QUIT")) {
                break;
            }

            String[] content = input.split(" ");

            //1. 역 이름이 입력되면 그에 맞는 코드를 찾는다.
            String fromCode = db.sameName(content[0]).get(0);
            String toCode = db.sameName(content[1]).get(0);

            //2. 역 코드를 찾았으면 그걸로 그 역의 Vertex를 얻는다.
            Vertex fromVertex = db.codeToVertex(fromCode);
            Vertex toVertex = db.codeToVertex(toCode);

            //3. route담을 ArrayList<String>() 만들고 다익스트라 시행
            ArrayList<String> route = new ArrayList<>();
            int time = dijkstra(db, route, fromVertex, toVertex);
            printAll(route, time);
        }
    }

    static private int dijkstra(SubwayDB db, ArrayList<String> route, Vertex fromVertex, Vertex toVertex) {
        PriorityQueue<Vertex> heap = new PriorityQueue<>();
        int n = db.size();
        int[] distance = new int[n];
        boolean[] visited = new boolean[n];

        //이전 검색에서 Vertex들의 dist가 바꼈을수 있으니 초기화
        for (int i=0; i<n; i++) {
            db.codeToVertex(db.get(i)).reset();
        }

        //distance 초기화
        for(int i=0; i<n; i++){
            distance[i] = Integer.MAX_VALUE;
        }

        //시작 Vertex 초기화
        fromVertex.dist = 0;
        distance[fromVertex.index] = 0;
        heap.add(fromVertex);
        visited[fromVertex.index] = true;

        //시작 Vertex가 환승역이면, 처음부터 환승하면 안 됨. 얘들 다 distance 0으로 만들고 힙에 넣어야한다.
        if (fromVertex.transferFlag) {
            for (String str: db.sameName(fromVertex.name)) {
                Vertex sameNameStation = db.codeToVertex(str);
                if (sameNameStation != fromVertex) {
                    sameNameStation.dist = 0;
                    heap.add(sameNameStation);
                    sameNameStation.prev = fromVertex;
                    distance[sameNameStation.index] = 0;
                }
            }
        }

        //다익스트라
        int[][] graph = db.getGraph();
        while(!heap.isEmpty()) {
            Vertex now = heap.poll();

            //도착했으면 바로 break
            if (now.name.equals(toVertex.name)) {
                toVertex = now;
                break;
            }

            //도착 안했으면 다시 relaxation
            else {
                int nowIndex = now.index;
                distance[nowIndex] = now.dist;
                visited[nowIndex] = true;

                for(int i = 0; i<n; i++) {
                    if (graph[nowIndex][i] < Integer.MAX_VALUE) {
                        if ((!visited[i]) && (distance[nowIndex] + graph[nowIndex][i] < distance[i])) {
                            Vertex tempVertex = db.codeToVertex(db.get(i));
                            //relaxation
                            distance[i] = distance[nowIndex] + graph[nowIndex][i];
                            tempVertex.dist = distance[i];
                            heap.add(tempVertex);
                            tempVertex.prev = db.codeToVertex(db.get(nowIndex));
                        }
                    }
                }
            }
        }

        //백트래킹
        Vertex curr = toVertex;
        while(curr != fromVertex) {
            route.add(curr.name); //출발지로부터 목적지까지의 경로가 거꾸로 저장된 배열리스트. 즉, 목적지 -> 출발지 순
            curr = curr.prev;
        }
        route.add(fromVertex.name);

        return toVertex.dist;
    }

    static private void printAll(ArrayList<String> route, int time) {
        int endCnt = 0; //출발역이 환승역이라서 처음부터 환승하는걸로 계산했으면 그부분은 빼줘야함
        for (int i = route.size() -1; i>=0; i--) {
            if (route.get(i).equals(route.get(i-1))) {
                endCnt++;
            }
            else {
                break;
            }
        }

        StringBuilder sb = new StringBuilder();

        for (int i = route.size() -1 -endCnt; i>=0; i--) {
            if (i==0) {
                sb.append(route.get(i)).append(" ");
                break;
            }
            else {
                if (route.get(i).equals(route.get(i-1))) {
                    sb.append("[").append(route.get(i)).append("]").append(" ");
                    i--;
                }
                else {
                    sb.append(route.get(i)).append(" ");
                }
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n").append(time);
        System.out.println(sb.toString());
    }
}
