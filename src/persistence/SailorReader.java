package persistence;

import business.Sailor;

import java.io.BufferedReader;
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

                System.out.println(currentLine);

                String[] split = currentLine.split(";");

                //ships.add(new Ship());
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return sailors;
    }












}
