import java.sql.*;

public class MovieListDAO {

    public void create(MovieList movieList) throws SQLException {
        String sqlList = "INSERT INTO movie_lists (name, creation_timestamp) VALUES (?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement pstmtList = con.prepareStatement(sqlList, Statement.RETURN_GENERATED_KEYS)) {

            pstmtList.setString(1, movieList.getName());
            pstmtList.setObject(2, movieList.getCreationTimestamp());
            pstmtList.executeUpdate();
            try (ResultSet rs = pstmtList.getGeneratedKeys()) {
                if (rs.next()) {
                    movieList.setId(rs.getInt(1));
                }
            }
            if (movieList.getMovies() != null && !movieList.getMovies().isEmpty()) {
                String sqlItems = "INSERT INTO movie_list_items (list_id, movie_id) VALUES (?, ?)";
                try (PreparedStatement pstmtItems = con.prepareStatement(sqlItems)) {
                    for (Movie movie : movieList.getMovies()) {
                        pstmtItems.setInt(1, movieList.getId());
                        pstmtItems.setInt(2, movie.getId());
                        pstmtItems.addBatch();
                    }
                    pstmtItems.executeBatch();
                }
            }

            con.commit();
        } catch (SQLException e) {
            System.err.println("Eroare la salvarea listei de filme: " + e.getMessage());
            throw e;
        }
    }
}