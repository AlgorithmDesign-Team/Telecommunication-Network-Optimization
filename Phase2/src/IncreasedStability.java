import java.util.*;

public class IncreasedStability {
   
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        //ورودی یال های درخت پوشای کمینه
        int numTreeEdges = sc.nextInt();
        List<int[]> mstEdges = new ArrayList<>();
        List<List<Integer>> mstAdj = new ArrayList<>();
        // برای پیدا کردن تعداد شهرها
        int n = 0;
        for(int i = 0; i < numTreeEdges; i++){
            int u = sc.nextInt();
            int v = sc.nextInt();
            mstEdges.add(new int[]{u, v});
            //پیدا کردن بزرگ ترین شماره راس
            n = Math.max(n, Math.max(u, v));
        }
        n++;
        //ساخت لیست مجاورت درخت پوشا
        for(int i = 0; i <= n; i++){
            mstAdj.add(new ArrayList<>());
        }
        for(int[] edge : mstEdges){
            int u = edge[0], v = edge[1];
            mstAdj.get(u).add(v);
            mstAdj.get(v).add(u);
        }
        //دریافت کابل های پشتیبان
        int m = sc.nextInt();
        int[][] backups = new int[m][3];
        for(int i = 0; i < m; i++){
            backups[i][0] = sc.nextInt();// شهر اول
            backups[i][1] = sc.nextInt();// شهر دوم
            backups[i][2] = sc.nextInt();// هزینه فعال‌سازی
        }
        boolean possible = true;
        long totalCost = 0;
        boolean[] usedBackup = new boolean[m];
        List<int[]> chosenBackups = new ArrayList<>();
        // هر یال درخت پوشا را جداگانه بررسی می کنیم
        for(int[] mstEdge : mstEdges){
            int u = mstEdge[0];
            int v = mstEdge[1];
            //راس های یک سمت یال بعد از حذف یال
            Set<Integer> sideU = new HashSet<>();
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(u);
            sideU.add(u);
            //پیدا کردن یکی از دو بخش گراف بعد از حذف یال
            while(!stack.isEmpty()){
                int cur = stack.pop();
                //یال مورد بررسی را موقتا حذف می کنیم
                for(int next : mstAdj.get(cur)){
                    // یال فعلی (u-v) را نادیده بگیر
                    if((cur == u && next == v) || (cur == v && next == u)){
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
            //پیدا کردن ارزان ترین کابل پشتیبان که دو بخش را به هم وصل می کند
            for(int i = 0; i < m; i++){
                if (usedBackup[i]){
                    continue;
                }
                int a = backups[i][0];
                int b = backups[i][1];
                int c = backups[i][2];
                boolean aInU = sideU.contains(a);
                boolean bInU = sideU.contains(b);
                // پشتیبان از روی یال قطع شده عبور می‌کند
                if(aInU != bInU){
                    if(c < bestCost){
                        bestCost = c;
                        bestIdx = i;
                    }
                }
            }
            if(bestIdx == -1){
                boolean anyCoverExists = false;
                //بررسی اینکه اصلا کابلی برای اتصال 2 بخش وجود دارد یا نه
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
                // پوشش داده شده -> ادامه بده
                continue;
            }
            //انتخاب کابل پشتیبان
            usedBackup[bestIdx] = true;
            totalCost += bestCost;
            chosenBackups.add(new int[]{backups[bestIdx][0], backups[bestIdx][1]});
        }
        //چاپ خروجی
        if(!possible){
            System.out.println("NO");
        } 
        else {
            System.out.println("YES");
            System.out.println(totalCost);
            System.out.println(chosenBackups.size());
            for(int[] b : chosenBackups){
                System.out.println(b[0] + " " + b[1]);
            }
        }
    }
}