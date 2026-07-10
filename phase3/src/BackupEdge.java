import java.util.ArrayList;

public class BackupEdge {

    int from;
    int to;
    int cost;

    ArrayList<Integer> covers;

    public BackupEdge(int from, int to, int cost){

        this.from = from;
        this.to = to;
        this.cost = cost;

        covers = new ArrayList<>();
    }
}