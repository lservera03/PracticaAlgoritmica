package persistence;

import business.Ship;
import business.ShipType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ShipReader {

    private static final String SHIP_ROUTE = "datasets/boats/boatsM.txt";

    public ArrayList<Ship> readAllShips(){
        String currentLine;
        BufferedReader reader;

        ArrayList<Ship> ships = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(SHIP_ROUTE));

            reader.readLine(); //Skip first line

            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(";");

                ships.add(new Ship(Integer.parseInt(split[0]), split[1],
                        ShipType.getEnumValue(split[2]), Double.parseDouble(split[3]),
                                Double.parseDouble(split[4]), Integer.parseInt(split[5]),
                                        Integer.parseInt(split[6]), split[7], Integer.parseInt(split[8]), split[9]));
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return ships;
    }





}
