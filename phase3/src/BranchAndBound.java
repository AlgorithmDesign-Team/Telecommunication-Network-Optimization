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
    }

    public int getBestCost() {
        return bestCost;
    }

    public ArrayList<BackupEdge> getBestAnswer() {
        return bestAnswer;
    }

    // آیا تمام یال‌های MST پوشش داده شده‌اند؟
    private boolean allCovered(boolean[] covered) {

        for(boolean b : covered){

            if(!b)
                return false;
        }

        return true;
    }

    // کمترین هزینه برای پوشاندن هر یال MST
    private void buildMinEdgeCost(){

        minEdgeCost = new int[mstEdgeCount];

        Arrays.fill(minEdgeCost, Integer.MAX_VALUE);


        for(BackupEdge backup : backups){

            for(int edgeId : backup.covers){

                minEdgeCost[edgeId] =
                        Math.min(minEdgeCost[edgeId], backup.cost);
            }
        }
    }
    // کران پایین برای هرس
    private int lowerBound(State state){

        int bound = state.cost;


        for(int i=0; i<mstEdgeCount; i++){

            if(!state.covered[i]){

                if(minEdgeCost[i] == Integer.MAX_VALUE)
                    return Integer.MAX_VALUE;


                bound += minEdgeCost[i];
            }
        }

        return bound;
    }

    private void branch(State state){

        // حالت پایان
        if(allCovered(state.covered)){


            if(state.cost < bestCost){

                bestCost = state.cost;

                bestAnswer =
                        new ArrayList<>(state.selected);
            }

            return;
        }

        // اگر از جواب فعلی بدتر شد
        if(state.cost >= bestCost)
            return;

        // اگر دیگر امکان پوشاندن وجود ندارد
        if(!canStillCover(state))
            return;

        if(lowerBound(state) >= bestCost)
            return;

        // اگر کابل دیگری نداریم
        if(state.index == backups.size())
            return;

        BackupEdge current =
                backups.get(state.index);

        /*
            انتخاب کابل فعلی
        */

        State take = new State(state);

        take.index++;

        take.cost += current.cost;

        take.selected.add(current);

        for(int edgeId : current.covers){

            take.covered[edgeId] = true;
        }

        branch(take);

        /*
            رد کردن کابل فعلی
        */

        State skip = new State(state);

        skip.index++;

        branch(skip);

    }

    public boolean solve(){

        State root =
                new State(mstEdgeCount);

        branch(root);

        return bestCost != Integer.MAX_VALUE;

    }

    // بررسی اینکه آیا با کابل‌های باقی‌مانده جواب ممکن است
    private boolean canStillCover(State state){

        boolean[] possible =
                state.covered.clone();

        for(int i=state.index; i<backups.size(); i++){

            BackupEdge backup =
                    backups.get(i);

            for(int id : backup.covers){

                possible[id] = true;

            }
        }

        for(boolean b : possible){

            if(!b)
                return false;
        }

        return true;
    }

}