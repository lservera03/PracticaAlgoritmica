package business;

import persistence.SailorReader;
import persistence.ShipReader;

import java.util.ArrayList;
import java.util.Arrays;


public class Exercise1BacktrackingImp extends Backtracking {

    private ArrayList<Ship> ships;
    private int NUM_SHIPS;
    private ArrayList<Sailor> sailors;
    private int NUM_SAILORS;

    private int[] bestConfig;
    private double bestTotalSpeed;

    private boolean isUsingMarking;
    private boolean isUsingPbmsc;

    private double previousSpeed;

    @Override
    public void run(boolean marking, boolean pbmsc) {

        this.isUsingMarking = marking;
        this.isUsingPbmsc = pbmsc;

        ShipReader shipReader = new ShipReader();
        ships = shipReader.readAllShips();

        SailorReader sailorReader = new SailorReader();
        sailors = sailorReader.readAllSailors();

        NUM_SHIPS = ships.size();
        NUM_SAILORS = sailors.size();

        //init basic configuration
        int[] x = new int[NUM_SAILORS];
        int k = 0;

        bestTotalSpeed = 0;

        long start = System.nanoTime();

        if (this.isUsingMarking) {
            Exercise1BacktrackingMarking markingObject = new Exercise1BacktrackingMarking(NUM_SHIPS);

            backtracking(x, k, markingObject);
        } else {
            backtracking(x, k, null);
        }


        System.out.println("Best solution speed: ");
        System.out.println(bestTotalSpeed);

        System.out.println("Best config: ");
        System.out.println(Arrays.toString(bestConfig));

        // TODO print pretty

        for (int i = 0; i < NUM_SHIPS; i++) {

            System.out.println("Boat " + (i + 1) + ": " + ships.get(i).getName());

            for (int j = 0; j < NUM_SAILORS; j++) {

                if (bestConfig[j] == i) {
                    System.out.println("\t Sailor " + sailors.get(j).getNum_membership() + " " + sailors.get(j).getName());
                }
            }
        }

        long end = System.nanoTime();
        long elapsedTime = end - start;

        System.out.println("Time used: " + elapsedTime + " nanoseconds");

    }


    public void backtracking(int[] x, int k, Exercise1BacktrackingMarking m) {

        prepareLevelTour(x, k);

        while (isThereSuccessor(x, k)) {

            nextBrother(x, k);

            if (this.isUsingMarking) {
                mark(x, k, m);
            }

            System.out.println(Arrays.toString(x));

            if (solution(x, k)) {

                System.out.println(Arrays.toString(x));

                if (this.isUsingMarking) {

                    if (markedFeasible(x, m)) {
                        System.out.println("SOLUCION: ");
                        System.out.println(Arrays.toString(x));

                        markedTreatSolution(x, m);
                    } else {
                        //SOLUTION INCORRECT
                    }

                } else {
                    if (feasible(x)) {
                        System.out.println("SOLUCION: ");
                        System.out.println(Arrays.toString(x));

                        treatSolution(x);
                    } else {
                        //SOLUTION INCORRECT
                    }
                }


            } else {
                if (this.isUsingMarking) {
                    if (markedCompletable(x, k, m)) {

                        if (this.isUsingPbmsc) { //PBSMC
                            if (m.totalSpeed > bestTotalSpeed) {
                                backtracking(x, k + 1, m);
                            }

                        } else {
                            backtracking(x, k + 1, m);
                        }

                    } else {
                        //PODA
                    }
                } else {
                    if (completable(x, k)) {
                        backtracking(x, k + 1, m);
                    } else {
                        //PODA
                    }
                }
            }

            if (this.isUsingMarking) {
                unmark(x, k, m);
            }
        }

    }



    private void mark(int[] x, int k, Exercise1BacktrackingMarking m) {
        double shipSpeed = 1;

        if (x[k] != -1) { //If the sailor is assigned to a ship
            m.sailorsByShip[x[k]].sailors = m.sailorsByShip[x[k]].sailors + 1; //sailor counter by ship

            m.sailorsByShip[x[k]].isFull = ships.get(x[k]).getCapacity() <= m.sailorsByShip[x[k]].sailors;

            if (m.sailorsByShip[x[k]].isFull && ships.get(x[k]).getCapacity() == m.sailorsByShip[x[k]].sailors) {
                m.fullBoats++;
            }

            //save previous speed to unmark
            previousSpeed = m.sailorsByShip[x[k]].speed;

            if (m.sailorsByShip[x[k]].speed != 0) {
                m.totalSpeed = m.totalSpeed - previousSpeed;
            }

            //update total speed
            Ship ship = ships.get(x[k]);

            shipSpeed = sailors.get(k).getImpact(ship);

            for (int i = (k - 1); i >= 0; i--) {
                if (x[i] == x[k]) {
                    shipSpeed = shipSpeed * sailors.get(i).getImpact(ship);
                }
            }

            shipSpeed = ship.getSpeed() * shipSpeed;

            m.sailorsByShip[x[k]].speed = shipSpeed;

            m.totalSpeed = m.totalSpeed + shipSpeed;
        }

    }



    private void unmark(int[] x, int k, Exercise1BacktrackingMarking m) {
        boolean lleno = false;

        if (x[k] != -1) {
            m.sailorsByShip[x[k]].sailors = m.sailorsByShip[x[k]].sailors - 1;
            if(m.sailorsByShip[x[k]].isFull){
                lleno = true;
            }
            if(m.sailorsByShip[x[k]].sailors < ships.get(x[k]).getCapacity()){
                m.sailorsByShip[x[k]].isFull = false;
                if(lleno){
                    m.fullBoats--;
                }
            }
            m.totalSpeed = m.totalSpeed - m.sailorsByShip[x[k]].speed;
            m.sailorsByShip[x[k]].speed = previousSpeed;
            m.totalSpeed = m.totalSpeed + m.sailorsByShip[x[k]].speed;
        }

    }


    @Override
    public void prepareLevelTour(int[] x, int k) {
        x[k] = -2;
    }

    @Override
    public boolean isThereSuccessor(int[] x, int k) {
        return x[k] < (NUM_SHIPS - 1);
    }

    @Override
    public void nextBrother(int[] x, int k) {
        x[k]++;
    }

    @Override
    public boolean solution(int[] x, int k) {
        return k == (NUM_SAILORS - 1);
    }

    @Override
    public boolean completable(int[] x, int k) {
        int counter = 0;

        if (x[k] == -1) {
            return true;
        }

        for (int i = 0; i <= k; i++) {
            if (x[i] == x[k]) {
                counter++;
            }
        }

        return ships.get(x[k]).getCapacity() >= counter;
    }

    public boolean markedCompletable(int[] x, int k, Exercise1BacktrackingMarking m) {

        if (x[k] == -1) {
            return true;
        }

        return ships.get(x[k]).getCapacity() >= m.sailorsByShip[x[k]].sailors;
    }


    public boolean feasible(int[] x) {
        int[] sailorsByShip = new int[NUM_SHIPS];

        for (int i = 0; i < NUM_SAILORS; i++) {
            if (x[i] > -1) {
                sailorsByShip[x[i]]++;
            }
        }

        //loop to check if all the ships have full capacity
        for (int i = 0; i < NUM_SHIPS; i++) {
            if (ships.get(i).getCapacity() > sailorsByShip[i]) {
                return false;
            }
        }

        return true;
    }

    public boolean markedFeasible(int[] x, Exercise1BacktrackingMarking m) {
        return m.fullBoats == NUM_SHIPS;
    }

    @Override
    public void treatSolution(int[] x) {
        ArrayList<Sailor> sailorsByShip = new ArrayList<>();
        double shipSpeed = 1;

        double totalSpeed = 0;

        for (int i = 0; i < NUM_SHIPS; i++) {

            Ship ship = ships.get(i);

            for (int j = 0; j < NUM_SAILORS; j++) {

                if (x[j] == i) {
                    sailorsByShip.add(sailors.get(j));
                }
            }

            shipSpeed = sailorsByShip.get(0).getImpact(ship);

            for (int y = 1; y < sailorsByShip.size(); y++) { //Calculate ship speed
                shipSpeed = shipSpeed * sailorsByShip.get(y).getImpact(ship);
            }

            shipSpeed = ship.getSpeed() * shipSpeed;

            totalSpeed += shipSpeed; //Accumulate ships speeds by configuration

            sailorsByShip.clear();
        }

        System.out.println("Total speed: " + totalSpeed);

        if (totalSpeed > bestTotalSpeed) {
            bestTotalSpeed = totalSpeed;
            bestConfig = Arrays.copyOf(x, NUM_SAILORS);
        }

    }

    private void markedTreatSolution(int[] x, Exercise1BacktrackingMarking m) {
        System.out.println("CONFIG SPEED: ");
        System.out.println(m.totalSpeed);

        if (m.totalSpeed > bestTotalSpeed) {
            bestTotalSpeed = m.totalSpeed;
            bestConfig = Arrays.copyOf(x, NUM_SAILORS);
        }
    }

}


class ShipMarking {

    int sailors;
    double speed;

    boolean isFull;

    public ShipMarking() {
        sailors = 0;
        speed = 0.0;
        isFull = false;

    }
}


class Exercise1BacktrackingMarking {

    ShipMarking[] sailorsByShip;

    double totalSpeed;

    int fullBoats;


    Exercise1BacktrackingMarking(int numShips) {
        sailorsByShip = new ShipMarking[numShips];

        for (int i = 0; i < numShips; i++) {
            sailorsByShip[i] = new ShipMarking();
        }

        fullBoats = 0;

        totalSpeed = 0;
    }

}
