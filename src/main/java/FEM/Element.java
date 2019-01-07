package FEM;

import jakobian.Jacoby;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Element {
    private int id;
    private Node nodes[] = new Node[4];
    private double k, ro, c, alpha, ambientTemp, dTau;
    private Jacoby jacoby;
    private double[][] H1Volume, matrixC, H2Surface, matrixH;
    private double[] matrixP;
    private double[] sideLengths;
    private Edge[] edge;

    public Element(int id, Node lowerLeft, Node lowerRight, Node upperRight, Node upperLeft) {
        nodes[0] = lowerLeft;
        nodes[1] = lowerRight;
        nodes[2] = upperRight;
        nodes[3] = upperLeft;
        sideLengths = new double[4];
        sideLengths[0] = Math.sqrt(Math.pow(lowerRight.getX() - lowerLeft.getX(), 2) + Math.pow(lowerRight.getY() - lowerLeft.getY(), 2));
        sideLengths[1] = Math.sqrt(Math.pow(upperRight.getX() - lowerRight.getX(), 2) + Math.pow(upperRight.getY() - lowerRight.getY(), 2));
        sideLengths[2] = Math.sqrt(Math.pow(upperRight.getX() - upperLeft.getX(), 2) + Math.pow(upperRight.getY() - upperLeft.getY(), 2));
        sideLengths[3] = Math.sqrt(Math.pow(upperLeft.getX() - lowerLeft.getX(), 2) + Math.pow(upperLeft.getY() - lowerLeft.getY(), 2));
        edge = new Edge[4];
        int i = 0;
        int j = 1;
        boolean boundry = false;
        while (true) {
            if (j > 3) {
                j = 0;
            }
            if (nodes[i].isBoundry() && nodes[j].isBoundry()) boundry = true;

            edge[i] = new Edge(new Node[]{nodes[i], nodes[j]}, boundry);
            if (j == 0) break;
            i++;
            j++;
            boundry = false;
        }
        this.id = id;
        jacoby = new Jacoby(nodes);
    }

    public Node getLowerLeft() {
        return nodes[0];
    }

    public Node getLowerRight() {
        return nodes[1];
    }

    public Node getUpperRight() {
        return nodes[2];
    }

    public Node getUpperLeft() {
        return nodes[3];
    }

    public Node[] getNodes() {
        return nodes;
    }
}
