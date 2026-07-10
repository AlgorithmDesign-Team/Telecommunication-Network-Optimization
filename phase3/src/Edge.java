import java.util.ArrayList;

public class Edge {

    int id;
    int from;
    int to;
    int cost;

    // این لیست بعداً برای کابل پشتیبان استفاده می‌شود
    ArrayList<Integer> covers;


    public Edge(int id, int from, int to, int cost){

        this.id = id;
        this.from = from;
        this.to = to;
        this.cost = cost;

        covers = new ArrayList<>();

    }
}