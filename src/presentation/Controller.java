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


        //TODO show menu

        System.out.println("Backtracking");

        Exercise2BacktrackingImp exercise2BacktrackingImp = new Exercise2BacktrackingImp();

        exercise2BacktrackingImp.run(false, false);


        /**
         Exercise1BacktrackingImp exercise1BacktrackingImp = new Exercise1BacktrackingImp();

         exercise1BacktrackingImp.run(false, false);
         **/

        System.out.println("B&B");

        Exercise2BranchAndBoundImp exercise2BranchAndBoundImp = new Exercise2BranchAndBoundImp();

        exercise2BranchAndBoundImp.run();

    }


}
