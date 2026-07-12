import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // ---------- ورودی درخت MST ----------
        int e = sc.nextInt();          // تعداد یال‌های MST
        int n = e + 1;          // تعداد شهرها

        TreeMST tree = new TreeMST(n);

        ArrayList<Edge> mstEdges = new ArrayList<>();

        for (int i = 0; i < e; i++) {

            int u = sc.nextInt();
            int v = sc.nextInt();

            Edge edge = new Edge(i, u, v, 1);

            mstEdges.add(edge);

            //یال به راس هر دو طرف یال در درخت اضافه می‌شه
            tree.addEdge(edge);
        }

        // ---------- ورودی کابل‌های پشتیبان ----------
        int m = sc.nextInt();

        ArrayList<BackupEdge> backups = new ArrayList<>();

        for (int i = 0; i < m; i++) {

            int u = sc.nextInt();
            int v = sc.nextInt();
            int cost = sc.nextInt();

            BackupEdge backup = new BackupEdge(u, v, cost);

            backups.add(backup);
        }

        //پیدا کردن و تخصیص دادن مسیرهای پشتیبانی کننده درخت به هر کابل
        for (BackupEdge backup : backups) {
            tree.computeCoverage(backup);
        }

        // مرتب سازی کابل‌های پشتیبان بر اساس هزینه بر اساس هزینه ساخت بر تعداد کابل پشتیبان کننده به طور صعودی برای افزایش سرعت
        backups.sort((a, b) -> {

            double scoreA =
                    (double)a.cost / a.covers.size();

            double scoreB =
                    (double)b.cost / b.covers.size();

            return Double.compare(scoreA, scoreB);

        });

        // ---------- اجرای Branch and Bound ----------
        BranchAndBound solver =
                new BranchAndBound(backups, mstEdges.size());

        if (solver.solve()) {

            System.out.println("YES");

            System.out.println(solver.getBestCost());

            System.out.println(solver.getBestAnswer().size());

            for (BackupEdge edge : solver.getBestAnswer()) {

                System.out.println(edge.from + " " + edge.to);

            }

        } else {

            System.out.println("NO");

        }

    }

}