package jakobian;

public class LocalElement {
    double[] ksi = {-1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3)};
    double[] eta = {-1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3)};
    double[] N1 = new double[4];
    double[] N2 = new double[4];
    double[] N3 = new double[4];
    double[] N4 = new double[4];
    double[][] dNdksi = new double[4][4];
    double[][] dNdeta = new double[4][4];

    public LocalElement() {
        //To jest stałe dla wszytskich elementów
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

    public double[][] getdNdksi() {
        return dNdksi;
    }

    public double[][] getdNdeta() {
        return dNdeta;
    }
    public void printdNdeta(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    System.out.println("Funkcja kształtu nr " + i);
                }
                System.out.println(dNdeta[i][j]);
            }
        }
    }
}
