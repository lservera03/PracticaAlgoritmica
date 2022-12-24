package business;

import persistence.ShipReader;

import java.util.ArrayList;
import java.util.Set;

public class Exercise2BacktrackingImp extends Backtracking {

    private ArrayList<Ship> ships;
    private int NUM_SHIPS;

    private ArrayList<Center> centers = new ArrayList<>();
    private int NUM_CENTERS;

    @Override
    public void run() {
        //READ SHIPS
        ShipReader shipReader = new ShipReader();
        ships = shipReader.readAllShips();

        NUM_SHIPS = ships.size();

        //create center arraylist
        createCentersArray();

        NUM_CENTERS = centers.size();
    }

    public void backtracking(int[] x, int k) {

        prepareLevelTour(x, k);

        while (isThereSuccessor(x, k)) {
            nextBrother(x, k);

            if (solution(x, k)) {
                if (feasible(x)) {
                    //TODO mostrar solucion / optimización (tratarSolucion)
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
        return k < NUM_CENTERS;
    }

    @Override
    public boolean completable(int[] x, int k) {
        return true;
    }

    @Override
    public boolean feasible(int[] x) {
        return false;
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
