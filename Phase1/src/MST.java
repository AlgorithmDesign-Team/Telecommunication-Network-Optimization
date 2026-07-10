import java.util.*;

public class MST {
    static int[] parent;
    static int[] parentRank;

    static int find(int x){
        if (parent[x] != x){
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    static boolean union(int x, int y){
        int px = find(x);
        int py = find(y);
        if (px == py){
            return false;
        }
        if (parentRank[px] < parentRank[py]){
            int t = px;
            px = py;
            py = t;
        }
        parent[py] = px;
        if (parentRank[px] == parentRank[py]){
            parentRank[px]++;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        int e = s.nextInt();
        int[][] edges = new int[e][3];
        for(int i = 0 ; i < e ; i ++){
            edges[i][0] = s.nextInt();
            edges[i][1] = s.nextInt();
            edges[i][2] = s.nextInt();
        }
        Arrays.sort(edges , (a , b) -> a[2] - b[2]);
        parent = new int[n + 1];
        parentRank = new int[n + 1];
        for(int i = 1 ; i <= n ; i++){
            parent[i] = i;
        }
        long totalCost = 0;
        List<int[]> mstEdges =new ArrayList<>();
        for(int[] edge : edges){
            int u = edge[0] , v = edge[1] , w = edge[2];
            if(union(u , v)){
                totalCost += w;
                mstEdges.add(new int[]{u , v});
                if(mstEdges.size() == n - 1){
                    break;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(totalCost).append("\n");
        sb.append(mstEdges.size()).append("\n");
        for (int[] edge : mstEdges) {
            sb.append(edge[0]).append(" ").append(edge[1]).append("\n");
        }
        System.out.print(sb);
    }
}