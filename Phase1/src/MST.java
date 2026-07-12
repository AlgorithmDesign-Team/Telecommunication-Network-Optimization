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
    //ادغام 2 مجموعه در صورت متفاوت بودن
    static boolean union(int x, int y){
        int px = find(x);
        int py = find(y);
        //اگر هر دو راس در یک مجموعه باشن این یال باعث چرخه میشود
        if (px == py){
            return false;
        }
        //اتصال درخت کوچک تر به بزرگ تر
        if (parentRank[px] < parentRank[py]){
            int t = px;
            px = py;
            py = t;
        }
        parent[py] = px;
        //اگر ارتفاع 2 درخت برابر باشد ارتفاع ریشه جدید یک واحد افزایش می یابد
        if (parentRank[px] == parentRank[py]){
            parentRank[px]++;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        //ورودی تعداد راس ها و یال ها
        int n = s.nextInt();
        int e = s.nextInt();
        //اطلاعات یال ها به صورت (مبدا , مقصد , وزن) ذخیره می شود
        int[][] edges = new int[e][3];
        for(int i = 0 ; i < e ; i ++){
            edges[i][0] = s.nextInt();
            edges[i][1] = s.nextInt();
            edges[i][2] = s.nextInt();
        }
        //مرتب سازی یال ها بر اساس وزن برای الگوریتم کروسکال
        Arrays.sort(edges , (a , b) -> a[2] - b[2]);
        parent = new int[n + 1];
        parentRank = new int[n + 1];
        //مقدار دهی اولیه ساختار Union-Find
        for(int i = 1 ; i <= n ; i++){
            parent[i] = i;
        }
        long totalCost = 0;
        List<int[]> mstEdges =new ArrayList<>();
        //انتخاب یال های درخت پوشای کمینه
        for(int[] edge : edges){
            int u = edge[0] , v = edge[1] , w = edge[2];
            //فقط یال هایی که چرخه ایجاد نکنند انتخاب می شوند
            if(union(u , v)){
                totalCost += w;
                mstEdges.add(new int[]{u , v});
                //درخت چوشای کمینه دقیقا n-1 یال دارد
                if(mstEdges.size() == n - 1){
                    break;
                }
            }
        }
        //برای خروجی به فرمت خواسته شده
        StringBuilder sb = new StringBuilder();
        sb.append(totalCost).append("\n");
        sb.append(mstEdges.size()).append("\n");
        for (int[] edge : mstEdges) {
            sb.append(edge[0]).append(" ").append(edge[1]).append("\n");
        }
        System.out.print(sb);
    }
}