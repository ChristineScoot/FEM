package FEM;

public class Node {
    private int id;
    private double x, y, temp;

    public Node(int id, double x, double y, double temp) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.temp = temp;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTemp() {
        return temp;
    }
}
