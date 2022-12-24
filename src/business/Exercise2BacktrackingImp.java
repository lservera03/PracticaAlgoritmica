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


    @Override
    public void run() {
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

        backtracking(x, k);

        //show best solution

        System.out.println("Mejor configuración: ");
        System.out.println(Arrays.toString(bestConfig));
        System.out.println("Centros usados: " + bestCentersUsed);
    }

    public void backtracking(int[] x, int k) {

        prepareLevelTour(x, k);

        while (isThereSuccessor(x, k)) {
            nextBrother(x, k);

            if (solution(x, k)) {
                if (feasible(x)) {
                    //System.out.println(Arrays.toString(x));

                    treatSolution(x);

                } else {
                    //SOLUCION incorrecta
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


    private void treatSolution(int[] x) {
        int counter = 0;

        for (int i = 0; i < NUM_CENTERS; i++) {

            if(x[i] == 1){
                counter++;
            }
        }

        if(counter <= bestCentersUsed){
            bestCentersUsed = counter;
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


}
