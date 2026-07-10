import java.util.ArrayList;
import java.util.Arrays;

public class TreeMST {


    int n;

    ArrayList<Edge>[] adj;

    boolean[] visited;
    ArrayList<Integer> path;



    public TreeMST(int n){

        this.n = n;

        visited = new boolean[n + 1];
        path = new ArrayList<>();

        adj = new ArrayList[n + 1];

        for(int i = 1; i <= n; i++){
            adj[i] = new ArrayList<>();
        }
    }



    public void addEdge(Edge e){

        adj[e.from].add(e);

        adj[e.to].add(
                new Edge(e.id, e.to, e.from, e.cost)
        );

    }

    public Edge getEdge(int u, int v){

        for(Edge edge : adj[u]){

            if(edge.to == v){

                return edge;

            }

        }

        return null;

    }

    public boolean dfs(int current, int target) {

        visited[current] = true;

        path.add(current);

        if (current == target) {
            return true;
        }

        for (Edge edge : adj[current]) {

            if (!visited[edge.to]) {

                boolean found = dfs(edge.to, target);

                if (found) {
                    return true;
                }

            }

        }

        path.remove(path.size() - 1);

        return false;

    }

    public ArrayList<Integer> findPath(int start, int end){

        Arrays.fill(visited, false);
        path.clear();

        if(dfs(start, end)){
            return new ArrayList<>(path);
        }

        return new ArrayList<>();
    }

    public void computeCoverage(BackupEdge backup){

        ArrayList<Integer> path =
                findPath(backup.from, backup.to);

        for(int i = 0; i < path.size()-1; i++){

            int u = path.get(i);
            int v = path.get(i+1);

            for(Edge edge : adj[u]){

                if(edge.to == v){

                    backup.covers.add(edge.id);

                    break;

                }

            }

        }

    }
}
