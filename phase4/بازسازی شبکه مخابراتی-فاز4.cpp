#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

struct Edge{
    int u;
    int v;

    int w;
    int m;
    bool unharmed = false;
};

// Disjoint Set Union (Union-Find)
// برای تشخیص ایجاد چرخه
class DSU{

private:

    vector<int> parent;
    vector<int> Rank;

public:

    DSU(){}

    DSU(int n){
        parent.resize(n+1);
        Rank.resize(n+1,0);

        for(int i=0;i<=n;i++)
            parent[i]=i;
    }

    // پیدا کردن نماینده مجموعه
    int find(int x){

        if(parent[x] == x)
            return x;

        parent[x] = find(parent[x]);

        return parent[x];
    };

    // ادغام دو مجموعه
    bool unite(int a,int b){

        a = find(a);
        b = find(b);

        if(a == b)
            return false;

        if(Rank[a] < Rank[b])
            swap(a,b);

        parent[b] = a;

        if(Rank[a] == Rank[b])
            Rank[a]++;

        return true;
    };

};

// وضعیت یک گره در Branch & Bound
struct State{

    int index=0;

    //برای کابل های انتخب شده تا الان:
    int buildCost=0;
    int maintainCost=0;
    int edgeCount=0;

    //برای تشخیص چرخه
    DSU dsu;

    // اندیس کابل‌های انتخاب شده
    vector<int> selectedEdges;

};

//============ Global Variables =============

int n,e,h,B;
vector<Edge> cables;

//بهترین هزینه تا الان
int bestCost = INT_MAX;
vector<int> bestAnswer;

//آیا حداقل یک جواب معتبر پیدا شده؟
bool foundAny = false;

//============  (Utility) توابع کمکی  ==============

bool createsCycle(State& state, const Edge& edge){

    return state.dsu.find(edge.u) == state.dsu.find(edge.v);
}

bool isSolution(const State& state){
    return (state.edgeCount == n-1 && state.maintainCost <= B);
}

//ایده: ادامه از وضعیت فعلی با کراسکال برای تخمین کمترین هزینه ساخت
int lowerBound(const State& state){

    DSU temp = state.dsu;

    int cost = state.buildCost;

    int need = (n - 1) - state.edgeCount;

    for(int i = state.index; i < e && need > 0; i++){

        if(temp.unite(cables[i].u, cables[i].v)){
            cost += cables[i].w;
            need--;
        }
    }

    if(need > 0)
        return INT_MAX;

    return cost;
}

//هرس
bool prune(const State& state){

    // بودجه نگهداری رد شده
    if(state.maintainCost > B)
    return true;

    // کابل کافی برای کامل کردن درخت نداریم
    if((e - state.index) < ((n - 1) - state.edgeCount))
    return true;

    //هزینه ساخت فعلی از بهترین جواب بیشتره
    if(state.buildCost >= bestCost)
    return true;

    // حتی بهترین حالت این شاخه هم از هزینه فعلی کمتر نیست
    if(lowerBound(state) >= bestCost)
    return true;

    return false;
}

//==================== Branch and Bound ====================

void branch(State current) {
    
    //هرس کردن
    if(prune(current))
    return;

    // اگر جواب کامل پیدا شد
    if (isSolution(current)) {
        foundAny = true;
        if (current.buildCost < bestCost) {
            bestCost = current.buildCost;
            bestAnswer = current.selectedEdges;           
        }
        return;
    }

    // اگه همه کابل ها بررسی شدند
    if (current.index >= e) return;

    // کابل فعلی
    Edge edge = cables[current.index];

    // اگر کابل سالم باشد
    if(edge.unharmed){

        // فقط اگر چرخه نسازد آن را انتخاب می‌کنیم.
        // چون رایگان است، شاخه عدم انتخاب کابل لازم نیست.
        if(!createsCycle(current,edge)){

            State include = current;

            include.dsu.unite(edge.u,edge.v);

            include.edgeCount++;

            include.selectedEdges.push_back(current.index);

            include.index++;

            branch(include);
        }
        else{

            // اگر چرخه ساخت، مجبوریم ردش کنیم.
            State skip = current;

            skip.index++;

            branch(skip);
        }

        return;
    }

    //  برای کابل های ناسالم Include شاخه
    if(current.maintainCost + edge.m <= B && !createsCycle(current, edge)){
        State include = current;

        include.dsu.unite(edge.u, edge.v);

        include.buildCost += edge.w;
        include.maintainCost += edge.m;
        include.edgeCount++;

        include.selectedEdges.push_back(current.index);

        include.index++;

        branch(include);
    }

    //  برای کابل های ناسالم Exclude شاخه
    State exclude = current;

    exclude.index++;

    branch(exclude);
}

void solve(){
    
    State root;

    //وضعیت اولیه، ریشه
    root.index = 0;
    root.buildCost = 0;
    root.maintainCost = 0;
    root.edgeCount = 0;
    root.dsu = DSU(n);

    branch(root);

    // خروجی نهایی
    if (!foundAny) {
        cout << "NO" << endl;
    } else {
        cout << bestCost << endl;

        // محاسبه‌ی مجموع هزینه‌ی نگهداری کابل‌های جدید انتخاب‌شده
        int totalMaintain = 0;
        for (int idx : bestAnswer) {
            if (!cables[idx].unharmed) // فقط کابل‌های جدید هزینه نگهداری دارند
                totalMaintain += cables[idx].m;
        }

        cout << totalMaintain << endl;
        cout << bestAnswer.size() << endl; //n-1 = تعداد یال های درخت
        for (int idx : bestAnswer) {
            cout << cables[idx].u << " " << cables[idx].v << endl;
        }
    }
}

void readInputs(){

    cin >> n >> e;
    cables.resize(e);
    
    for(int i=0; i<e; i++){
        cin >> cables[i].u
            >> cables[i].v
            >> cables[i].w
            >> cables[i].m;
        }

    cin >> h;
    int oldE = e;
    cables.resize(oldE+h);

    for(int i=oldE;i<oldE+h;i++){

        cin >> cables[i].u
            >> cables[i].v;

        cables[i].w = 0;
        cables[i].m = 0;
        cables[i].unharmed = true;
    }
    e = oldE + h;

    cin >> B;
}

int main(){

    //دریافت ورودی ها
    readInputs();

    //مرتب کردن بر اساس هزینه ساخت به صورت صعودی
    sort(cables.begin(), cables.end(), [](const Edge& a,const Edge& b){

    if(a.w != b.w) return a.w < b.w;

    return a.m < b.m;
    });

    solve();

    return 0;
}