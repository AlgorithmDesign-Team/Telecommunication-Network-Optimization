import java.util.ArrayList;
import java.util.Arrays;

public class BranchAndBound {

    private ArrayList<BackupEdge> backups;
    private int mstEdgeCount;

    private int bestCost;
    private ArrayList<BackupEdge> bestAnswer;

    private int[] minEdgeCost;

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

    // ساخت آرایه کمترین هزینه پوشش هر یال
    private void buildMinEdgeCost() {

        minEdgeCost = new int[mstEdgeCount];

        Arrays.fill(minEdgeCost, Integer.MAX_VALUE);

        for (BackupEdge backup : backups) {

            for (int edgeId : backup.covers) {

                minEdgeCost[edgeId] =
                        Math.min(minEdgeCost[edgeId], backup.cost);
            }
        }
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
    private boolean canStillCover(State state) {

        boolean[] possible = state.covered.clone();

        for (int i = state.index; i < backups.size(); i++) {

            for (int id : backups.get(i).covers) {

                possible[id] = true;

            }

        }

        return allCovered(possible);

    }

    // Greedy برای تولید جواب اولیه (Upper Bound)
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

    // Lower Bound
    private int lowerBound(State state) {

        int bound = state.cost;

        int minRemaining = Integer.MAX_VALUE;

        for (int i = state.index; i < backups.size(); i++) {

            BackupEdge backup = backups.get(i);

            for (int id : backup.covers) {

                if (!state.covered[id]) {

                    minRemaining =
                            Math.min(minRemaining, backup.cost);

                    break;

                }

            }

        }

        if (minRemaining == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;

        return bound + minRemaining;

    }

    // Branch
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

        if (!canStillCover(state))
            return;

        if (lowerBound(state) >= bestCost)
            return;

        if (state.index == backups.size())
            return;

        BackupEdge current = backups.get(state.index);

        // Take
        State take = new State(state);
        take.index++;
        take.cost += current.cost;
        take.selected.add(current);

        for (int id : current.covers) {
            take.covered[id] = true;
        }

        branch(take);

        // Skip
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