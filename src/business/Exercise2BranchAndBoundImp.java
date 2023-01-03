package business;

import persistence.ShipReader;

import java.util.*;

public class Exercise2BranchAndBoundImp extends BranchAndBound {


    public static final int NUM_TYPES = 6;
    private ArrayList<Center> centers = new ArrayList<>();
    private int NUM_CENTERS;

    private ArrayList<Ship> ships;

    private int NUM_SHIPS;


    @Override
    public void run() {

        //READ SHIPS
        ShipReader shipReader = new ShipReader();
        ships = shipReader.readAllShips();

        NUM_SHIPS = ships.size();

        //create center arraylist
        createCentersArray();

        NUM_CENTERS = centers.size();

        long start = System.nanoTime();

        Exercise2Configuration best = (Exercise2Configuration) BranchAndBound();

        long end = System.nanoTime();
        long elapsedTime = end - start;

        System.out.println("Time used: " + elapsedTime + " nanoseconds");

        System.out.println("Best configuration: ");
        System.out.println(Arrays.toString(best.getCenters()));
        System.out.println("Centers used: " + value(best));
    }


    public Configuration BranchAndBound() {
        Configuration configuration, bestConfiguration = null;
        int bestCentersUsed;
        Configuration[] sons = new Configuration[2];

        PriorityQueue<ConfigurationQueue> aliveNodes = new PriorityQueue<>();

        configuration = rootConfiguration();

        aliveNodes.add(new ConfigurationQueue((Exercise2Configuration) configuration, NUM_CENTERS));
        bestCentersUsed = Integer.MAX_VALUE;

        while (!aliveNodes.isEmpty()) {

            configuration = aliveNodes.poll().getConfiguration();

            sons = expand(configuration);

            for (Configuration son : sons) {

                if (solution(son)) {

                    if (feasibleMarking(son)) {

                        if (valueMarking(son) < bestCentersUsed) {
                            bestCentersUsed = value(son);
                            bestConfiguration = new Exercise2Configuration((Exercise2Configuration) son, NUM_CENTERS);
                        }

                    } else {
                        //DISCARD
                    }

                } else {

                    if (completable(son)) {

                        if (partialValue(son) < bestCentersUsed) {
                            aliveNodes.add(new ConfigurationQueue((Exercise2Configuration) son, estimatedValue(son)));
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
        return new Exercise2Configuration(-1, NUM_CENTERS);
    }

    @Override
    public Configuration[] expand(Configuration configuration) {
        Configuration[] sons = new Exercise2Configuration[2];

        for (int i = 0; i < 2; i++) {
            //Copy previous configuration
            Exercise2Configuration previous = (Exercise2Configuration) configuration;

            sons[i] = new Exercise2Configuration(previous.getK() + 1, NUM_CENTERS);
            Exercise2Configuration son = (Exercise2Configuration) sons[i];

            for (int j = 0; j <= configuration.getK(); j++) {
                son.setPosition(j, previous.getPosition(j));
            }

            //set new position
            son.setPosition(previous.getK() + 1, i);

            //Copiar valor marcaje
            son.marking.centersUsed = previous.marking.centersUsed;

            son.marking.typesUsed = new HashMap<>(previous.marking.typesUsed);

            //son.marking.typesUsedCounter = previous.marking.typesUsedCounter;


            //Marking
            son.marking.centersUsed += i;

            //update types used
            if (i == 1) {

                Center center = this.centers.get(previous.getK() + 1);

                for (int j = 0; j < center.getShips().size(); j++) {
                    son.marking.typesUsed.put(center.getShips().get(j).getType(), son.marking.typesUsed.get(center.getShips().get(j).getType()) + 1);
                }

            }

            //types used counter
            for (Map.Entry<ShipType, Integer> entry : son.marking.typesUsed.entrySet()) {
                ShipType key = entry.getKey();
                Integer value = entry.getValue();

                if (value > 0) {
                    son.marking.typesUsedCounter++;
                }

            }

        }

        return sons;
    }

    @Override
    public boolean solution(Configuration configuration) {
        return configuration.getK() == (NUM_CENTERS - 1);
    }

    @Override
    public boolean completable(Configuration configuration) {
        return true;
    }

    @Override
    public boolean feasible(Configuration configuration) {
        Map<ShipType, Integer> counterTypes = new HashMap<>();
        int counter = 0;

        counterTypes.put(ShipType.Windsurf, 0);
        counterTypes.put(ShipType.Optimist, 0);
        counterTypes.put(ShipType.Laser, 0);
        counterTypes.put(ShipType.PatiCatala, 0);
        counterTypes.put(ShipType.HobieDragoon, 0);
        counterTypes.put(ShipType.HobieCat, 0);


        Exercise2Configuration conf = (Exercise2Configuration) configuration;


        for (int i = 0; i < NUM_CENTERS; i++) {

            if (conf.getPosition(i) == 1) { //If the center is selected, check the types of its ships

                for (Ship s : centers.get(i).getShips()) {

                    if (counterTypes.get(s.getType()) == 0) {
                        counter++;
                    }
                    counterTypes.put(s.getType(), counterTypes.get(s.getType()) + 1);

                }
            }
        }

        return counter == NUM_TYPES;
    }

    public boolean feasibleMarking(Configuration configuration) {
        Exercise2Configuration conf = (Exercise2Configuration) configuration;

        return conf.marking.typesUsedCounter == NUM_TYPES;
    }


    @Override
    public int value(Configuration configuration) {
        int counter = 0;

        Exercise2Configuration conf = (Exercise2Configuration) configuration;

        for (int i = 0; i <= conf.getK(); i++) {

            if (conf.getPosition(i) == 1) {
                counter++;
            }
        }

        return counter;
    }


    public int valueMarking(Configuration configuration) {
        Exercise2Configuration conf = (Exercise2Configuration) configuration;

        return conf.marking.centersUsed;
    }


    @Override
    public int partialValue(Configuration configuration) {
        return valueMarking(configuration);
    }

    @Override
    public int estimatedValue(Configuration configuration) {
        return (valueMarking(configuration) / (configuration.getK() + 1)) * (NUM_CENTERS - configuration.getK() + 1); //Average centers used
    }


    private void createCentersArray() {
        ArrayList<String> names = new ArrayList<>();

        for (Ship s : ships) {

            if (!names.contains(s.getCenter())) {
                names.add(s.getCenter());

                Center center = new Center(s.getCenter());

                center.addShip(s);

                centers.add(center);
            } else {
                centers.get(names.indexOf(s.getCenter())).addShip(s);
            }
        }
    }


}


class Exercise2BAndBMarking {

    int centersUsed;

    Map<ShipType, Integer> typesUsed;

    int typesUsedCounter;

    public Exercise2BAndBMarking() {
        this.centersUsed = 0;
        this.typesUsedCounter = 0;
        this.typesUsed = new HashMap<>();
        this.fillTypesMap();
    }

    private void fillTypesMap() {
        this.typesUsed.put(ShipType.Windsurf, 0);
        this.typesUsed.put(ShipType.Optimist, 0);
        this.typesUsed.put(ShipType.Laser, 0);
        this.typesUsed.put(ShipType.PatiCatala, 0);
        this.typesUsed.put(ShipType.HobieDragoon, 0);
        this.typesUsed.put(ShipType.HobieCat, 0);
    }

}


class Exercise2Configuration extends Configuration {

    private int[] centers;

    Exercise2BAndBMarking marking;


    public Exercise2Configuration(int k, int num_centers) {
        super(k);
        this.centers = new int[num_centers];
        this.marking = new Exercise2BAndBMarking();
    }

    public Exercise2Configuration(Exercise2Configuration exercise2Configuration, int num_centers) {
        super(exercise2Configuration.getK());
        this.centers = Arrays.copyOf(exercise2Configuration.centers, num_centers);
    }


    public int[] getCenters() {
        return centers;
    }

    public void setPosition(int position, int value) {
        this.centers[position] = value;
    }

    public int getPosition(int position) {
        return this.centers[position];
    }

}


class ConfigurationQueue implements Comparable<ConfigurationQueue> {

    private Exercise2Configuration configuration;
    private int score;


    public ConfigurationQueue(Exercise2Configuration configuration, int score) {
        this.configuration = configuration;
        this.score = score;
    }


    public Exercise2Configuration getConfiguration() {
        return configuration;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(ConfigurationQueue o) {
        return Integer.compare(this.getScore(), o.getScore());
    }
}


