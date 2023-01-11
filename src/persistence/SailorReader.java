package persistence;

import business.Sailor;
import business.Ship;
import business.ShipType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

                //Crear hashmap
                HashMap <ShipType, Integer> types = new HashMap<>();

                types.put(ShipType.Windsurf, Integer.parseInt(split[3]));
                types.put(ShipType.Optimist, Integer.parseInt(split[4]));
                types.put(ShipType.Laser, Integer.parseInt(split[5]));
                types.put(ShipType.PatiCatala, Integer.parseInt(split[6]));
                types.put(ShipType.HobieDragoon, Integer.parseInt(split[7]));
                types.put(ShipType.HobieCat, Integer.parseInt(split[8]));

                sailors.add(new Sailor(Integer.parseInt(split[0]), split[1],
                        Double.parseDouble(split[2]), types,
                        Integer.parseInt(split[9])
                        ));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sailors;
    }


}
