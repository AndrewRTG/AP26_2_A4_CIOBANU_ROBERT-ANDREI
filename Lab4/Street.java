public class Street implements Comparable<Street> {
    private String name;
    private int length;
    private Intersection intersection1;
    private Intersection intersection2;

    public Street(String name, int length, Intersection i1, Intersection i2) {
        this.name = name;
        this.length = length;
        this.intersection1 = i1;
        this.intersection2 = i2;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public Intersection getIntersection1() {
        return intersection1;
    }

    public Intersection getIntersection2() {
        return intersection2;
    }

    @Override
    public int compareTo(Street other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name + "(len=" + length + ")";
    }
}