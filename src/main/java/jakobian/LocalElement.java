package jakobian;

import lombok.Getter;

@Getter
public class LocalElement {
    private double point = 1.0 / Math.sqrt(3);
    private double[] ksi = {-point, point, point, -point};
    private double[] eta = {-point, -point, point, point};
    private double[] ksiSurface = {-point, point, 1.0, 1.0, point, -point, -1.0, -1.0};
    private double[] etaSurface = {-1, -1, -point, point, 1.0, 1.0, point, -point};
    private double[] N1 = new double[4];
    private double[] N2 = new double[4];
    private double[] N3 = new double[4];
    private double[] N4 = new double[4];
    private double[][] NSurface = new double[2][4];
    private double[][] dNdksi = new double[4][4];
    private double[][] dNdeta = new double[4][4];

    public LocalElement() {
        for (int i = 0; i < 4; i++) {
            N1[i] = N(-ksi[i], -eta[i]);
            N2[i] = N(ksi[i], -eta[i]);
            N3[i] = N(ksi[i], eta[i]);
            N4[i] = N(-ksi[i], eta[i]);
        }
        for (int i = 0; i < 4; i++) {
            dNdksi[0][i] = -0.25 * (1 - eta[i]);
            dNdksi[1][i] = -dNdksi[0][i];
            dNdksi[2][i] = 0.25 * (1 + eta[i]);
            dNdksi[3][i] = -dNdksi[2][i];
        }
        for (int i = 0; i < 4; i++) {
            dNdeta[0][i] = -0.25 * (1 - ksi[i]);
            dNdeta[1][i] = -0.25 * (1 + ksi[i]);
            dNdeta[2][i] = -dNdeta[1][i];
            dNdeta[3][i] = -dNdeta[0][i];
        }
    }

    private static double N(double ksi, double eta) {
        return 0.25 * (1 + eta) * (1 + ksi);
    }

    public void printdNdeta() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    System.out.println("Shape function nr " + i);
                }
                System.out.println(dNdeta[i][j]);
            }
        }
    }

    public double[][] getShapeFunctions() {
        double[][] functions = new double[4][4];
        functions[0] = N1;
        functions[1] = N2;
        functions[2] = N3;
        functions[3] = N4;
        return functions;
    }

    public double[][] getNSurface(int edge) {
        NSurface[0][0] = N(-ksiSurface[2 * edge], -etaSurface[2 * edge]);
        NSurface[0][1] = N(ksiSurface[2 * edge], -etaSurface[2 * edge]);
        NSurface[0][2] = N(ksiSurface[2 * edge], etaSurface[2 * edge]);
        NSurface[0][3] = N(-ksiSurface[2 * edge], etaSurface[2 * edge]);

        NSurface[1][0] = N(-ksiSurface[2 * edge + 1], -etaSurface[2 * edge + 1]);
        NSurface[1][1] = N(ksiSurface[2 * edge + 1], -etaSurface[2 * edge + 1]);
        NSurface[1][2] = N(ksiSurface[2 * edge + 1], etaSurface[2 * edge + 1]);
        NSurface[1][3] = N(-ksiSurface[2 * edge + 1], etaSurface[2 * edge + 1]);
        return NSurface;
    }
}
