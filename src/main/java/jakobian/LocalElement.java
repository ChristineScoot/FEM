package jakobian;

public class LocalElement {
    private double[] ksi = {-1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3)};
    private double[] eta = {-1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3)};
    private double[] ksiSurface = {-1 / Math.sqrt(3), 1 / Math.sqrt(3), 1, 1, 1 / Math.sqrt(3), -1 / Math.sqrt(3), -1, -1};
    private double[] etaSurface = {-1, -1, -1 / Math.sqrt(3), 1 / Math.sqrt(3), 1, 1, 1 / Math.sqrt(3), -1 / Math.sqrt(3)};
    private double[] N1 = new double[4];
    private double[] N2 = new double[4];
    private double[] N3 = new double[4];
    private double[] N4 = new double[4];
    private double[][] NSurface = new double[2][4];
    private double[][] dNdksi = new double[4][4];
    private double[][] dNdeta = new double[4][4];

    public LocalElement() {
        //To jest stałe dla wszystkich elementów
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
//        for (int i = 0; i < 4; i++) {
//            NSurface[0][i] = N(-ksiSurface[2*i], -etaSurface[2*i]);
//            NSurface[1][i] = N(+ksiSurface[2*i], -etaSurface[2*i]);
//            NSurface[2][i] = N(+ksiSurface[2*i], +etaSurface[2*i]);
//            NSurface[3][i] = N(-ksiSurface[2*i], +etaSurface[2*i]);
//        }
        for (int i = 0; i < 4; i++) {
            NSurface[0][0] = N(-ksiSurface[2 * i], -etaSurface[2 * i]);
            NSurface[0][1] = N(ksiSurface[2 * i], -etaSurface[2 * i]);
            NSurface[0][2] = N(ksiSurface[2 * i], etaSurface[2 * i]);
            NSurface[0][3] = N(-ksiSurface[2 * i], etaSurface[2 * i]);

            NSurface[1][0] = N(-ksiSurface[2 * i + 1], -etaSurface[2 * i + 1]);
            NSurface[1][1] = N(ksiSurface[2 * i + 1], -etaSurface[2 * i + 1]);
            NSurface[1][2] = N(ksiSurface[2 * i + 1], etaSurface[2 * i + 1]);
            NSurface[1][3] = N(-ksiSurface[2 * i + 1], etaSurface[2 * i + 1]);
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

    public void printdNdeta() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    System.out.println("Funkcja kształtu nr " + i);
                }
                System.out.println(dNdeta[i][j]);
            }
        }
    }

    public double[][] getFunkcjeKsztaltu() {
        double[][] funkcje = new double[4][4];
        funkcje[0] = N1;
        funkcje[1] = N2;
        funkcje[2] = N3;
        funkcje[3] = N4;
        return funkcje;
    }

    public void showFK() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.printf("%f ", NSurface[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public double[][] getNSurface() {
        return NSurface;
    }
}
