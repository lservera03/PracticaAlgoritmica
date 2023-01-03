package business;

public abstract class BranchAndBound {


    public abstract void run();

    public abstract Configuration rootConfiguration();

    public abstract Configuration[] expand(Configuration configuration);

    public abstract boolean solution(Configuration configuration);

    public abstract boolean completable(Configuration configuration);

    public abstract boolean feasible(Configuration configuration);

    public abstract int value(Configuration configuration);

    public abstract int partialValue(Configuration configuration);

    public abstract int estimatedValue(Configuration configuration);


}


class Configuration {

    public int k;

    public Configuration(int k) {
        this.k = k;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
}