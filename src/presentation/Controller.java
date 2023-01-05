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
                menuExercise2Bactracking();
            }
            case 2 -> {
                Exercise2BranchAndBoundImp exercise2BranchAndBoundImp = new Exercise2BranchAndBoundImp();
                exercise2BranchAndBoundImp.run();
            }
            default -> this.menu.showString("Esta opción no existe");
        }
    }


    public void menuExercise2Bactracking(){
        menu.showMenuExercise2Bactracking();
        int option = menu.askForInteger("Escoge como quieres realizar el ejercicio");
        switch (option) {
            case 1 -> exercise2Bactracking(false, false);
            case 2 -> exercise2Bactracking(true, true);
            default -> this.menu.showString("Esta opción no existe");
        }
    }

    public void exercise2Bactracking(boolean marking, boolean pbmsc){
        Exercise2BacktrackingImp exercise2BacktrackingImp = new Exercise2BacktrackingImp();
        exercise2BacktrackingImp.run(marking, pbmsc);
    }



}
