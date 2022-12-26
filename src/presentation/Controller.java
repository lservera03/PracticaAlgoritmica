package presentation;

import business.Exercise1BacktrackingImp;
import business.Exercise2BacktrackingImp;

public class Controller {

    private Menu menu;


    public Controller(Menu menu) {
        this.menu = menu;
    }

    public void run() {


        //TODO show menu

        /**
         Exercise2BacktrackingImp exercise2BacktrackingImp = new Exercise2BacktrackingImp();

         exercise2BacktrackingImp.run(true, true);
         **/

        Exercise1BacktrackingImp exercise1BacktrackingImp = new Exercise1BacktrackingImp();

        exercise1BacktrackingImp.run(false, false);
    }


}
