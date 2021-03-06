package FEM;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Node {
    private int id;
    private double x, y, temp;
    private boolean boundry;
}
