package FEM;

public class Element {
    private int id;
//    private int nodeIds[];
    private Node nodes[]=new Node[4];

    public Element(int id, Node lowerLeft, Node lowerRight, Node upperRight, Node upperLeft) {
        nodes[0]=lowerLeft;
        nodes[1]=lowerRight;
        nodes[2]=upperRight;
        nodes[3]=upperLeft;
//        nodeIds=new int[4];
        this.id=id;
//        this.nodeIds[0]=lowerLeft;
//        this.nodeIds[1]=lowerRight;
//        this.nodeIds[2]=upperRight;
//        this.nodeIds[3]=upperLeft;
    }
    public void showElement(){
        System.out.println();
    }
    public Node getLowerLeft(){
        return nodes[0];
    }
    public Node getLowerRight(){
        return nodes[1];
    }
    public Node getUpperRight(){
        return nodes[2];
    }
    public Node getUpperLeft(){
        return nodes[3];
    }

    public Node[] getNodes() {
        return nodes;
    }
}
