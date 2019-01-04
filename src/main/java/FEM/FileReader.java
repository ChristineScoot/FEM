package FEM;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import lombok.Getter;

@Getter
public class FileReader {
    double H, L, specificHeat, ro, alfa, initialTemperature, simulationtime, simulationStepTime, ambientTemperature;
    int nH, nL, conductivity;

    public FileReader() throws FileNotFoundException {
        readFromFile();
    }

    public void readFromFile() throws FileNotFoundException {
        double[] data = new double[12];
//        System.out.println("Wczytywanie danych z pliku .txt");
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
        conductivity = (int) data[4];//k
        specificHeat = data[5];
        ro = data[6];
        alfa = data[7];//współczynnik konwekcyjnej wymiany ciepła
        initialTemperature = data[8];
        simulationtime = data[9];
        simulationStepTime=data[10];
        ambientTemperature=data[11];//otoczenia
    }

    public void printData() {
        System.out.println("H=" + H);
        System.out.println("L=" + L);
        System.out.println("nH=" + nH);
        System.out.println("nL=" + nL);
//        System.out.println("specificHeat=" + specificHeat);
//        System.out.println("ro=" + ro);
//        System.out.println("alfa=" + alfa);
        System.out.println();
    }
}
