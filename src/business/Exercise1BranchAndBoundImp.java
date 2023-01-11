package business;

import persistence.SailorReader;
import persistence.ShipReader;

import java.util.*;

public class Exercise1BranchAndBoundImp extends BranchAndBound {


    private ArrayList<Ship> ships;

    private int NUM_SHIPS;

    private ArrayList<Sailor> sailors;

    private int NUM_SAILORS;

    @Override
    public void run() {
        ShipReader shipReader = new ShipReader();
        ships = shipReader.readAllShips();
        NUM_SHIPS = ships.size();

        SailorReader sailorReader = new SailorReader();
        sailors = sailorReader.readAllSailors();
        NUM_SAILORS = sailors.size();


        long start = System.nanoTime();

        //Exercise1Configuration best = (Exercise1Configuration) BranchAndBound();

        long end = System.nanoTime();
        long elapsedTime = end - start;

        System.out.println("Time used: " + elapsedTime + " nanoseconds");

        System.out.println("Best configuration: ");
        //System.out.println(Arrays.toString(best.getSailors()));
        //System.out.println("Centers used: " + value(best));
    }

    @Override
    public Configuration rootConfiguration() {
        return new Exercise2Configuration(-1, NUM_SAILORS);
    }

    @Override
    public Configuration[] expand(Configuration configuration) {
        return null;
    }

    @Override
    public boolean solution(Configuration configuration) {
        return false;
    }

    @Override
    public boolean completable(Configuration configuration) {
        return false;
    }

    @Override
    public boolean feasible(Configuration configuration) {
        return false;
    }

    @Override
    public int value(Configuration configuration) {
        return 0;
    }

    @Override
    public int partialValue(Configuration configuration) {
        return 0;
    }

    @Override
    public int estimatedValue(Configuration configuration) {
        return 0;
    }
}


class Exercise1Configuration extends Configuration {

    private int[] sailors;


    public Exercise1Configuration(int k, int num_sailors) {
        super(k);
        this.sailors = new int[num_sailors];
    }

    public Exercise1Configuration(Exercise1Configuration exercise1Configuration, int num_centers) {
        super(exercise1Configuration.getK());
        this.sailors = Arrays.copyOf(exercise1Configuration.sailors, num_centers);
    }


    public int[] getSailors() {
        return sailors;
    }
}