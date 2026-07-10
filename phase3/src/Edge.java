import java.util.ArrayList;

public class Edge {

    int from;
    int to;
    int cost;

    // این لیست بعداً برای کابل پشتیبان استفاده می‌شود
    ArrayList<Integer> covers;


    public Edge(int from, int to, int cost){

        this.from = from;
        this.to = to;
        this.cost = cost;

        covers = new ArrayList<>();

    }
}