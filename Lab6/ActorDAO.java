import java.sql.*;
public class ActorDAO {
    public void create(String name) throws SQLException{
        try (Connection con = Database.getConnection(); PreparedStatement pstmt= con.prepareStatement("INSERT INTO actors (name) values (?)"))
        {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            con.commit();
        }
    }
    public Integer findByName(String name) throws SQLException{
        try (Connection con = Database.getConnection();  PreparedStatement pstmt= con.prepareStatement("SELECT id FROM actors WHERE name = ?"))
        {
            pstmt.setString(1, name);
            try(ResultSet rs = pstmt.executeQuery())
            {
                if(rs.next())
                {
                    return rs.getInt("id");
                }
                else return null;
            }
        }
    }
    public Actor findById(int id) throws SQLException{
        try (Connection con = Database.getConnection(); PreparedStatement pstmt= con.prepareStatement("SELECT name FROM actors WHERE id = ?"))
        {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery())
            {
                if(rs.next())
                {
                    return new Actor(id,rs.getString("name"));
                }
                else return null;
            }
        }
    }
}
