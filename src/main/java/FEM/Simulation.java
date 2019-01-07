package FEM;

import jakobian.Jacoby;
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
            Jacoby jacoby = grid.getElements()[currentElement].getJacoby();
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
                    dNdx[i][j] = inverseJacoby[i][0] * localElement.getDNdksi()[i][j] + inverseJacoby[i][1] * localElement.getDNdeta()[i][j];
                    dNdy[i][j] = inverseJacoby[i][2] * localElement.getDNdksi()[i][j] + inverseJacoby[i][3] * localElement.getDNdeta()[i][j];
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

//MatrixC
            double[][] matrixC = new double[4][4];
            double[][] NxN = new double[4][4];
            for (int point = 0; point < 4; point++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        NxN[i][j] = localElement.getShapeFunctions()[point][i] * localElement.getShapeFunctions()[point][j];
                        matrixC[i][j] += grid.getSpecificHeat() * grid.getDensity() * grid.getElements()[0].getJacoby().getDetJ()[i] * NxN[i][j];
                    }
                }
            }
            grid.getElements()[currentElement].setMatrixC(matrixC);

//MatrixH on the surface, MatrixP
            double[] detJ = {grid.getElements()[currentElement].getSideLengths()[0] / 2,
                    grid.getElements()[currentElement].getSideLengths()[1] / 2,
                    grid.getElements()[currentElement].getSideLengths()[2] / 2,
                    grid.getElements()[currentElement].getSideLengths()[3] / 2};
            double[][] pc1, pc2;
            double[] ppc1, ppc2;
            pc1 = new double[4][4];
            pc2 = new double[4][4];
            ppc1 = new double[4];
            ppc2 = new double[4];
            double[][] NSurface; //[2][4]

            double[][] matrixHSurface = new double[4][4];
            double[] matrixP = new double[4];
            for (int edge = 0; edge < 4; edge++) {
                if (grid.getElements()[currentElement].getEdge()[edge].isBoundry()) {
                    NSurface = localElement.getNSurface(edge);
//Matrix H on the surface
                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 4; k++) {
                            pc1[j][k] = NSurface[0][j] * NSurface[0][k] * grid.getElements()[currentElement].getAlpha();
                            pc2[j][k] = NSurface[1][j] * NSurface[1][k] * grid.getElements()[currentElement].getAlpha();
                            matrixHSurface[j][k] += (pc1[j][k] + pc2[j][k]) * detJ[edge];
                        }
                    }
//Matrix P
                    for (int i = 0; i < 4; i++) {
                        ppc1[i] = NSurface[0][i] * grid.getElements()[currentElement].getAlpha() * grid.getElements()[currentElement].getAmbientTemp();
                        ppc2[i] = NSurface[1][i] * grid.getElements()[currentElement].getAlpha() * grid.getElements()[currentElement].getAmbientTemp();
                        matrixP[i] += (ppc1[i] + ppc2[i]) * detJ[edge];
                    }
                }
            }
            for (int i = 0; i < matrixP.length; i++) {
                matrixP[i] = -matrixP[i];
            }
            grid.getElements()[currentElement].setH2Surface(matrixHSurface);
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