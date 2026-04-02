/**
 * A concrete location representing a Gas Station with a specific fuel price.
 */
public final class GasStation extends Location {
    private double gasPrice;

    public GasStation(String name, double x, double y, double gasPrice) {
        super(name, x, y);
        this.gasPrice = gasPrice;
    }

    public double getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(double gasPrice) {
        this.gasPrice = gasPrice;
    }

    @Override
    public String toString() {
        return "GasStation{name='" + name + "', x=" + x + ", y=" + y + ", price=" + gasPrice + "}";
    }
}