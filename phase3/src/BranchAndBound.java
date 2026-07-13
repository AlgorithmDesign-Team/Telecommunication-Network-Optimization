import java.util.ArrayList;
import java.util.Arrays;

public class BranchAndBound {

    private ArrayList<BackupEdge> backups;
    private int mstEdgeCount;

    private int bestCost;
    private ArrayList<BackupEdge> bestAnswer;

    private int[] minEdgeCost;

    private void buildMinEdgeCost() {
        minEdgeCost = new int[mstEdgeCount];
        Arrays.fill(minEdgeCost, Integer.MAX_VALUE);

        for (BackupEdge backup : backups) {
            for (int edgeId : backup.covers) {
                minEdgeCost[edgeId] = Math.min(minEdgeCost[edgeId], backup.cost);
            }
        }
    }

    public BranchAndBound(ArrayList<BackupEdge> backups, int mstEdgeCount) {

        this.backups = backups;
        this.mstEdgeCount = mstEdgeCount;

        bestCost = Integer.MAX_VALUE;
        bestAnswer = new ArrayList<>();
        buildMinEdgeCost();
        // ساخت یک جواب اولیه با الگوریتم حریصانه
        greedyUpperBound();
    }

    public int getBestCost() {
        return bestCost;
    }

    public ArrayList<BackupEdge> getBestAnswer() {
        return bestAnswer;
    }

    // آیا همه یال‌های درخت پوشش داده شده‌اند؟
    private boolean allCovered(boolean[] covered) {

        for (boolean b : covered) {

            if (!b)
                return false;

        }

        return true;

    }

    // آیا با کابل‌های باقی‌مانده هنوز جواب ممکن است؟
    private boolean isCoveragePossible(State state) {

        boolean[] possible = state.covered.clone();

        for (int i = state.index; i < backups.size(); i++) {

            for (int id : backups.get(i).covers) {
                possible[id] = true;
            }
        }
        return allCovered(possible);
    }

    // تولید یک جواب اولیه با الگوریتم حریصانه
    // تا بهترین هزینه اولیه برای هرس شاخه‌ها به دست آید.
    private void greedyUpperBound() {

        boolean[] covered = new boolean[mstEdgeCount];

        ArrayList<BackupEdge> answer =
                new ArrayList<>();

        int totalCost = 0;

        while (!allCovered(covered)) {

            BackupEdge best = null;

            double bestRatio = Double.MAX_VALUE;

            for (BackupEdge backup : backups) {

                int newCover = 0;

                for (int id : backup.covers) {

                    if (!covered[id])
                        newCover++;

                }

                if (newCover == 0)
                    continue;

                double ratio = (double) backup.cost / newCover;

                if (ratio < bestRatio) {

                    bestRatio = ratio;

                    best = backup;

                }

            }

            if (best == null)
                return;

            answer.add(best);

            totalCost += best.cost;

            for (int id : best.covers) {

                covered[id] = true;

            }

        }

        bestCost = totalCost;

        bestAnswer = answer;

    }

    // کران پایین: هزینه فعلی + ارزان‌ترین کابلی که حداقل
    // یکی از یال‌های پوشش‌نشده را پوشش می‌دهد.
    private int lowerBound(State state) {
        int maxNeeded = 0;

        for (int i = 0; i < mstEdgeCount; i++) {
            if (!state.covered[i]) {
                if (minEdgeCost[i] == Integer.MAX_VALUE)
                    return Integer.MAX_VALUE;
                maxNeeded = Math.max(maxNeeded, minEdgeCost[i]);
            }
        }

        return state.cost + maxNeeded;
    }

    // اجرای بازگشتی الگوریتم Branch and Bound
    private void branch(State state) {

        if (allCovered(state.covered)) {

            if (state.cost < bestCost) {

                bestCost = state.cost;

                bestAnswer =
                        new ArrayList<>(state.selected);

            }
            return;
        }

        if (state.cost >= bestCost)
            return;

        if (!isCoveragePossible(state))
            return;

        if (state.index == backups.size())
            return;

        if (lowerBound(state) >= bestCost)
            return;

        BackupEdge current = backups.get(state.index);

        // شاخه انتخاب کابل فعلی
        State take = new State(state);
        take.index++;
        take.cost += current.cost;
        take.selected.add(current);

        for (int id : current.covers) {
            take.covered[id] = true;
        }

        branch(take);

        // شاخه عدم انتخاب کابل فعلی
        State skip = new State(state);
        skip.index++;
        branch(skip);
    }

    // شروع الگوریتم
    public boolean solve() {

        State root = new State(mstEdgeCount);

        branch(root);

        return bestCost != Integer.MAX_VALUE;
    }
}