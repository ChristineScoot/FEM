package FEM;

import lombok.Getter;

import java.io.FileNotFoundException;

@Getter
public class Grid {
    FileReader fileReader;
    private int numberOfNodes, numberOfElements, nH, nL;
    private double H, L, initialTemp, simulationTime, simulationStepTime, ambientTemp, alfa, specificHeat, k, density;
    private double[][] globalMatrixH, globalMatrixC, matrixHZDaszkiem;
    private double[] globalMatrixP, matrixPZDaszkiem;

    private Node[] nodes;
    private Element[] elements;

    public Grid() {
        try {
            fileReader = new FileReader();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        fileReader.printData();
        this.H = fileReader.getH();
        this.L = fileReader.getL();
        this.nH = fileReader.getNH();
        this.nL = fileReader.getNL();
        this.initialTemp = fileReader.getInitialTemperature();
        this.simulationTime = fileReader.getSimulationtime();
        this.simulationStepTime = fileReader.getSimulationStepTime();
        this.ambientTemp = fileReader.getAmbientTemperature();
        this.alfa = fileReader.getAlpha();
        this.specificHeat = fileReader.getSpecificHeat();
        this.k = fileReader.getConductivity();
        this.density = fileReader.getRo();

        this.numberOfNodes = nH * nL;
        this.numberOfElements = (nH - 1) * (nL - 1);
        nodes = new Node[numberOfNodes];
        elements = new Element[numberOfElements];
        generateGrid();

        globalMatrixH = new double[numberOfNodes][numberOfNodes];
        globalMatrixC = new double[numberOfNodes][numberOfNodes];
        globalMatrixP = new double[numberOfNodes];
        matrixHZDaszkiem = new double[numberOfNodes][numberOfNodes];
        matrixPZDaszkiem = new double[numberOfNodes];
    }

    public void showGrid() {
        System.out.println("Grid");
        int counter = 0;
        for (int i = 0; i < nL; i++) {
            for (int j = 0; j < nH; j++) {
                System.out.println("(" + nodes[counter].getX() + ", \t" + nodes[counter].getY() + ")");
                counter++;
            }
        }
    }

    public void showElements() {
        System.out.println("Elements");
        for (int i = 0; i < numberOfElements; i++) {
            Element element = elements[i];
            System.out.println(element.getUpperLeft().getId() + "--" + element.getUpperRight().getId());
            System.out.println("|\t|");
            System.out.println(element.getLowerLeft().getId() + "--" + element.getLowerRight().getId());
            System.out.println();
        }
    }

    private void addNode(int id, double x, double y, double temp) {
        boolean boundry = false;
        if (x == 0 || x >= L || y == 0 || y >= H) boundry = true;
        Node newNode = new Node(id, x, y, temp, boundry);
        nodes[id - 1] = newNode;
    }

    private void generateGrid() {
        generateNodes();
        generateElements();
    }

    private void generateElements() {
        int currentElement = 0;
        for (int i = 0; i < nL - 1; i++) {
            for (int j = 0; j < nH - 1; j++) {
                int lowerLeft = nodes[i * nH + j].getId();
                int lowerRight = nodes[(i + 1) * nH + j].getId();
                int upperRight = nodes[(i + 1) * nH + j + 1].getId();
                int upperLeft = nodes[i * nH + j + 1].getId();
                elements[currentElement] = new Element(currentElement, nodes[lowerLeft - 1], nodes[lowerRight - 1], nodes[upperRight - 1], nodes[upperLeft - 1]);
                elements[currentElement].setAmbientTemp(ambientTemp);
                elements[currentElement].setK(k);
                elements[currentElement].setAlpha(alfa);
                elements[currentElement].setRo(density);
                elements[currentElement].setC(specificHeat);
                elements[currentElement].setDTau(simulationStepTime);
                currentElement++;
            }
        }
    }

    private void generateNodes() {
        double dy = 1.0 * H / (nH - 1);
        double dx = 1.0 * L / (nL - 1);
        int nodeId = 1;
        for (int i = 0; i < nL; i++) {
            for (int j = 0; j < nH; j++) {
                addNode(nodeId, dx * i, dy * j, initialTemp);
                nodeId++;
            }
        }
    }

    public void calculateGlobalMatrixH() {
        for (int i = 0; i < numberOfElements; i++) {
            for (int j = 0; j < 4; j++) {
                for (int l = 0; l < 4; l++) {
                    int row = elements[i].getNodes()[j].getId() - 1;
                    int column = elements[i].getNodes()[l].getId() - 1;
                    globalMatrixH[row][column] += elements[i].getH1Volume()[j][l];
                    globalMatrixH[row][column] += elements[i].getH2Surface()[j][l];
                }
            }
        }
    }

    public void calculateGlobalMatrixC() {
        for (int i = 0; i < numberOfElements; i++) {
            for (int j = 0; j < 4; j++) {
                for (int l = 0; l < 4; l++) {
                    globalMatrixC[elements[i].getNodes()[j].getId() - 1][elements[i].getNodes()[l].getId() - 1] += elements[i].getMatrixC()[j][l];
                }
            }
        }
    }

    public void calculateGlobalMatrixP() {
        for (int i = 0; i < numberOfElements; i++) {
            for (int j = 0; j < 4; j++) {
                globalMatrixP[elements[i].getNodes()[j].getId() - 1] -= elements[i].getMatrixP()[j];
            }
        }
    }

    public void calculateMatrixHzDaszkiem() {
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                matrixHZDaszkiem[i][j] = globalMatrixH[i][j] + globalMatrixC[i][j] / simulationStepTime;
            }
        }
    }

    public void calculateMatrixPxDaszkiem() {
        double[] temp = new double[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                temp[i] += nodes[j].getTemp() * globalMatrixC[i][j] / simulationStepTime;
            }
            matrixPZDaszkiem[i] = temp[i] + globalMatrixP[i];
        }
    }

    public void resetMatrices() {
        for (int i = 0; i < numberOfNodes; i++) {
            globalMatrixP[i] = 0;
            for (int j = 0; j < numberOfNodes; j++) {
                globalMatrixH[i][j] = 0;
                globalMatrixC[i][j] = 0;
            }
        }
    }

    public void showGlobalMatrixH() {
        System.out.println("Global matrix H:");
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                System.out.printf("%f  ", globalMatrixH[i][j]);
            }
            System.out.println();
        }
    }

    public void showGlobalMatrixC() {
        System.out.println("Global matrix C:");
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                System.out.printf("%f  ", globalMatrixC[i][j]);
            }
            System.out.println();
        }
    }

    public void showGlobalMatrixP() {
        System.out.println("Global matrix P: ");
        for (int i = 0; i < numberOfNodes; i++) {
            System.out.println(globalMatrixP[i]);
        }
    }

    public void showMatrixPzDaszkiem() {
        System.out.println("Matrix P^: ");
        for (int i = 0; i < numberOfNodes; i++) {
            System.out.println(matrixPZDaszkiem[i]);
        }
    }

    public void showMatrixHzDaszkiem() {
        System.out.println("Matrix H^:");
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                System.out.printf("%f  ", matrixHZDaszkiem[i][j]);
            }
            System.out.println();
        }
    }

    public void showEdges() {
        for (int i = 0; i < numberOfElements; i++) {
            System.out.println("Element nr: " + i);
            System.out.println("\t" + elements[i].getEdge()[2].isBoundry());
            System.out.println(elements[i].getEdge()[3].isBoundry() + "\t" + elements[i].getEdge()[1].isBoundry());
            System.out.println("\t" + elements[i].getEdge()[0].isBoundry());
        }
    }
}
