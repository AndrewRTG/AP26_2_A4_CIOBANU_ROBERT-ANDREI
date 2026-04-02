import java.util.*;
import java.util.stream.IntStream;
import com.github.javafaker.Faker;

public class Main {
    public static void main(String[] args) {

        City myCity = new City("Iasi");
        Faker faker = new Faker();
        Intersection[] nodeArray = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> new Intersection(faker.address().cityName()))
                .toArray(Intersection[]::new);

        for (Intersection node : nodeArray) {
            myCity.addIntersection(node);
        }


        System.out.println("Intersecții generate: " + Arrays.toString(nodeArray));

        Random rand = new Random();
        int numberOfStreets = 40;
        for (int i = 0; i < numberOfStreets; i++) {
            int u = rand.nextInt(10);
            int v = rand.nextInt(10);

            if (u != v) {
                myCity.addStreet(new Street(
                        faker.address().streetName(),
                        rand.nextInt(50) + 1,
                        nodeArray[u],
                        nodeArray[v]
                ));
            }
        }
        List<Street> cityStreets = myCity.getStreets();
        Collections.sort(cityStreets, Comparator.comparing(Street::getLength));
        System.out.println("Străzi după sortare (după lungime): " + cityStreets);



        System.out.println("\n--- Rezultatul Query-ului (Stream API) ---");
        int minLength = 10;
        System.out.println("Străzile mai lungi de " + minLength + " care se unesc cu cel puțin alte 3 străzi:");

        myCity.getStreets().stream()
                .filter(street -> street.getLength() > minLength)

                .filter(street -> {   // o strada(muchie) uneste 2 intersectii (noduri)
                    List<Street> streetsAtNode1 = myCity.getCityMap().get(street.getIntersection1()); //lista de strazi care ies din primul nod al strazii initiale
                    List<Street> streetsAtNode2 = myCity.getCityMap().get(street.getIntersection2()); // lista de strazi care ies din al doilea nod al strazii initiale

                    Set<Street> joinedStreets = new HashSet<>(); // facem un set de strazi unice
                    joinedStreets.addAll(streetsAtNode1); // pune toate strazile care ies din noduri inclusiv strada initiala
                    joinedStreets.addAll(streetsAtNode2);

                    int connectedOtherStreets = joinedStreets.size() - 1; // excludem strada initiala

                    return connectedOtherStreets >= 3;
                })
                .forEach(System.out::println); // afiseaza toate strazile care indeplinesc conditiile

        KBestSolutionsFinder finder = new KBestSolutionsFinder(myCity);
        finder.printKBestSolutions(3);
        System.out.println("\n--- ADVANCED: RUTĂ INSPECȚIE CAMERE ---");
        MaintenanceRouteFinder routeFinder = new MaintenanceRouteFinder(myCity);
        routeFinder.solve();
    }

}