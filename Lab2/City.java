/**
 * Represents a City location with a specific population count.
 * * @author Andrei
 */
public final class City extends Location {
    private int population;
    /**
     * @param name Name of the city.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param population Total number of people who live there.
     */

    public City(String name, double x, double y, int population) {
        super(name, x, y);
        this.population = population;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "City{name='" + name + "', x=" + x + ", y=" + y + ", population=" + population + "}";
    }
}