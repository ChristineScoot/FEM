package FEM;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {
    int H, L, nH, nL;

    public FileReader() throws FileNotFoundException {
        readFromFile();
    }

    public void readFromFile() throws FileNotFoundException {
        int[] data = new int[4];
        System.out.println("Wczytywanie danych z pliku .txt");
        File file = new File("data.txt");
        Scanner read = new Scanner(file);
        int counter = 0;
        while (read.hasNextLine()) {
            String[] dataInString = read.nextLine().split("=");
            data[counter] = Integer.parseInt(dataInString[1]);
            counter++;
        }
        H = data[0];
        L = data[1];
        nH = data[2];
        nL = data[3];
    }

    public int getH() {
        return H;
    }

    public int getL() {
        return L;
    }

    public int getnH() {
        return nH;
    }

    public int getnL() {
        return nL;
    }

    public void printData(){
        System.out.println("H=" + H);
        System.out.println("L=" + L);
        System.out.println("nH=" + nH);
        System.out.println("nL=" + nL);
        System.out.println();
    }
}
