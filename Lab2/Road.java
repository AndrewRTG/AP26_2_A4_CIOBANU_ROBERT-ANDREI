import java.util.Objects;


/**
 * Represents a road connection between two locations.
 * Stores information about the type, length, and speed limit.
 * * @author Andrei
 */
public class Road {

    private RoadTypes type;
    private float length;
    private int speedLimit;
    private Location start;
    private Location finish;
    /**
     * Constructs a road and links two locations.
     * * @param type The {@link RoadTypes} of the road.
     * @param length The length of the road (must be >= Euclidean distance).
     * @param speedLimit Maximum speed allowed.
     * @param start Starting {@link Location}.
     * @param finish Ending {@link Location}.
     */

    public Road(RoadTypes type, float length, int speedLimit, Location start, Location finish) {
        this.type = type;
        this.length = length;
        this.speedLimit = speedLimit;
        this.start = start;
        this.finish = finish;
    }



    public RoadTypes getType() {
        return type;
    }

    public void setType(RoadTypes type) {
        this.type = type;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getFinish() {
        return finish;
    }

    public void setFinish(Location finish) {
        this.finish = finish;
    }

    @Override
    public String toString() {
        return "Road{" +
                "type=" + type +
                ", length=" + length +
                ", speedLimit=" + speedLimit +
                ", start=" + start +
                ", finish=" + finish +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return Float.compare(getLength(), road.getLength()) == 0 && getSpeedLimit() == road.getSpeedLimit() && getType() == road.getType() && Objects.equals(getStart(), road.getStart()) && Objects.equals(getFinish(), road.getFinish());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getLength(), getSpeedLimit(), getStart(), getFinish());
    }
}
