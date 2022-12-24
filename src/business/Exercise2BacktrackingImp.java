package business;

import persistence.ShipReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Exercise2BacktrackingImp extends Backtracking {

    private ArrayList<Ship> ships;
    private int NUM_SHIPS;

    private ArrayList<Center> centers = new ArrayList<>();
    private int NUM_CENTERS;

    private int[] bestConfig;
    private int bestCentersUsed;


    private boolean marking;
    private boolean pbmsc;

    @Override
    public void run(boolean marking, boolean pbmsc) {

        this.marking = marking;
        this.pbmsc = pbmsc;

        //READ SHIPS
        ShipReader shipReader = new ShipReader();
        ships = shipReader.readAllShips();

        NUM_SHIPS = ships.size();

        //create center arraylist
        createCentersArray();

        NUM_CENTERS = centers.size();

        bestCentersUsed = NUM_CENTERS;

        //init basic configuration
        int[] x = new int[NUM_CENTERS];
        int k = 0;

        //TODO control execution time

        if (marking) {
            Marking m = new Marking();

            Map<ShipType, Integer> counterTypes = new HashMap<>();

            counterTypes.put(ShipType.Windsurf, 0);
            counterTypes.put(ShipType.Optimist, 0);
            counterTypes.put(ShipType.Laser, 0);
            counterTypes.put(ShipType.PatiCatala, 0);
            counterTypes.put(ShipType.HobieDragoon, 0);
            counterTypes.put(ShipType.HobieCat, 0);

            m.setTypes(counterTypes);

            backtracking(x, k, m);
        } else {
            backtracking(x, k, null);
        }


        //TODO show best solution pretty
        System.out.println("Mejor configuración: ");
        System.out.println(Arrays.toString(bestConfig));
        System.out.println("Centros usados: " + bestCentersUsed);
    }

    public void backtracking(int[] x, int k, Marking m) {

        prepareLevelTour(x, k);

        while (isThereSuccessor(x, k)) {
            nextBrother(x, k);

            if (this.marking) {
                mark(x, k, m);
            }

            if (solution(x, k)) {
                if (this.marking) {

                    if (markedFeasible(x, m)) {
                        markedTreatSolution(x, m);
                    } else {
                        //INCORRECT SOLUTION
                    }

                } else {
                    if (feasible(x)) {
                        treatSolution(x);

                    } else {
                        //INCORRECT SOLUTION
                    }
                }

            } else {
                if (completable(x, k)) {

                    if (this.pbmsc) {
                        if (m.getCentersUsed() < bestCentersUsed) {
                            backtracking(x, k + 1, m);
                        } else {
                            //PODA
                        }
                    } else {
                        backtracking(x, k + 1, m);
                    }

                } else {
                    //PODA
                }
            }

            if (this.marking) {
                unMark(x, k, m);
            }

        }
    }


    @Override
    public void prepareLevelTour(int[] x, int k) {
        x[k] = -1;
    }

    @Override
    public boolean isThereSuccessor(int[] x, int k) {
        return x[k] < 1;
    }

    @Override
    public void nextBrother(int[] x, int k) {
        x[k]++;
    }

    @Override
    public boolean solution(int[] x, int k) {
        return k == (NUM_CENTERS - 1);
    }

    @Override
    public boolean completable(int[] x, int k) {
        return true;
    }

    @Override
    public boolean feasible(int[] x) {
        Map<ShipType, Integer> counterTypes = new HashMap<>();
        int counter = 0;

        counterTypes.put(ShipType.Windsurf, 0);
        counterTypes.put(ShipType.Optimist, 0);
        counterTypes.put(ShipType.Laser, 0);
        counterTypes.put(ShipType.PatiCatala, 0);
        counterTypes.put(ShipType.HobieDragoon, 0);
        counterTypes.put(ShipType.HobieCat, 0);


        for (int i = 0; i < NUM_CENTERS; i++) {

            if (x[i] == 1) { //If the center is selected, check the types of its ships

                for (Ship s : centers.get(i).getShips()) {

                    if (counterTypes.get(s.getType()) == 0) {
                        counter++;
                    }
                    counterTypes.put(s.getType(), counterTypes.get(s.getType()) + 1);

                }
            }
        }


        return counter == counterTypes.size();
    }


    private boolean markedFeasible(int[] x, Marking m) {
        return m.getNumTypes() == m.getTypes().size();
    }


    public void treatSolution(int[] x) {
        int counter = 0;

        for (int i = 0; i < NUM_CENTERS; i++) {

            if (x[i] == 1) {
                counter++;
            }
        }

        if (counter <= bestCentersUsed) {
            bestCentersUsed = counter;
            bestConfig = Arrays.copyOf(x, NUM_CENTERS);
        }

    }


    public void markedTreatSolution(int[] x, Marking m) {

        if (m.getCentersUsed() <= bestCentersUsed) {
            bestCentersUsed = m.getCentersUsed();
            bestConfig = Arrays.copyOf(x, NUM_CENTERS);
        }

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


    private void mark(int[] x, int k, Marking m) {

        if (x[k] == 1) {
            m.addCentersUsed();

            for (Ship s : centers.get(k).getShips()) {

                m.addType(s.getType());

                if (m.getTypes().get(s.getType()) == 1) {
                    m.addNumType();
                }
            }
        }

    }

    private void unMark(int[] x, int k, Marking m) {

        if (x[k] == 1) {
            m.subtractCentersUsed();

            for (Ship s : centers.get(k).getShips()) {

                if (m.getTypes().get(s.getType()) > 0) {
                    m.subtractType(s.getType());
                }

                if (m.getTypes().get(s.getType()) == 0) {
                    m.subtractNumType();
                }

            }

        }

    }


}


class Marking {

    private Map<ShipType, Integer> types = new HashMap<>();
    private int numTypes = 0;
    private int centersUsed = 0;


    public void addType(ShipType type) {
        this.types.put(type, this.types.get(type) + 1);
    }

    public void subtractType(ShipType type) {
        this.types.put(type, this.types.get(type) - 1);
    }

    public void addNumType() {
        this.numTypes++;
    }

    public void subtractNumType() {
        this.numTypes--;
    }

    public void addCentersUsed() {
        this.centersUsed++;
    }

    public void subtractCentersUsed() {
        this.centersUsed--;
    }

    public Map<ShipType, Integer> getTypes() {
        return types;
    }

    public int getNumTypes() {
        return numTypes;
    }

    public int getCentersUsed() {
        return centersUsed;
    }

    public void setTypes(Map<ShipType, Integer> types) {
        this.types = types;
    }
}


