import java.sql.*;
import java.util.*;
public class MovieDAO {
    public void create(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, release_date, duration, score, genre_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Database.getConnection(); PreparedStatement pstmtMovie = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmtMovie.setString(1, movie.getTitle());
            pstmtMovie.setObject(2, movie.getReleaseDate());
            pstmtMovie.setObject(3, movie.getDuration());
            pstmtMovie.setObject(4, movie.getScore());
            if (movie.getGenre() != null && movie.getGenre().getId() != null) {
            pstmtMovie.setInt(5, movie.getGenre().getId());
            }
            else {
                pstmtMovie.setNull(5, Types.INTEGER);
            }
            pstmtMovie.executeUpdate();
            try (ResultSet rs = pstmtMovie.getGeneratedKeys()) {
                if (rs.next()) {
                    movie.setId(rs.getInt(1));
                }
            }
            if (movie.getActors() != null && !movie.getActors().isEmpty()) {
                String sqlActors = "INSERT INTO movie_actors (movie_id, actor_id) VALUES (?, ?)";
                try (PreparedStatement pstmtActors = con.prepareStatement(sqlActors)) {
                    for (Actor actor : movie.getActors()) {
                        pstmtActors.setInt(1, movie.getId());
                        pstmtActors.setInt(2, actor.getId());
                        pstmtActors.addBatch(); // Adaugam in batch spus la curs
                    }
                    pstmtActors.executeBatch(); // executam batch spus la curs
                }
            }
            con.commit();

        }

        }
    public List<Movie> findAll() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDate("release_date") != null ? rs.getDate("release_date").toLocalDate() : null,
                        rs.getInt("duration"),
                        rs.getDouble("score"),
                        null
                );
                String sqlActors = "SELECT a.id, a.name FROM actors a JOIN movie_actors ma ON a.id = ma.actor_id WHERE ma.movie_id = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sqlActors)) {
                    pstmt.setInt(1, movie.getId());
                    try (ResultSet rsActors = pstmt.executeQuery()) {
                        while (rsActors.next()) {
                            movie.addActor(new Actor(rsActors.getInt("id"), rsActors.getString("name")));
                        }
                    }
                }
                movies.add(movie);
            }
        }
        return movies;
    }

}
