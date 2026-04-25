package com.example.demo.dao;

import com.example.demo.entitati.Actor;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;

@Repository
public class ActorDAO {

    private final DataSource dataSource;
    public ActorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void create(String name) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO actors (name) VALUES (?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    public Integer findByName(String name) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id FROM actors WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
                return null;
            }
        }
    }

    public Actor findById(int id) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT name FROM actors WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Actor(id, rs.getString("name"));
                return null;
            }
        }
    }
}