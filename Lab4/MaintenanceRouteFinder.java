import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;

public class MaintenanceRouteFinder {
    private City city;

    public MaintenanceRouteFinder(City city) {
        this.city = city;
    }

    public void solve() {
        // 1. Creăm un graf complet în JGraphT unde muchia dintre 2 puncte 
        // este cel mai scurt drum calculat prin oraș
        Graph<Intersection, DefaultWeightedEdge> completeGraph =
                new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for (Intersection inter : city.getIntersections()) {
            completeGraph.addVertex(inter);
        }

        // Folosim Floyd-Warshall pentru a găsi distanțele minime între TOATE intersecțiile(nodurile)
        // Avem nevoie de asta pentru ca algoritmul comis-voiajorului să funcționeze pe un graf rar
        //Imaginează-ți un agent de vânzări (comis-voiajor) care are de vizitat un set de orașe. El vrea să găsească cel mai scurt drum care:
        //Pleacă dintr-un oraș de start.
        //Vizitează fiecare oraș din listă exact o singură dată.
        //Se întoarce în orașul de unde a plecat.
        Graph<Intersection, DefaultWeightedEdge> baseGraph = buildBaseGraph();
        FloydWarshallShortestPaths<Intersection, DefaultWeightedEdge> swp = // swp calculeaza distanta totala de la a la c(a-b-c)
                new FloydWarshallShortestPaths<>(baseGraph);

        List<Intersection> nodes = List.copyOf(city.getIntersections()); //intersection este un set, avem nevoie de indici node[i] deci transformam set ul in lista
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) { // matrice de drumuri minime
                double dist = swp.getPathWeight(nodes.get(i), nodes.get(j));
                // Dacă există drum între ele, adăugăm o muchie directă în graful TSP
                if (dist < Double.POSITIVE_INFINITY) { // daca a si b nu sunt conectate getpathweight iti returneaza infinit nu da eroare
                    DefaultWeightedEdge e = completeGraph.addEdge(nodes.get(i), nodes.get(j));
                    completeGraph.setEdgeWeight(e, dist); //baga muchia e cu capetele i si j si costul dist
                }
            }
        }
        TwoApproxMetricTSP<Intersection, DefaultWeightedEdge> tsp = new TwoApproxMetricTSP<>(); //solutie care nu este mai rea de 2 ori fata de ruta optima.
//Pentru ca acest algoritm sa functioneze, graful trebuie să fie metric. Asta înseamna ca respecta inegalitatea triunghiului: distanta directa
// între A și C
// este intotdeauna mai mică sau egala cu drumul ocolit prin B
        try {
            GraphPath<Intersection, DefaultWeightedEdge> tour = tsp.getTour(completeGraph);

            System.out.println("\n=== RUTA MAȘINII DE MENTENANȚĂ (TSP 2-Approx) ===");
            System.out.println("Lungime totală estimată: " + tour.getWeight());
            System.out.println("Traseu intersecții: " + tour.getVertexList());
        } catch (IllegalArgumentException e) {
            System.out.println("\n[Eroare] Orașul nu este complet conectat! Mașina nu poate ajunge peste tot.");
        }
    }


    private Graph<Intersection, DefaultWeightedEdge> buildBaseGraph() {
        Graph<Intersection, DefaultWeightedEdge> g = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        city.getIntersections().forEach(g::addVertex);
        for (Street s : city.getStreets()) {
            DefaultWeightedEdge e = g.addEdge(s.getIntersection1(), s.getIntersection2());
            if (e != null) g.setEdgeWeight(e, s.getLength());
        }
        return g;
    }
}