import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm.SpanningTree;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.WeightedMultigraph;
import java.util.*;
public class KBestSolutionsFinder {
    private City city;
    private Graph<Intersection, DefaultWeightedEdge> graph;
    private Map<DefaultWeightedEdge, Street> edgeToStreet;
    public KBestSolutionsFinder(City city) {
        this.city = city;
        //this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.edgeToStreet = new HashMap<>();
        buildGraph();
    }
    private void buildGraph() {
        for (Intersection node : city.getIntersections()) {
            graph.addVertex(node);
        }

        for (Street street : city.getStreets()) {
            // pentru fiecare strada adaugam o muchie in graf care uneste 2 noduri(intersectiile)
            DefaultWeightedEdge edge = graph.addEdge(street.getIntersection1(), street.getIntersection2());
            if (edge != null) {
                //daca muchia exista atunci setam costul ei ca fiind lungimea strazii
                graph.setEdgeWeight(edge, street.getLength());
                edgeToStreet.put(edge, street);
            }
        }
    }
    public void printKBestSolutions(int k) {
        // O lista pentru a salva toate solutiile găsite (perechi de tipul <Cost, Lista_de_Străzi>)
        List<Solution> possibleSolutions = new ArrayList<>();
        KruskalMinimumSpanningTree<Intersection, DefaultWeightedEdge> baseKruskal = new KruskalMinimumSpanningTree<>(graph);
        SpanningTree<DefaultWeightedEdge> bestMst = baseKruskal.getSpanningTree();
        possibleSolutions.add(new Solution(bestMst.getWeight(), getStreetsFromEdges(bestMst.getEdges())));
        for (DefaultWeightedEdge mstEdge : bestMst.getEdges()) {
            double originalWeight = graph.getEdgeWeight(mstEdge);
            Intersection source = graph.getEdgeSource(mstEdge);
            Intersection target = graph.getEdgeTarget(mstEdge);

            // Ștergem muchia din graf temporar
            graph.removeEdge(mstEdge);

            // Rulăm Kruskal din nou pe graful rămas
            KruskalMinimumSpanningTree<Intersection, DefaultWeightedEdge> alternativeKruskal = new KruskalMinimumSpanningTree<>(graph);
            SpanningTree<DefaultWeightedEdge> alternativeMst = alternativeKruskal.getSpanningTree();

            // Dacă noul arbore are suficiente străzi pentru a conecta tot orașul
            if (alternativeMst.getEdges().size() == city.getIntersections().size() - 1) {
                possibleSolutions.add(new Solution(alternativeMst.getWeight(), getStreetsFromEdges(alternativeMst.getEdges())));
            }

            // Punem muchia înapoi în graf pentru următoarea iteratie
            DefaultWeightedEdge restoredEdge = graph.addEdge(source, target);
            graph.setEdgeWeight(restoredEdge, originalWeight);
            edgeToStreet.put(restoredEdge, edgeToStreet.get(mstEdge));
        }

        possibleSolutions.sort(Comparator.comparing(Solution::getCost));


        System.out.println("\n=== TOP " + k + " CELE MAI BUNE SOLUTII DE COST MINIM ===");
        int limit = Math.min(k, possibleSolutions.size());

        for (int i = 0; i < limit; i++) {
            Solution sol = possibleSolutions.get(i);
            System.out.println("\nSoluția " + (i + 1) + " - Cost total: " + sol.getCost());
            System.out.println("Străzile selectate: " + sol.getStreets());
        }
    }


    private List<Street> getStreetsFromEdges(Set<DefaultWeightedEdge> edges) {
        List<Street> result = new ArrayList<>();
        for (DefaultWeightedEdge edge : edges) {
            result.add(edgeToStreet.get(edge));
        }
        return result;
    }

    private static class Solution {
        private double cost;
        private List<Street> streets;

        public Solution(double cost, List<Street> streets) {
            this.cost = cost;
            this.streets = streets;
        }

        public double getCost() { return cost; }
        public List<Street> getStreets() { return streets; }
    }
}


