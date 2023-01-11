package business;

import persistence.SailorReader;
import persistence.ShipReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        //TODO print pretty

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
                    if (feasible2(x)) {
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
        ArrayList<Sailor> sailorsOfShip = new ArrayList<>();
        double shipSpeed = 1;
        double totalSpeed = 0;
        boolean full = true;

        if (x[k] != -1) { //If the sailor is assigned to a ship
            m.sailorsByShip[x[k]] = m.sailorsByShip[x[k]] + 1; //sailor counter by ship

            for (int i = 0; i < NUM_SHIPS && full; i++) {
                if (m.sailorsByShip[i] != ships.get(i).getCapacity()) {
                    full = false;
                }
            }

            m.isAllShipsFull = full;

            //save previous speed to unmark
            previousSpeed = m.totalSpeed;

            //update total speed
            for (int i = 0; i < NUM_SHIPS; i++) {

                Ship ship = ships.get(i);

                for (int j = 0; j <= k; j++) {

                    if (x[j] == i) {
                        sailorsOfShip.add(sailors.get(j));
                    }
                }

                if(!sailorsOfShip.isEmpty()){
                    shipSpeed = sailorsOfShip.get(0).getImpact(ship);

                    for (int y = 1; y < sailorsOfShip.size(); y++) { //Calculate ship speed
                        shipSpeed = shipSpeed * sailorsOfShip.get(y).getImpact(ship);
                    }

                    shipSpeed = ship.getSpeed() * shipSpeed;

                    totalSpeed += shipSpeed;

                    sailorsOfShip.clear();
                }

            }

            m.totalSpeed = totalSpeed;
        }

    }

    private void mark2(int[] x, int k, Exercise1BacktrackingMarking m) {
        ArrayList<Sailor> sailorsOfShip = new ArrayList<>();
        double shipSpeed = 1;
        double totalSpeed = 0;
        boolean full = true;

        if (x[k] != -1) { //If the sailor is assigned to a ship
            m.sailorsByShip[x[k]] = m.sailorsByShip[x[k]] + 1; //sailor counter by ship

            for (int i = 0; i < NUM_SHIPS && full; i++) {
                if (m.sailorsByShip[i] != ships.get(i).getCapacity()) {
                    full = false;
                }
            }

            m.isAllShipsFull = full;

            //save previous speed to unmark
            previousSpeed = m.speeds[x[k]];

            //update total speed
            Ship ship = ships.get(x[k]);

            for (int j = 0; j <= k; j++) {

                if (x[j] == x[k]) {
                    sailorsOfShip.add(sailors.get(j));
                }
            }

            shipSpeed = sailorsOfShip.get(0).getImpact(ship);

            for (int y = 1; y < sailorsOfShip.size(); y++) { //Calculate ship speed
                shipSpeed = shipSpeed * sailorsOfShip.get(y).getImpact(ship);
            }

            shipSpeed = ship.getSpeed() * shipSpeed;

            m.speeds[x[k]] = shipSpeed;

            sailorsOfShip.clear();


            m.totalSpeed = m.totalSpeed + shipSpeed;
        }

    }


    private void unmark(int[] x, int k, Exercise1BacktrackingMarking m) {

        if (x[k] != -1) {
            m.sailorsByShip[x[k]] = m.sailorsByShip[x[k]] - 1;
            m.isAllShipsFull = false;
            m.totalSpeed = previousSpeed;
        }

    }


    private void unmark2(int[] x, int k, Exercise1BacktrackingMarking m) {

        if (x[k] != -1) {
            m.sailorsByShip[x[k]] = m.sailorsByShip[x[k]] - 1;
            m.isAllShipsFull = false;
            m.totalSpeed = m.totalSpeed - m.speeds[x[k]];
            m.speeds[x[k]] = previousSpeed;
            m.totalSpeed = m.totalSpeed + m.speeds[x[k]];
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

        return ships.get(x[k]).getCapacity() >= m.sailorsByShip[x[k]];
    }


    @Override
    public boolean feasible(int[] x) {
        Map<Ship, Integer> counterSailors = new HashMap<>();

        //Loop to create Hashmap
        for (Ship s : ships) {
            counterSailors.put(s, 0);
        }

        //loop to fill hashmap
        for (int i = 0; i < NUM_SAILORS; i++) {
            if (x[i] > -1) {
                counterSailors.put(ships.get(x[i]), counterSailors.get(ships.get(x[i])) + 1);
            }
        }

        //loop to check if all the ships have full capacity
        for (Map.Entry<Ship, Integer> entry : counterSailors.entrySet()) {
            Ship ship = entry.getKey();
            Integer counter = entry.getValue();

            if (ship.getCapacity() > counter) {
                return false;
            }

        }

        return true;
    }

    public boolean feasible2(int[] x) {
        int[] sailorsByShip = new int[NUM_SHIPS];

        Arrays.fill(sailorsByShip, 0);

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
        return m.isAllShipsFull;
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


class Exercise1BacktrackingMarking {

    int[] sailorsByShip;

    double totalSpeed;

    double[] speeds;

    boolean isAllShipsFull;


    Exercise1BacktrackingMarking(int numShips) {
        sailorsByShip = new int[numShips];
        speeds = new double[numShips];
        Arrays.fill(speeds, 0.0);
        totalSpeed = 0;
        isAllShipsFull = false;
    }

}
