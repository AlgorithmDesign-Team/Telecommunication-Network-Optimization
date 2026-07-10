import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int e = sc.nextInt();

        ArrayList<Edge> mstEdges;
        ArrayList<Edge> edges = new ArrayList<>();

        for(int i=0;i<e;i++){

            int u = sc.nextInt();
            int v = sc.nextInt();
            int w = sc.nextInt();

            edges.add(new Edge(u,v,w));

        }
    }

}