package business;

import persistence.SailorReader;
import persistence.ShipReader;

import java.util.*;

public class Exercise1BranchAndBoundImp extends BranchAndBound {


    private ArrayList<Ship> ships;

    private int NUM_SHIPS;

    private ArrayList<Sailor> sailors;

    private int NUM_SAILORS;

    private double bestTotalSpeed;

    @Override
    public void run() {
        ShipReader shipReader = new ShipReader();
        ships = shipReader.readAllShips();
        NUM_SHIPS = ships.size();

        SailorReader sailorReader = new SailorReader();
        sailors = sailorReader.readAllSailors();
        NUM_SAILORS = sailors.size();


        long start = System.nanoTime();

        Exercise1Configuration best = (Exercise1Configuration) BranchAndBound();

        for (int i = 0; i < NUM_SHIPS; i++) {

            System.out.println("Boat " + (i + 1) + ": " + ships.get(i).getName());

            for (int j = 0; j < NUM_SAILORS; j++) {

                if (best.getPosition(j) == i) {
                    System.out.println("\t Sailor " + sailors.get(j).getNum_membership() + " " + sailors.get(j).getName());
                }
            }
        }


        long end = System.nanoTime();
        long elapsedTime = end - start;

        System.out.println("Time used: " + elapsedTime + " nanoseconds");

        System.out.println("Best configuration: ");
        System.out.println(Arrays.toString(best.getSailors()));
        System.out.println("Maxima velocidad usada: " + value(best));
    }


    private Configuration BranchAndBound() {
        Configuration configuration, bestConfiguration = null;
        Configuration[] sons = new Configuration[NUM_SAILORS + 1];


        PriorityQueue<Ex1ConfigurationQueue> aliveNodes = new PriorityQueue<>();

        configuration = rootConfiguration();


        aliveNodes.add(new Ex1ConfigurationQueue((Exercise1Configuration) configuration, NUM_SAILORS));
        bestTotalSpeed = 0;

        while (!aliveNodes.isEmpty()) {

            configuration = aliveNodes.poll().getConfiguration();

            sons = expand(configuration);

            for (Configuration son : sons) {

                if (solution(son)) {

                    if (feasible(son)) {

                        if (value(son) > bestTotalSpeed) {
                            bestTotalSpeed = value(son);
                            bestConfiguration = new Exercise1Configuration((Exercise1Configuration) son, NUM_SAILORS);
                        }

                    } else {
                        //DISCARD
                    }

                } else {

                    if (completable(son)) {

                        if (partialValue(son) > bestTotalSpeed) {
                            aliveNodes.add(new Ex1ConfigurationQueue((Exercise1Configuration) son, estimatedValue(son)));
                        }

                    } else {
                        //DISCARD
                    }

                }


            }

        }

        return bestConfiguration;
    }


    @Override
    public Configuration rootConfiguration() {
        return new Exercise1Configuration(-1, NUM_SAILORS);
    }

    @Override
    public Configuration[] expand(Configuration configuration) {
        Configuration[] sons = new Exercise1Configuration[NUM_SHIPS + 1];

        for (int i = -1; i < NUM_SHIPS; i++) {

            //Copy previous configuration
            Exercise1Configuration previous = (Exercise1Configuration) configuration;

            sons[i + 1] = new Exercise1Configuration(previous.getK() + 1, NUM_SAILORS);
            Exercise1Configuration son = (Exercise1Configuration) sons[i + 1];


            for (int j = 0; j <= configuration.k; j++) {
                son.setPosition(j, previous.getPosition(j));
            }

            //Generate new son
            son.setPosition(previous.getK() + 1, i);
        }


        return sons;
    }

    @Override
    public boolean solution(Configuration configuration) {
        return configuration.getK() == (NUM_SAILORS - 1);
    }

    @Override
    public boolean completable(Configuration configuration) {
        int counter = 0;

        Exercise1Configuration conf = (Exercise1Configuration) configuration;

        if (conf.getPosition(conf.getK()) == -1) {
            return true;
        }

        for (int i = 0; i <= conf.getK(); i++) {

            if (conf.getPosition(i) == conf.getPosition(conf.getK())) {
                counter++;
            }
        }

        return ships.get(conf.getPosition(conf.getK())).getCapacity() >= counter;
    }

    @Override
    public boolean feasible(Configuration configuration) {
        int[] sailorsByShip = new int[NUM_SHIPS];

        Arrays.fill(sailorsByShip, 0);

        Exercise1Configuration conf = (Exercise1Configuration) configuration;

        for (int i = 0; i < NUM_SAILORS; i++) {
            if (conf.getPosition(i) > -1) {
                sailorsByShip[conf.getPosition(i)]++;
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

    @Override
    public double value(Configuration configuration) {
        ArrayList<Sailor> sailorsByShip = new ArrayList<>();
        double shipSpeed = 1;

        double totalSpeed = 0;

        Exercise1Configuration conf = (Exercise1Configuration) configuration;

        System.out.println(Arrays.toString(conf.getSailors()));


        for (int i = 0; i < NUM_SHIPS; i++) {

            Ship ship = ships.get(i);

            for (int j = 0; j <= conf.getK(); j++) {

                if (conf.getPosition(j) == i) {
                    sailorsByShip.add(sailors.get(j));
                }
            }

            if (!sailorsByShip.isEmpty()) {
                shipSpeed = sailorsByShip.get(0).getImpact(ship);

                for (int y = 1; y < sailorsByShip.size(); y++) { //Calculate ship speed
                    shipSpeed = shipSpeed * sailorsByShip.get(y).getImpact(ship);
                }

                shipSpeed = ship.getSpeed() * shipSpeed;

                totalSpeed += shipSpeed; //Accumulate ships speeds by configuration

                sailorsByShip.clear();
            }
        }

        System.out.println("Total speed: " + totalSpeed);


        return totalSpeed;
    }

    @Override
    public double partialValue(Configuration configuration) {
        return value(configuration);
    }

    @Override
    public double estimatedValue(Configuration configuration) {
        return (NUM_SAILORS - (configuration.getK() + 1)) / value(configuration);
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

    public void setPosition(int position, int value) {
        this.sailors[position] = value;
    }

    public int getPosition(int position) {
        return this.sailors[position];
    }


}


class Ex1ConfigurationQueue implements Comparable<Ex1ConfigurationQueue> {

    private Exercise1Configuration configuration;
    private double score;


    public Ex1ConfigurationQueue(Exercise1Configuration configuration, double score) {
        this.configuration = configuration;
        this.score = score;
    }


    public Exercise1Configuration getConfiguration() {
        return configuration;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int compareTo(Ex1ConfigurationQueue o) {
        return Double.compare(this.getScore(), o.getScore());
    }
}
