package presentation;

import business.Exercise1BacktrackingImp;
import business.Exercise2BacktrackingImp;
import business.Exercise2BranchAndBoundImp;

public class Controller {

    private Menu menu;


    public Controller(Menu menu) {
        this.menu = menu;
    }

    public void run() {
        int option;

        //show menu
        do{
            menu.showPrincipalMenu();
            option = this.menu.askForInteger("Selecciona el ejercicio a realizar");
            switch (option){
                case 1:
                    exercise1(this.menu.askForInteger("Selecciona la opción para realizar el ejercicio"));
                    break;
                case 2:
                    this.menu.showMenuExercise2();
                    exercise2(this.menu.askForInteger("Selecciona la opción para realizar el ejercicio"));
                    break;
                case 3:
                    this.menu.showString("Saliendo....");
                    break;
                default:
                    this.menu.showString("Esta opción no existe!!!");
            }
        }while(option != 3);

    }

    public void exercise2(int option){
        switch (option) {
            case 1 -> {
                Exercise2BacktrackingImp exercise2BacktrackingImp = new Exercise2BacktrackingImp();
                exercise2BacktrackingImp.run(true, true);
            }
            case 2 -> {
                Exercise2BranchAndBoundImp exercise2BranchAndBoundImp = new Exercise2BranchAndBoundImp();
                exercise2BranchAndBoundImp.run();
            }
            default -> this.menu.showString("Esta opción no existe");
        }
    }


    public void exercise1(int option){
        switch (option) {
            case 1 -> {
                Exercise1BacktrackingImp exercise1BacktrackingImp = new Exercise1BacktrackingImp();
                exercise1BacktrackingImp.run(false, false);
            }
            case 2 -> {

            }
            default -> this.menu.showString("Esta opción no existe");
        }
    }


}
