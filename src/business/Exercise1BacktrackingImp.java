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
    private int bestAverageSpeed;

    private boolean isUsingMarking;
    private boolean isUsingPbmsc;

    private int executionCounter = 0;


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


        backtracking(x, k);


        //TODO show solution
    }


    public void backtracking(int[] x, int k) {

        prepareLevelTour(x, k);

        while (isThereSuccessor(x, k)) {

            nextBrother(x, k);

            if (solution(x, k)) {

                executionCounter++;

                System.out.println("EJECUCION: " + executionCounter);

                System.out.println(Arrays.toString(x));

                if (feasible(x)) {
                    System.out.println("SOLUCION: ");
                    System.out.println(Arrays.toString(x));

                    //TODO treat solution
                } else {
                    //SOLUTION INCORRECT
                }

            } else {
                if (completable(x, k)) {
                    backtracking(x, k + 1);
                } else {
                    //PODA
                }
            }

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
        return true;
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

            if (ship.getCapacity() != counter) {
                return false;
            }

        }

        return true;
    }

    @Override
    public void treatSolution(int[] x) {

    }
}
