package jakobian;

import FEM.Element;

public class Jakobian {
    double[][] matrix = new double[4][4];
    double[] detJ = new double[4];
    LocalElement localElement=new LocalElement();

    public Jakobian(Element element){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[j][0] += localElement.getdNdksi()[i][j] * element.getNodes()[i].getX();
                matrix[j][1] += localElement.getdNdksi()[i][j] * element.getNodes()[i].getY();
                matrix[j][2] += localElement.getdNdeta()[i][j] * element.getNodes()[i].getX();
                matrix[j][3] += localElement.getdNdeta()[i][j] * element.getNodes()[i].getY();
            }
        }
        for (int i = 0; i < 4; i++) {
            detJ[i] = matrix[i][0] * matrix[i][3] - matrix[i][1] * matrix[i][2];
        }
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public double[] getDetJ() {
        return detJ;
    }
}
