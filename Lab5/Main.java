import java.util.*;
public class Main {
    public static void main(String[] args) {

        ResourceRepository repository = new ResourceRepository("My Repository");

        Resource rickRoll = new Resource(
                "1", "Rick Astley - Never Gonna Give You Up", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "1987", "Rick Astley",
                Concept.MUSIC, Concept.GENERAL
        );

        Resource cinema = new Resource(
                "id1", "Cinema", "C:\\Users\\ehgdh\\Downloads\\973466b143485e156747561a0f7744ae.jpg", "2024", "Eu",
                Concept.GENERAL
        );

        Resource jvm = new Resource(
                "jvm25", "The Java Virtual Machine Specification", "https://docs.oracle.com/javase/specs/jvms/se25/html/index.html", "2025", "Tim Lindholm & others",
                Concept.OOP, Concept.ALGORITHM_DESIGN
        );

        Resource sadus = new Resource(
                "2", "Epic shorty", "https://www.youtube.com/watch?v=louTXOyvKLI", "2021", "Epic Shorty",
                Concept.MUSIC
        );

        Resource Leon = new Resource(
                "3", "Where Is Everyone?", "https://www.youtube.com/watch?v=bFc27V7BM7o", "2023", "Kennedy",
                Concept.GENERAL
        );
        try {

            new Concept.AddCommand(repository, rickRoll).execute();
            new Concept.AddCommand(repository, cinema).execute();
            new Concept.AddCommand(repository, jvm).execute();
            new Concept.AddCommand(repository, sadus).execute();
            new Concept.AddCommand(repository, Leon).execute();

            System.out.println("\n-----------------------------------\n");


            new ListCommand(repository).execute();
            new ViewCommand(repository.findResource("id1")).execute();

        }catch (InvalidResourceException e) {
            System.err.println("❌ Exceptie custom (Resursă Invalidă): " + e.getMessage());

        } catch (Exception e) {
            System.err.println("A apărut o eroare la execuția unei comenzi: " + e.getMessage());
        }
        try {
            new ReportCommand(repository).execute();

        } catch (Exception e) {
            System.err.println("Eroare la generarea raportului: " + e.getMessage());
        }
        try {

            List<Resource> optimalCover = CoverAlgorithm.findGreedyCover(repository.getResources());
            System.out.println("✅ Pentru a acoperi TOATE conceptele, ai nevoie de " + optimalCover.size() + " resurse:");
            for (Resource res : optimalCover) {
                System.out.println(" -> " + res.getTitle() + " (acoperă: " + res.getConcepts() + ")");
            }

        } catch (InvalidResourceException e) {

            System.err.println("❌ Eroare Algoritm: " + e.getMessage());
        }
    }
}