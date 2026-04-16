import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {
    private static HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/cities");
        config.setUsername("postgres");
        config.setPassword("andrei");
        config.setMaximumPoolSize(32);
        config.setConnectionTimeout(3000);
        ds=new HikariDataSource(config);
    }
    private Database() {}


    public static Connection getConnection() throws SQLException {
       Connection con= ds.getConnection();
       con.setAutoCommit(false);
       return con;

    }

    public static void closeConnection() {
        if (ds != null) {
            ds.close();
        }
    }

    public static void rollback(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Eroare la rollback: " + e.getMessage());
        }
    }
}