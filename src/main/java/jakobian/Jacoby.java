package jakobian;

import FEM.Node;
import lombok.Getter;

@Getter
public class Jacoby {
    double[][] matrix = new double[4][4];
    double[] detJ = new double[4];
    LocalElement localElement=new LocalElement();

    public Jacoby(Node[] nodes){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[j][0] += localElement.getDNdksi()[i][j] * nodes[i].getX();
                matrix[j][1] += localElement.getDNdksi()[i][j] * nodes[i].getY();
                matrix[j][2] += localElement.getDNdeta()[i][j] * nodes[i].getX();
                matrix[j][3] += localElement.getDNdeta()[i][j] * nodes[i].getY();
            }
        }
        for (int i = 0; i < 4; i++) {
            detJ[i] = matrix[i][0] * matrix[i][3] - matrix[i][1] * matrix[i][2];
        }
    }
}
