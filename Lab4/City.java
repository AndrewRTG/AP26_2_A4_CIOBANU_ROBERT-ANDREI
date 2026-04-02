import java.util.*;
public class City {
    private String name;
    private Set<Intersection> intersections;
    private List<Street> streets;
    private Map<Intersection, List<Street>> cityMap;

    public City(String name) {
        this.name = name;
        this.intersections = new HashSet<>();
        this.streets = new ArrayList<>();
        this.cityMap = new HashMap<>();
    }
    public void addIntersection(Intersection v) {
        if (intersections.add(v)) {
            cityMap.put(v, new ArrayList<>());
        }
    }
    public void addStreet(Street s) {
        streets.add(s); // s uneste 2 intersectii le luam pe fiecare cu getIntersection1/2 si adaugam strada s in mapa pentru ambele intersectii
        cityMap.get(s.getIntersection1()).add(s);
        cityMap.get(s.getIntersection2()).add(s);
    }
    public String getName() {
        return name;
    }
    public Set<Intersection> getIntersections() {
        return intersections;
    }
    public List<Street> getStreets() {
        return streets;
    }
    public Map<Intersection, List<Street>> getCityMap() {
        return cityMap;
    }
    @Override
    public String toString() {
        return "City{name='" + name + "', intersections=" + intersections.size() +
                ", streets=" + streets.size() + "}";
    }
}
