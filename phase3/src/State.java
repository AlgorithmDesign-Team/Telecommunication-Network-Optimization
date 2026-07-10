import java.util.ArrayList;

public class State {

    int index;                    // کابلی که الان قرار است درباره‌اش تصمیم بگیریم

    int cost;                     // مجموع هزینه کابل‌های انتخاب شده

    boolean[] covered;            // کدام یال‌های MST پوشش داده شده‌اند

    ArrayList<BackupEdge> selected; // کابل‌های انتخاب شده

    public State(int mstEdgesCount){

        index = 0;
        cost = 0;

        covered = new boolean[mstEdgesCount];

        selected = new ArrayList<>();
    }

    public State(State other){

        this.index = other.index;
        this.cost = other.cost;

        this.covered = other.covered.clone();

        this.selected = new ArrayList<>(other.selected);

    }

}