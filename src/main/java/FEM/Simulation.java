package FEM;

import jakobian.Jakobian;
import jakobian.LocalElement;

import java.util.Arrays;

public class Simulation {
    private Grid grid;

    public Simulation(Grid grid) {
        this.grid = grid;
        simulate();
    }

    void simulate() {
        for (int currentElement = 0; currentElement < grid.getNumberOfElements(); currentElement++) {
            Jakobian jacoby = grid.getElements()[currentElement].getJacoby();
            LocalElement localElement = jacoby.getLocalElement();

            double[][] inverseJacoby = new double[4][4];
            for (int i = 0; i < 4; i++) {
                inverseJacoby[i][0] = jacoby.getMatrix()[i][3] / jacoby.getDetJ()[i];
                inverseJacoby[i][1] = jacoby.getMatrix()[i][1] / jacoby.getDetJ()[i];
                inverseJacoby[i][2] = jacoby.getMatrix()[i][2] / jacoby.getDetJ()[i];
                inverseJacoby[i][3] = jacoby.getMatrix()[i][0] / jacoby.getDetJ()[i];
            }

            double[][] dNdx = new double[4][4]; //dN/dx
            double[][] dNdy = new double[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    dNdx[i][j] = inverseJacoby[i][0] * localElement.getdNdksi()[i][j] + inverseJacoby[i][1] * localElement.getdNdeta()[i][j];
                    dNdy[i][j] = inverseJacoby[i][2] * localElement.getdNdksi()[i][j] + inverseJacoby[i][3] * localElement.getdNdeta()[i][j];
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
                        matrixH[i][j] += grid.getK() * (dndx[j] * dndx[i] + dndy[j] * dndy[i]) * grid.getElements()[0].getJacoby().getDetJ()[i];
                    }
                }
            }
            grid.getElements()[currentElement].setH1Volume(matrixH);


            double[][] matrixC = new double[4][4];
            double[][] NxN = new double[4][4];
//        matrixC = calka z c ro {N}{N}^T
            for (int point = 0; point < 4; point++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        NxN[i][j] = localElement.getFunkcjeKsztaltu()[point][i] * localElement.getFunkcjeKsztaltu()[point][j];
                        matrixC[i][j] += grid.getSpecificHeat() * grid.getDensity() * grid.getElements()[0].getJacoby().getDetJ()[i] * NxN[i][j];
                    }
                }
            }
            grid.getElements()[currentElement].setMatrixC(matrixC);


//H po powierzchni
            //FIXME      Tu chyba błąd------------------------------------
            double[][] matrixHSurface = new double[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrixHSurface[i][j] = 0;
                }
            }
            double[] detJ = {grid.getElements()[currentElement].getSideLengths()[0] / 2,
                    grid.getElements()[currentElement].getSideLengths()[1] / 2,
                    grid.getElements()[currentElement].getSideLengths()[2] / 2,
                    grid.getElements()[currentElement].getSideLengths()[3] / 2};
            double[][] pc1, pc2, sum;
            pc1 = new double[4][4];
            pc2 = new double[4][4];
            sum = new double[4][4];
            double[][] NSurface = localElement.getNSurface(); //[2][4]

            //localElement.showFK();
//            System.out.println("SUM:");
            for (int edge = 0; edge < 4; edge++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        pc1[i][j] = NSurface[0][i] * NSurface[0][j] * grid.getElements()[currentElement].getAlfa();
                        pc2[i][j] = NSurface[1][i] * NSurface[1][j] * grid.getElements()[currentElement].getAlfa();
                        sum[i][j] = (pc1[i][j] + pc2[i][j]) * detJ[edge];
                        if (currentElement == 0)
                            System.out.println(sum[i][j]);
                    }
                    System.out.println();
                }
                if (grid.getElements()[currentElement].getEdge()[edge].isBoundry()) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            matrixHSurface[i][j] += sum[i][j];
                        }
                    }
                }
            }
            grid.getElements()[currentElement].setH2Surface(matrixHSurface);

//Macierz P
            double[] matrixP = new double[4];
            for (int i = 0; i < 4; i++) {
                matrixP[i] = 0;
            }
            double[] ppc1, ppc2, psum;
            ppc1 = new double[4];
            ppc2 = new double[4];
            psum = new double[4];

            for (int edge = 0; edge < 4; edge++) {
                for (int i = 0; i < 4; i++) {
                    ppc1[i] = NSurface[0][i] * grid.getElements()[currentElement].getAlfa() * grid.getElements()[currentElement].getAmbientTemp();
                    ppc2[i] = NSurface[1][i] * grid.getElements()[currentElement].getAlfa() * grid.getElements()[currentElement].getAmbientTemp();
                    psum[i] = (ppc1[i] + ppc2[i]) * detJ[edge];
                }
                if (grid.getElements()[currentElement].getEdge()[edge].isBoundry()) {
                    for (int i = 0; i < 4; i++) {
                        matrixP[i] += psum[i];
                    }
                }
            }
            grid.getElements()[currentElement].setMatrixP(matrixP);
        }
        System.out.println("Simulation time");
        double simulationTime = grid.getSimulationTime();
        double simulationStepTime = grid.getSimulationStepTime();
        int steps = (int) (simulationTime / simulationStepTime);
        for (int i = 1; i <= steps; i++) {
            double time = i * simulationStepTime;
            System.out.println("After " + time + "s:");
            grid.resetMatrices();
            grid.calculateGlobalMatrixH();
            grid.calculateGlobalMatrixC();
            grid.calculateGlobalMatrixP();
            grid.calculateMatrixHzDaszkiem();
            grid.calculateMatrixPxDaszkiem();

            double[][] A = new double[grid.getNumberOfNodes()][grid.getNumberOfNodes()];
            double[] b = new double[grid.getNumberOfNodes()];
            for (int j = 0; j < grid.getNumberOfNodes(); j++) {
                int k;
                for (k = 0; k < grid.getNumberOfNodes(); k++) {
                    A[j][k] = grid.getMatrixHZDaszkiem()[j][k];
                }
                b[j] = grid.getMatrixPZDaszkiem()[j];
            }
            double[] x = GaussianElimination.lsolve(A, b);
//            if (i == 1) {
//                for (int j = 0; j < x.length; j++) {
//                    System.out.println(j + ".: t=" + x[j]);
//                }
//            }
            for (int j = 0; j < x.length; j++) {
                grid.getNodes()[j].setTemp(x[j]);
            }
            double min = Arrays.stream(x).min().getAsDouble();
            double max = Arrays.stream(x).max().getAsDouble();
            System.out.println("Max=" + max + "\nMin=" + min + "\n");
        }
    }

    private static double N(double ksi, double eta) {
        return 0.25 * (1 + eta) * (1 + ksi);
    }
}