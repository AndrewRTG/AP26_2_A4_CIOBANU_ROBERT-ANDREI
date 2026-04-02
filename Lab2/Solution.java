import java.util.Arrays;

/**
 * Represents the result of a route-finding algorithm.
 * It stores the sequence of roads that form the path from the start to the destination.
 * * @author Andrei
 */
public class Solution {
    private Road[] path;
    private double totalCost;
    private String type; // "Shortest" or "Fastest"

    public Solution(Road[] path, double totalCost, String type) {
        this.path = path;
        this.totalCost = totalCost;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "path=" + Arrays.toString(path) +
                ", totalCost=" + totalCost +
                ", mode='" + type + '\'' +
                '}';
    }
}
