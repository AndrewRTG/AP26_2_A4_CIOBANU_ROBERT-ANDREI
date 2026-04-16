import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataImporter {

    private final GenreDAO genreDAO;
    private final ActorDAO actorDAO;
    private final MovieDAO movieDAO;

    public DataImporter() {
        this.genreDAO = new GenreDAO();
        this.actorDAO = new ActorDAO();
        this.movieDAO = new MovieDAO();
    }

    public void importData(String csvFilePath) {
        System.out.println("Începem importul datelor din: " + csvFilePath);
        int successCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                try {
                    String[] columns = line.split(",");
                    if (columns.length < 6) {
                        System.err.println("Linie invalidă (prea puține coloane): " + line);
                        continue;
                    }
                    String title = columns[0].trim();
                    LocalDate releaseDate = parseDate(columns[1].trim());
                    Integer duration = Integer.parseInt(columns[2].trim());
                    Double score = Double.parseDouble(columns[3].trim());
                    String genreName = columns[4].trim();
                    String[] actorNames = columns[5].split("\\|");
                    Genre genre = getOrCreateGenre(genreName);
                    Movie movie = new Movie(null, title, releaseDate, duration, score, genre);
                    for (String actorName : actorNames) {
                        Actor actor = getOrCreateActor(actorName.trim());
                        movie.addActor(actor);
                    }

                    movieDAO.create(movie);
                    successCount++;

                } catch (Exception e) {
                    System.err.println("Eroare la procesarea liniei: " + line + " - " + e.getMessage());
                }
            }

            System.out.println("Import finalizat! Filme adăugate cu succes: " + successCount);

        } catch (IOException e) {
            System.err.println("Eroare fatală la citirea fișierului CSV: " + e.getMessage());
        }
    }

    private Genre getOrCreateGenre(String name) throws SQLException {
        Integer id = genreDAO.findByName(name);
        if (id == null) {
            genreDAO.create(name);
            id = genreDAO.findByName(name);
        }
        return new Genre(id, name);
    }

    private Actor getOrCreateActor(String name) throws SQLException {
        Integer id = actorDAO.findByName(name);
        if (id == null) {
            actorDAO.create(name);
            id = actorDAO.findByName(name);
        }
        return new Actor(id, name);
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}