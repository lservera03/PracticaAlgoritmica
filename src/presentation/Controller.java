package presentation;

import business.Exercise2BacktrackingImp;

public class Controller {

    private Menu menu;


    public Controller(Menu menu) {
        this.menu = menu;
    }

    public void run() {


        //TODO show menu

        Exercise2BacktrackingImp exercise2BacktrackingImp = new Exercise2BacktrackingImp();

        exercise2BacktrackingImp.run(false, false);


    }


}
