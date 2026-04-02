/**
 * A concrete location representing an Airport with a terminal count.
 */
public final class Airport extends Location {
    private int numberOfTerminals;

    public Airport(String name, double x, double y, int numberOfTerminals) {
        super(name, x, y);
        this.numberOfTerminals = numberOfTerminals;
    }

    public int getNumberOfTerminals() {
        return numberOfTerminals;
    }

    public void setNumberOfTerminals(int numberOfTerminals) {
        this.numberOfTerminals = numberOfTerminals;
    }

    @Override
    public String toString() {
        return "Airport{name='" + name + "', x=" + x + ", y=" + y + ", terminals=" + numberOfTerminals + "}";
    }
}