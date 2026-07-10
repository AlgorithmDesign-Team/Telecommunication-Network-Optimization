import java.util.ArrayList;
import java.util.Arrays;

public class TreeMST {


    int n;

    ArrayList<Edge>[] adj;

    boolean[] visited;
    ArrayList<Integer> path;



    public TreeMST(int n){

        visited = new boolean[n + 1];
        path = new ArrayList<>();

        this.n = n;


        adj = new ArrayList[n+1];


        for(int i=1;i<=n;i++){

            adj[i] = new ArrayList<>();

        }

    }



    public void addEdge(Edge e){

        adj[e.from].add(e);

        adj[e.to].add(
                new Edge(e.to,e.from,e.cost)
        );

    }


}
