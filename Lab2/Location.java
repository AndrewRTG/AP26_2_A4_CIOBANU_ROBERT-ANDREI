import java.util.Objects;
/**
 * Abstract sealed class representing a generic location on a map.
 * This class serves as a base for specific location types like City or Airport.
 * * @author Andrei
 */
public abstract sealed class Location permits Airport, GasStation, City {
    protected String name;
    protected double x;
    protected double y;
    /** ID used for indexing in the DFS visitor array. */
    private int id;
    /**
     * @param name The display name of the location.
     * @param x The X coordinate in the system.
     * @param y The Y coordinate in the system.
     */

    public Location(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(getX(), location.getX()) == 0 && Double.compare(getY(), location.getY()) == 0 && Objects.equals(getName(), location.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getX(), getY());
    }
}
