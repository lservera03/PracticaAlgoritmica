package business;

public abstract class Backtracking {

    public abstract void run(boolean marking, boolean pbmsc);

    public abstract void prepareLevelTour(int[] x, int k);

    public abstract boolean isThereSuccessor(int[] x, int k);

    public abstract void nextBrother(int[] x, int k);

    public abstract boolean solution(int[] x, int k);

    public abstract boolean completable(int[] x, int k);

    public abstract boolean feasible(int[] x);

    public abstract void treatSolution(int[] x);

}
