import org.flywaydb.core.Flyway;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("--- Inițializare Bază de Date cu Flyway ---");
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/cities", "postgres", "andrei")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        System.out.println("Migrări Flyway executate cu succes!\n");

        try {
            System.out.println("--- Începem testarea aplicației ---");

            //Importul datelor din CSV dezactivat ca sa nu supraincarc baza de date pentru a merge partitionarea
           // DataImporter importer = new DataImporter();
            //importer.importData("movies_dataset.csv");

            System.out.println("\nExtragem filmele pentru partiționare");
            MovieDAO movieDAO = new MovieDAO();
            MovieListDAO movieListDAO = new MovieListDAO();
            List<Movie> allMovies = movieDAO.findAll();

            // facem cu  Choco Solver pentru partitionare asa cum scrie pe slide uri
            System.out.println("Rulăm algoritmul de partiționare (Graph Coloring) pe " + allMovies.size() + " filme...");
            MoviePartitioner partitioner = new MoviePartitioner();
            List<List<Movie>> partitionedLists = partitioner.partitionMovies(allMovies);


            System.out.println("\n--- Rezultatele partiționării ---");
            int listCounter = 1;
            for (List<Movie> list : partitionedLists) {

                MovieList newList = new MovieList(null, "Lista Echilibrată " + listCounter, LocalDateTime.now());
                newList.setMovies(list);

                movieListDAO.create(newList); // O salvăm în DB folosind noul DAO

                System.out.println("S-a salvat " + newList.getName() + " conținând:");
                for (Movie m : list) {
                    System.out.println("   - " + m.getTitle() + " (Actori: " + m.getActors().size() + ")");
                }
                listCounter++;
            }


            System.out.println("\nGenerăm raportul HTML bazat pe View-ul din baza de date...");
            ReportGenerator reportGenerator = new ReportGenerator();
            reportGenerator.generateHtmlReport("template.html", "raport_filme.html");

        } catch (Exception e) {
            System.err.println("A apărut o eroare: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection();
            System.out.println("\nPool-ul de conexiuni HikariCP a fost închis.");
        }
    }
}