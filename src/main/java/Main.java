import FEM.Grid;
import FEM.Simulation;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid();
//        grid.showGrid();
//        grid.showElements();
//        grid.showEdges();
        Simulation simulation = new Simulation(grid);
        grid.showGlobalMatrixH(); //FIXME czy h2Surface jest ok  ?!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!????????????????????
//        grid.showGlobalMatrixC();
//        grid.showGlobalMatrixP();
//        grid.showMatrixHzDaszkiem();
//        grid.showMatrixPzDaszkiem();

    }
}