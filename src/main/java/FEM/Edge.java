package FEM;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Edge {
    private Node nodes[] = new Node[2];
    private boolean boundry;
}
