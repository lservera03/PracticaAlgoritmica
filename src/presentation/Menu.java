package presentation;

import java.util.*;

/**
 * Class that allows to show information.
 */
public class Menu {

    private Scanner sc;


    /**
     * Method that allows to show the main title of the application.
     */
    public void showTitle() {
        System.out.println(" _____ _            _____      _       _      ");
        System.out.println("/__   \\ |__   ___  /__   \\_ __(_) __ _| |___  ");
        System.out.println("  / /\\/ '_ \\ / _ \\   / /\\/ '__| |/ _` | / __| ");
        System.out.println(" / /  | | | |  __/  / /  | |  | | (_| | \\__ \\ ");
        System.out.println(" \\/   |_| |_|\\___|  \\/   |_|  |_|\\__,_|_|___/ ");
        System.out.println();

    }

    public void showPrincipalMenu(){
        System.out.println("*************CatTheHobie*************");
        System.out.println("1. Navegación de alta velocidad");
        System.out.println("2. Flota al completo");
        System.out.println("3. Salir");
        System.out.println("*************************************");
    }

    public void showMenuExercise1(){

    }

    public void showMenuExercise2(){
        System.out.println("***Backtracking vs Branch & Bound***");
        System.out.println("1.Backtracking");
        System.out.println("2.Branch & Bound ");
        System.out.println("************************************");
    }

    public void showMenuExercise2Bactracking(){
        System.out.println("************Backtracking************");
        System.out.println("1.Simple");
        System.out.println("2.Con marcaje y PBMSC");
        System.out.println("************************************");

    }
    /**
     * Function that allows to ask for an Integer doing some checkings.
     *
     * @param msg Receives a String which will be the message shown to ask the Integer.
     * @return the integer introduced by the user.
     */
    public int askForInteger(String msg) {
        int number;
        boolean correct;

        correct = false;
        number = -1;


        do {
            sc = new Scanner(System.in);
            this.showString(msg);
            try {
                number = sc.nextInt();

                correct = true;
            } catch (InputMismatchException ex) {
                System.err.println("ERROR: Enter a number!");
            }

        } while (!correct);

        return number;
    }

    /**
     * Function that allows to ask for an Integer doing some checkings.
     *
     * @param msg Receives a String which will be the message shown to ask the Integer.
     * @return the integer introduced by the user.
     */
    public int askForIntegerWithRange(String msg, int min, int max) {
        int number;
        boolean correct;

        correct = false;
        number = -1;


        do {
            sc = new Scanner(System.in);
            this.showString(msg + "[" + min + "," + max + "]");
            try {
                number = sc.nextInt();

                if (number >= min && number <= max) {
                    correct = true;
                } else {
                    System.out.println("ERROR: The number is not in the required range");
                }

            } catch (InputMismatchException ex) {
                System.err.println("ERROR: Enter a number!");
            }

        } while (!correct);

        return number;
    }

    /**
     * Function that allows to ask for an Integer doing some checkings.
     *
     * @param msg Receives a String which will be the message shown to ask the Integer.
     * @return the integer introduced by the user.
     */
    public int askForIntegerOfAStringList(String msg, ArrayList<String> strings) {
        int number;
        boolean correct;

        correct = false;
        number = -1;


        do {
            for (int i = 1; i <= strings.size(); i++) {
                if (strings.get(i - 1).equalsIgnoreCase("Back")) {
                    System.out.println();
                }
                System.out.println("\t" + i + ") " + strings.get(i - 1));
            }
            sc = new Scanner(System.in);
            this.showString(msg);
            try {
                number = sc.nextInt();

                if (number > 0 && number <= strings.size()) {
                    correct = true;
                } else {
                    System.out.println("ERROR: Enter a option number of the list");
                }

            } catch (InputMismatchException ex) {
                System.err.println("ERROR: Enter a number!");
            }

        } while (!correct);

        return number;
    }

    /**
     * Method that allows to show a list of Strings numered.
     *
     * @param msg     Receives a string with the message to show.
     * @param strings Receives an Arraylist of Strings to show.
     */
    public void showStringOfAStringList(String msg, ArrayList<String> strings) {
        System.out.println(msg);
        for (int i = 1; i <= strings.size(); i++) {
            System.out.println("\t" + i + ") " + strings.get(i - 1));
        }
    }


    /**
     * Function that allows to ask for a String.
     *
     * @param msg Receives a String which will be the message shown to ask the String.
     * @return the Stirng introduced by the user.
     */
    public String askForString(String msg) {
        String string;
        boolean correct;

        correct = false;

        do {
            sc = new Scanner(System.in);
            this.showString(msg);

            string = sc.nextLine();

            if (string.isEmpty()) {
                this.showString("ERROR: The field can't be empty");
            } else {
                correct = true;
            }

        } while (!correct);

        return string;
    }

    /**
     * Function that allows to ask for a String from a predefined group of strings.
     *
     * @param msg     Receives a String containing the message to show.
     * @param strings Receives an Arraylist of Strings containing all the options.
     * @return Return the string introduced by the user.
     */
    public String askForStringOfAStringList(String msg, List<String> strings) {
        boolean correct;
        StringBuilder stringBuilder;
        String string;

        stringBuilder = new StringBuilder();
        correct = false;

        do {
            sc = new Scanner(System.in);

            stringBuilder.append(msg);
            stringBuilder.append(" [");
            for (String s : strings) {
                stringBuilder.append(s);
                stringBuilder.append(" ");
            }
            stringBuilder.append("]:");

            string = askForString(stringBuilder.toString());

            for (String s : strings) {
                if (s.equals(string)) {
                    correct = true;
                    break;
                }
            }

            if (!correct) {
                this.showString("ERROR: There isn't any option named " + string);
            }

        } while (!correct);

        return string;
    }


    /**
     * Method that shows a message in the console.
     *
     * @param msg Receives the String to show.
     */
    public void showString(String msg) {
        System.out.println(msg);
    }
}