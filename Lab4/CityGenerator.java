import com.github.javafaker.Faker;
import java.util.*;

public class CityGenerator {
    private static final Faker faker = new Faker();
    private static final Random rand = new Random();


    private static class Point {
        double x, y;
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static City generateMetricCity(String cityName, int numIntersections, int numStreets) {
        City city = new City(cityName);
        Map<Intersection, Point> coordinates = new HashMap<>();
        List<Intersection> nodesList = new ArrayList<>();
        for (int i = 0; i < numIntersections; i++) {
            Intersection inter = new Intersection(faker.address().cityName());
            double x = rand.nextDouble() * 100;
            double y = rand.nextDouble() * 100;

            city.addIntersection(inter);
            coordinates.put(inter, new Point(x, y));
            nodesList.add(inter);
        }

        int createdStreets = 0;
        while (createdStreets < numStreets) {
            Intersection u = nodesList.get(rand.nextInt(numIntersections));
            Intersection v = nodesList.get(rand.nextInt(numIntersections));

            if (!u.equals(v)) {
                Point p1 = coordinates.get(u);
                Point p2 = coordinates.get(v);

                double dist = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
                int length = (int) Math.round(dist);
                if (length == 0) length = 1;
                city.addStreet(new Street(faker.address().streetName(), length, u, v));
                createdStreets++;
            }
        }

        return city;
    }
}