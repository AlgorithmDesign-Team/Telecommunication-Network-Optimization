import java.util.*;

public class IncreasedStability {
    static int[] parent;
    static int[] rank_;

    static int find(int x){
        if(parent[x] != x){
            parent[x] = find(parent[x]);
        } 
        return parent[x];
    }

    static boolean union(int x, int y){
        int rx = find(x);
        int ry = find(y);
        if(rx == ry){
            return false;
        }
        if(rank_[rx] < rank_[ry]){
            int t = rx; rx = ry; ry = t; 
        }
        parent[ry] = rx;
        if(rank_[rx] == rank_[ry]){
            rank_[rx]++;
        }
        return true;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int e = sc.nextInt();
        int[][] edges = new int[e][3]; 
        for(int i = 0; i < e; i++){
            edges[i][0] = sc.nextInt();
            edges[i][1] = sc.nextInt();
            edges[i][2] = sc.nextInt();
        }
        Arrays.sort(edges, (a , b) -> a[2] - b[2]);
        parent = new int[n + 1];
        rank_ = new int[n + 1];
        for(int i = 1; i <= n; i++){
            parent[i] = i;
        }
        List<int[]> mstEdges = new ArrayList<>(); 
        List<List<Integer>> mstAdj = new ArrayList<>();
        for(int i = 0; i <= n; i++){
            mstAdj.add(new ArrayList<>());
        }
        for(int[] edge : edges){
            int u = edge[0], v = edge[1];
            if (union(u, v)){
                mstEdges.add(new int[]{u, v});
                mstAdj.get(u).add(v);
                mstAdj.get(v).add(u);
                if(mstEdges.size() == n - 1){
                    break;
                }
            }
        }
        int m = sc.nextInt();
        int[][] backups = new int[m][3]; 
        for(int i = 0; i < m; i++){
            backups[i][0] = sc.nextInt();
            backups[i][1] = sc.nextInt();
            backups[i][2] = sc.nextInt();
        }
        boolean possible = true;
        long totalCost = 0;
        boolean[] usedBackup = new boolean[m];
        List<int[]> chosenBackups = new ArrayList<>();
        for(int[] mstEdge : mstEdges){
            int u = mstEdge[0];
            int v = mstEdge[1];
            Set<Integer> sideU = new HashSet<>();
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(u);
            sideU.add(u);
            while(!stack.isEmpty()){
                int cur = stack.pop();
                for(int next : mstAdj.get(cur)){
                    if(cur == u && next == v){
                        continue;
                    }
                    if(cur == v && next == u){
                        continue;
                    }
                    if(!sideU.contains(next)){
                        sideU.add(next);
                        stack.push(next);
                    }
                }
            }
            int bestIdx = -1;
            long bestCost = Long.MAX_VALUE;
            for(int i = 0; i < m; i++){
                if (usedBackup[i]) continue; 
                int a = backups[i][0];
                int b = backups[i][1];
                int c = backups[i][2];
                boolean aInU = sideU.contains(a);
                boolean bInU = sideU.contains(b);
                if(aInU != bInU){ 
                    if(c < bestCost){
                        bestCost = c;
                        bestIdx = i;
                    }
                }
            }
            if(bestIdx == -1){
                boolean anyCoverExists = false;
                for(int i = 0; i < m; i++){
                    int a = backups[i][0], b = backups[i][1];
                    boolean aInU = sideU.contains(a);
                    boolean bInU = sideU.contains(b);
                    if(aInU != bInU){ 
                        anyCoverExists = true; 
                        break;
                    }
                }
                if(!anyCoverExists){
                    possible = false;
                    break;
                }
                continue;
            }
            usedBackup[bestIdx] = true;
            totalCost += bestCost;
            chosenBackups.add(new int[]{backups[bestIdx][0], backups[bestIdx][1]});
        }
        StringBuilder sb = new StringBuilder();
        if(!possible){
            sb.append("NO\n");
        } 
        else{
            sb.append("YES\n");
            sb.append(totalCost).append("\n");
            sb.append(chosenBackups.size()).append("\n");
            for(int[] b : chosenBackups){
                sb.append(b[0]).append(" ").append(b[1]).append("\n");
            }
        }
        System.out.print(sb);
    }
}