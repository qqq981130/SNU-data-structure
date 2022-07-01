public class Vertex implements Comparable<Vertex>{
    public String code; //역 번호
    public String name; //역 이름
    public int index; //몇번째로 입력된 역인지. Graph에서의 index
    public boolean transferFlag;

    public Vertex prev;
    public int dist;


    public Vertex(String inCode, String inName, int inIndex) {
        code = inCode;
        name = inName;
        index = inIndex;
        transferFlag = false;
    }

    public void reset() {
        dist = Integer.MAX_VALUE;
    }

    @Override
    public int compareTo(Vertex v) {
        return Integer.compare(dist, v.dist);
    }
}
