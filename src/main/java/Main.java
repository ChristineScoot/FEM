import FEM.Element;
import FEM.FileReader;
import FEM.Grid;
import FEM.Node;
import jakobian.Jakobian;
import jakobian.LocalElement;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader = new FileReader();
        fileReader.printData();

        Grid grid = new Grid(fileReader.getnH(), fileReader.getnL(), fileReader.getH(), fileReader.getL());
//        grid.showGrid();
//        grid.showElements();

//JAKOBIANY
        int k = 30; //conductivity-przewodność

        LocalElement localElement = new LocalElement();
        Element element = new Element(1, new Node(1, 0, 0, 20),
                new Node(2, 0.025, 0, 20),
                new Node(3, 0.025, 0.025, 20),
                new Node(4, 0, 0.025, 20));
        Jakobian jacoby = new Jakobian(element);

//TODO wyrzucic do innej klasy
        //wyznacznik/jakobian
        double[][] tymczasowaNazwa = new double[4][4];
        for (int i = 0; i < 4; i++) {
            tymczasowaNazwa[i][0] = jacoby.getMatrix()[i][3] / jacoby.getDetJ()[i];
            tymczasowaNazwa[i][1] = jacoby.getMatrix()[i][1] / jacoby.getDetJ()[i];
            tymczasowaNazwa[i][2] = jacoby.getMatrix()[i][2] / jacoby.getDetJ()[i];
            tymczasowaNazwa[i][3] = jacoby.getMatrix()[i][0] / jacoby.getDetJ()[i];
        }

        //dN/dx
        double[][] dNdx = new double[4][4];
        double[][] dNdy = new double[4][4];
//        double[][] dNdxT = new double[4][4]; //transponowane
//        double[][] dNdyT = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                dNdx[i][j] = tymczasowaNazwa[i][0] * localElement.getdNdksi()[i][j] + tymczasowaNazwa[i][1] * localElement.getdNdeta()[i][j];
                dNdy[i][j] = tymczasowaNazwa[i][2] * localElement.getdNdksi()[i][j] + tymczasowaNazwa[i][3] * localElement.getdNdeta()[i][j];
//                dNdxT[j][i] = dNdx[i][j];
//                dNdyT[j][i] = dNdy[i][j];
            }
        }
        double[][] matrixH = new double[4][4]; //macierz współczynników układów równań
        for (int point = 0; point < 4; point++) {
            double[] dndx = new double[4];
            double[] dndy = new double[4];
            for (int i = 0; i < 4; i++) {
                dndx[i] = dNdx[i][point];
                dndy[i] = dNdy[i][point];
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrixH[i][j] += k * (dndx[j] * dndx[i] + dndy[j] * dndy[i]) * jacoby.getDetJ()[i];
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    System.out.println("matrixH");
                }
                System.out.println(matrixH[i][j]);
            }
        }
    }
}