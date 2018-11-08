package FEM;

public class Grid {
    private int numberOfNodes;
    private int numberOfElements;
    private int nH;
    private int nL;
    private int H;
    private int L;
    private Node[] nodes;
    private Element[] elements;

    public Grid(int nH, int nL, int H, int L) {
        this.H = H;
        this.L = L;
        this.nH = nH;
        this.nL = nL;
        this.numberOfNodes = nH * nL;
        this.numberOfElements = (nH - 1) * (nL - 1);
        nodes = new Node[numberOfNodes];
        elements = new Element[numberOfElements];
        generateGrid();
    }

    public void showGrid() {
        int counter = 0;
        for (int i = 0; i < nL; i++) {
            for (int j = 0; j < nH; j++) {
//                System.out.println("(" + nodes[(i * j) - 1].getX() + ", \t" + nodes[(i * j) - 1].getY() + ")");
                System.out.println("(" + nodes[counter].getX() + ", \t" + nodes[counter].getY() + ")");
                counter++;
            }
        }

//TODO wyświetl wierszami??????????
        //lepsze
//        int a;
//        for(int j=1; j<=nL; j++) {
//            a=0;
//            for (int i = nH * j; i > nH*a; i--) {
//                System.out.printf("%d\t", nodes[i-1].getId());
//                a++;
//            }
//            System.out.println("");
//        }

        //gorsze
//        System.out.println("wyswietlanie wierszami");
//        for (int i = nH; i > 0; i--) {
//            for (int j = 1; j <= nL; j++) {
//                System.out.printf("%d\t", nodes[(i*j)-1].getId());
//            }
//            System.out.println("");
//        }
//        for (int i = nH; i > 0; i--) {
//            System.out.printf("%d\t", nodes[(i*6)-1].getId());
//        }
    }

    public void showElements(){
        for(int i=0; i<numberOfElements; i++){
            Element element=elements[i];
//            System.out.println(nodes[element.getUpperLeft()-1]+"--"+nodes[element.getUpperRight()-1]);
//            System.out.println(nodes[element.getUpperLeft()-1].getId()+"--"+nodes[element.getUpperRight()-1].getId());
            System.out.println(element.getUpperLeft().getId()+"--"+element.getUpperRight().getId());
            System.out.println("|\t|");
//            System.out.println(nodes[element.getLowerLeft()-1]+"--"+nodes[element.getLowerLeft()-1]);
//            System.out.println(nodes[element.getLowerLeft()-1].getId()+"--"+nodes[element.getLowerRight()-1].getId());
            System.out.println(element.getLowerLeft().getId()+"--"+element.getLowerRight().getId());
            System.out.println();
        }
    }

    private void addNode(int id, double x, double y, double temp) {
        Node newNode = new Node(id, x, y, temp);
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
                elements[currentElement] = new Element(currentElement, nodes[lowerLeft-1], nodes[lowerRight-1], nodes[upperRight-1], nodes[upperLeft-1]);
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
//                System.out.println("Id=" + nodeId + "\t(" + dx * i + ", " + dy * j + ")");
                addNode(nodeId, dx * i, dy * j, 20.0);
                nodeId++;
            }
        }
    }
}
