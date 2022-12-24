package business;

import java.util.ArrayList;

public class Center {

    private String name;
    private ArrayList<Ship> ships = new ArrayList<>();


    public Center(String name) {
        this.name = name;
    }


    public void addShip(Ship s){
        this.ships.add(s);
    }

}