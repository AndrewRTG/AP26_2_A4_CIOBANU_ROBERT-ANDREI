import java.util.*;
public class CoverAlgorithm {

    public static List<Resource> findGreedyCover(List<Resource> catalog) throws InvalidResourceException {
        Set<Concept> uncoveredConcepts = new HashSet<>(Arrays.asList(Concept.values())); //toate conceptele prin care trebuie sa trecem
        List<Resource> selectedResources = new ArrayList<>();
        while (!uncoveredConcepts.isEmpty()) {
            Resource bestResource = null; //resursa cu cele mai multe concepte neacoperite
            int maxCovered = 0;
            for (Resource res : catalog) {
                Set<Concept> intersection = new HashSet<>(res.getConcepts());
                intersection.retainAll(uncoveredConcepts); // pune in intersection doar elementele comune dintre intersection si uncovered concepts
                if (intersection.size() > maxCovered) {
                    maxCovered = intersection.size();
                    bestResource = res;
                }
            }
            if (bestResource == null) {
                throw new InvalidResourceException("Catalogul nu are suficiente resurse pentru a acoperi toate conceptele!");
            }
            selectedResources.add(bestResource);
            uncoveredConcepts.removeAll(bestResource.getConcepts());
        }

        return selectedResources;
    }
}