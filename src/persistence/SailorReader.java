package persistence;

import business.Sailor;
import business.Ship;
import business.ShipType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SailorReader {

    private static final String SAILOR_ROUTE = "datasets/sailors/sailorsXS.txt";

    public ArrayList<Sailor> readAllSailors(){
        String currentLine;
        BufferedReader reader;

        ArrayList<Sailor> sailors = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(SAILOR_ROUTE));

            reader.readLine();

            while ((currentLine = reader.readLine()) != null) {

                String[] split = currentLine.split(";");

                sailors.add(new Sailor(Integer.parseInt(split[0]), split[1],
                        Double.parseDouble(split[3]),ShipType.getEnumValue(split[2]),
                        Integer.parseInt(split[4])
                        ));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sailors;
    }












}
