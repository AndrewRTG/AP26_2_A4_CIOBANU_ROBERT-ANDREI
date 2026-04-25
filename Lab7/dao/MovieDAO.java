package com.example.demo.dao;

import com.example.demo.entitati.Actor;
import com.example.demo.entitati.Movie;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovieDAO {

    private final DataSource dataSource;

    public MovieDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void create(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, release_date, duration, score, genre_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, movie.getTitle());
            ps.setObject(2, movie.getReleaseDate());
            ps.setObject(3, movie.getDuration());
            ps.setObject(4, movie.getScore());

            if (movie.getGenre() != null && movie.getGenre().getId() != null) {
                ps.setInt(5, movie.getGenre().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.executeUpdate();


            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    movie.setId(rs.getInt(1));
                }
            }

            if (movie.getActors() != null && !movie.getActors().isEmpty()) {
                String actorSql = "INSERT INTO movie_actors (movie_id, actor_id) VALUES (?, ?)";
                try (PreparedStatement psActor = con.prepareStatement(actorSql)) {
                    for (Actor actor : movie.getActors()) {
                        psActor.setInt(1, movie.getId());
                        psActor.setInt(2, actor.getId());
                        psActor.addBatch();
                    }
                    psActor.executeBatch();
                }
            }
        }
    }
    public void delete(int id)throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";
        String sql2 = "DELETE FROM movie_actors WHERE movie_id = ?";
        try (Connection con = dataSource.getConnection();) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(sql2)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }
    public void update(int id, Movie movie)throws SQLException {
        String sql = "UPDATE movies SET title = ?, release_date = ?, duration = ?, score = ?, genre_id = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setObject(2, movie.getReleaseDate());
            ps.setObject(3, movie.getDuration());
            ps.setObject(4, movie.getScore());
            if (movie.getGenre() != null && movie.getGenre().getId() != null) {
                ps.setInt(5, movie.getGenre().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, id);
            ps.executeUpdate();
            if (movie.getActors() != null) {
                try (PreparedStatement psDelete=con.prepareStatement("DELETE FROM movie_actors WHERE movie_id = ?")) {
                    psDelete.setInt(1, id);
                    psDelete.executeUpdate();

                }
                if (!movie.getActors().isEmpty()){
                    String sqlactor= "INSERT INTO movie_actors (movie_id, actor_id) VALUES (?, ?)";
                    try (PreparedStatement psActor = con.prepareStatement(sqlactor)) {
                        for (Actor actor : movie.getActors()) {
                            psActor.setInt(1, movie.getId());
                            psActor.setInt(2, actor.getId());
                            psActor.addBatch();
                        }
                        psActor.executeBatch();
                    }
                }
            }



        }
    }
    public void updateScore(int id, Movie movie)throws SQLException {
        String sql = "UPDATE movies SET score = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, movie.getScore());
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public List<Movie> getAll() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDate("release_date") != null ? rs.getDate("release_date").toLocalDate() : null,
                        rs.getInt("duration"),
                        rs.getDouble("score"),
                        null
                );
                movies.add(movie);
            }
        }

        String actorSql = "SELECT a.id, a.name FROM actors a JOIN movie_actors ma ON a.id = ma.actor_id WHERE ma.movie_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement psActor = con.prepareStatement(actorSql)) {

            for (Movie movie : movies) {
                psActor.setInt(1, movie.getId());
                try (ResultSet rsActor = psActor.executeQuery()) {
                    List<Actor> actors = new ArrayList<>();
                    while (rsActor.next()) {
                        actors.add(new Actor(rsActor.getInt("id"), rsActor.getString("name")));
                    }
                    movie.setActors(actors);
                }
            }
        }

        return movies;
    }
}