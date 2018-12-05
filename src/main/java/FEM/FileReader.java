package FEM;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {
    double H, L, c, ro;
    int nH, nL;

    public FileReader() throws FileNotFoundException {
        readFromFile();
    }

    public void readFromFile() throws FileNotFoundException {
        double[] data = new double[6];
        System.out.println("Wczytywanie danych z pliku .txt");
        File file = new File("data.txt");
        Scanner read = new Scanner(file);
        int counter = 0;
        while (read.hasNextLine()) {
            String[] dataInString = read.nextLine().split("=");
            data[counter] = Double.parseDouble(dataInString[1]);
            counter++;
        }
        H = data[0];
        L = data[1];
        nH = (int) data[2];
        nL = (int) data[3];
        c = data[4];
        ro = data[5];
    }

    public double getH() {
        return H;
    }

    public double getL() {
        return L;
    }

    public int getnH() {
        return nH;
    }

    public int getnL() {
        return nL;
    }

    public double getC() {
        return c;
    }

    public double getRo() {
        return ro;
    }

    public void printData() {
        System.out.println("H=" + H);
        System.out.println("L=" + L);
        System.out.println("nH=" + nH);
        System.out.println("nL=" + nL);
        System.out.println();
    }
}
