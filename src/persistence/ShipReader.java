package persistence;

import business.Ship;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ShipReader {

    private static final String SHIP_ROUTE = "files/match.csv";

    public ArrayList<Ship> readAllShips(){
        String currentLine;
        BufferedReader reader;

        ArrayList<Ship> ships = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(SHIP_ROUTE));

            reader.readLine();

            while ((currentLine = reader.readLine()) != null) {

                System.out.println(currentLine);

                String[] split = currentLine.split(";");

                //ships.add(new Ship());
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return ships;
    }





}
