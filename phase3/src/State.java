import java.util.ArrayList;

// ----- هر گره از درخت جست و جو -----
public class State {

    int index;                    // کابلی که الان قرار است درباره‌اش تصمیم بگیریم

    int cost;                     // مجموع هزینه کابل‌های انتخاب شده

    boolean[] covered;            // کدام یال‌های MST پوشش داده شده‌اند

    ArrayList<BackupEdge> selected; // کابل‌های انتخاب شده

    //ساخت وضعیت ریشه
    public State(int mstEdgesCount){

        index = 0;
        cost = 0;

        covered = new boolean[mstEdgesCount];

        selected = new ArrayList<>();
    }

    // a copy of exist state
    public State(State other){

        this.index = other.index;
        this.cost = other.cost;

        //deep copy
        this.covered = other.covered.clone();

        this.selected = new ArrayList<>(other.selected);

    }

}