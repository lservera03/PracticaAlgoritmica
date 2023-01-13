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

    private boolean isMarking;

    @Override
    public void run(boolean marking) {
        ShipReader shipReader = new ShipReader();
        ships = shipReader.readAllShips();
        NUM_SHIPS = ships.size();

        SailorReader sailorReader = new SailorReader();
        sailors = sailorReader.readAllSailors();
        NUM_SAILORS = sailors.size();

        this.isMarking = marking;

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

                    if (this.isMarking) {
                        if (markedFeasible(son)) {

                            if (markedValue(son) > bestTotalSpeed) {
                                bestTotalSpeed = value(son);
                                bestConfiguration = new Exercise1Configuration((Exercise1Configuration) son, NUM_SAILORS);
                            }

                        } else {
                            //DISCARD
                        }
                    } else {
                        if (feasible(son)) {

                            if (value(son) > bestTotalSpeed) {
                                bestTotalSpeed = value(son);
                                bestConfiguration = new Exercise1Configuration((Exercise1Configuration) son, NUM_SAILORS);
                            }

                        } else {
                            //DISCARD
                        }
                    }


                } else {
                    if (this.isMarking) {
                        if (markedCompletable(son)) {

                            if (markedPartialValue(son) > bestTotalSpeed) {
                                aliveNodes.add(new Ex1ConfigurationQueue((Exercise1Configuration) son, markedEstimatedValue(son)));
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

        }

        return bestConfiguration;
    }


    @Override
    public Configuration rootConfiguration() {
        return new Exercise1Configuration(-1, NUM_SAILORS, NUM_SHIPS);
    }

    @Override
    public Configuration[] expand(Configuration configuration) {
        Configuration[] sons = new Exercise1Configuration[NUM_SHIPS + 1];

        for (int i = -1; i < NUM_SHIPS; i++) {

            //Copy previous configuration
            Exercise1Configuration previous = (Exercise1Configuration) configuration;

            sons[i + 1] = new Exercise1Configuration(previous.getK() + 1, NUM_SAILORS, NUM_SHIPS);
            Exercise1Configuration son = (Exercise1Configuration) sons[i + 1];


            for (int j = 0; j <= configuration.k; j++) {
                son.setPosition(j, previous.getPosition(j));
            }

            //Generate new son
            son.setPosition(previous.getK() + 1, i);

            //Marking
            if (this.isMarking && i != -1) {
                //Copy previous marking
                son.marking.fullBoats = previous.marking.fullBoats;
                son.marking.totalSpeed = previous.marking.totalSpeed;

                for (int y = 0; y < NUM_SHIPS; y++) {
                    son.marking.sailorsByShip[y] = new Ex1BAndBShipMarking(previous.marking.sailorsByShip[y]);
                }


                double shipSpeed = 1;

                son.marking.sailorsByShip[i].sailors = son.marking.sailorsByShip[i].sailors + 1; //sailor counter by ship

                son.marking.sailorsByShip[i].isFull = ships.get(i).getCapacity() <= son.marking.sailorsByShip[i].sailors;

                if (son.marking.sailorsByShip[i].isFull && ships.get(i).getCapacity() == son.marking.sailorsByShip[i].sailors) {
                    son.marking.fullBoats++;
                }

                if (son.marking.sailorsByShip[i].speed != 0) {
                    son.marking.totalSpeed = son.marking.totalSpeed - son.marking.sailorsByShip[i].speed;
                }

                //update total speed
                Ship ship = ships.get(i);

                shipSpeed = sailors.get(son.getK()).getImpact(ship);

                for (int j = (son.getK() - 1); j >= 0; j--) {
                    if (son.getSailors()[j] == i) {
                        shipSpeed = shipSpeed * sailors.get(j).getImpact(ship);
                    }
                }

                shipSpeed = ship.getSpeed() * shipSpeed;

                son.marking.sailorsByShip[i].speed = shipSpeed;

                son.marking.totalSpeed = son.marking.totalSpeed + shipSpeed;
            }
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

    public boolean markedCompletable(Configuration configuration) {
        Exercise1Configuration conf = (Exercise1Configuration) configuration;

        if (conf.getPosition(conf.getK()) == -1) {
            return true;
        }

        return ships.get(conf.getPosition(conf.getK())).getCapacity() >= conf.marking.sailorsByShip[conf.getPosition(conf.getK())].sailors;
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

    public boolean markedFeasible(Configuration configuration) {
        Exercise1Configuration conf = (Exercise1Configuration) configuration;

        return conf.marking.fullBoats == NUM_SHIPS;
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

    public double markedValue(Configuration configuration) {
        Exercise1Configuration conf = (Exercise1Configuration) configuration;

        return conf.marking.totalSpeed;
    }

    @Override
    public double partialValue(Configuration configuration) {
        return value(configuration);
    }

    public double markedPartialValue(Configuration configuration) {
        return markedValue(configuration);
    }

    @Override
    public double estimatedValue(Configuration configuration) {
        return (NUM_SAILORS - (configuration.getK() + 1)) / value(configuration);
    }


    public double markedEstimatedValue(Configuration configuration) {
        return (NUM_SAILORS - (configuration.getK() + 1)) / markedValue(configuration);
    }

}


class Ex1BAndBShipMarking {

    int sailors;
    double speed;

    boolean isFull;

    public Ex1BAndBShipMarking() {
        sailors = 0;
        speed = 0.0;
        isFull = false;
    }

    public Ex1BAndBShipMarking(Ex1BAndBShipMarking copy) {
        this.sailors = copy.sailors;
        this.speed = copy.speed;
        this.isFull = copy.isFull;
    }

}


class Exercise1BAndBMarking {

    Ex1BAndBShipMarking[] sailorsByShip;

    double totalSpeed;

    int fullBoats;


    Exercise1BAndBMarking(int numShips) {
        sailorsByShip = new Ex1BAndBShipMarking[numShips];

        for (int i = 0; i < numShips; i++) {
            sailorsByShip[i] = new Ex1BAndBShipMarking();
        }

        fullBoats = 0;

        totalSpeed = 0;
    }


}


class Exercise1Configuration extends Configuration {

    private int[] sailors;

    Exercise1BAndBMarking marking;

    public Exercise1Configuration(int k, int num_sailors, int numShips) {
        super(k);
        this.sailors = new int[num_sailors];
        this.marking = new Exercise1BAndBMarking(numShips);
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
