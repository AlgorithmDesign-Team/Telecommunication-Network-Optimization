import java.util.ArrayList;
import java.util.Arrays;

public class TreeMST {


    int n;

    //آرایه لیست‌های مجاورت برای تشکیل درخت
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

    //پیدا کردن مسیر بین دو راس در درخت
    public boolean dfs(int current, int target) {

        visited[current] = true;

        //اضافه کردن راس فعلی به مسیر
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

        //اگر مسیر از این راس به مقصد نرسید این راس رو از مسیر حذف میکنیم.
        path.remove(path.size() - 1);

        return false;

    }

    //پیدا کردن مسیر بین دو شهر در درخت
    public ArrayList<Integer> findPath(int start, int end){

        Arrays.fill(visited, false);
        path.clear();

        //اجرای DFS و برگرداندن کپی مسیر پیدا شده.
        if(dfs(start, end)){
            return new ArrayList<>(path);
        }

        //اگه مسیری وجود نداشت لیست خالی برگردون.
        return new ArrayList<>();
    }

    //محاسبه و تخصیص یال‌های پوشش داده شده توسط یک کابل پشتیبان در درخت به اون کابل
    public void computeCoverage(BackupEdge backup){

        //دریافت لیست راس های مسیر بین دو سر کابل در درخت
        ArrayList<Integer> path = findPath(backup.from, backup.to);

        for(int i = 0; i < path.size()-1; i++){

            int u = path.get(i);
            int v = path.get(i+1);

            //پیدا کردن id یال در مسیر
            for(Edge edge : adj[u]){

                if(edge.to == v){
                    // اضافه کردن id هر یال درخت در مسیر به لیست پوشش کابل پشتیبانی
                    backup.covers.add(edge.id);

                    break;

                }

            }

        }

    }
}
