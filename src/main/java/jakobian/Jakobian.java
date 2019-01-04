package jakobian;

import FEM.Element;
import FEM.Node;
import lombok.Getter;

@Getter
public class Jakobian {
    double[][] matrix = new double[4][4];
    double[] detJ = new double[4];
    LocalElement localElement=new LocalElement();

    public Jakobian(Node[] nodes){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[j][0] += localElement.getdNdksi()[i][j] * nodes[i].getX();
                matrix[j][1] += localElement.getdNdksi()[i][j] * nodes[i].getY();
                matrix[j][2] += localElement.getdNdeta()[i][j] * nodes[i].getX();
                matrix[j][3] += localElement.getdNdeta()[i][j] * nodes[i].getY();
            }
        }
        for (int i = 0; i < 4; i++) {
            detJ[i] = matrix[i][0] * matrix[i][3] - matrix[i][1] * matrix[i][2];
        }
    }
}
