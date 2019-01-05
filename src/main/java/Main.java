import FEM.Grid;
import FEM.Simulation;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid();
//        grid.showGrid();
//        grid.showElements();
//        grid.showEdges();
        Simulation simulation = new Simulation(grid);
//        grid.showGlobalMatrixH();
//        grid.showGlobalMatrixC();
//        grid.showGlobalMatrixP();
//        grid.showMatrixHzDaszkiem();
//        grid.showMatrixPzDaszkiem();

    }
}