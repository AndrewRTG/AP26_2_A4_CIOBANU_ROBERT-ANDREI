package com.example.demo.dao;

import com.example.demo.entitati.Genre;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;

@Repository
public class GenreDAO {

    private final DataSource dataSource;

    public GenreDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void create(String name) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO genres (name) VALUES (?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    public Integer findByName(String name) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id FROM genres WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
                return null;
            }
        }
    }

    public Genre findById(int id) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT name FROM genres WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Genre(id, rs.getString("name"));
                return null;
            }
        }
    }

    public void deleteByName(String name) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM genres WHERE name = ?")) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }
}