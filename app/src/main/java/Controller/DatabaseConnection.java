package Controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    static String databaseName = "parkinglotsdb";
    private static final String URL = "jdbc:postgresql://localhost:5432/" + databaseName;
    private static final String USER = "xmith";
    private static final String PASS = "12345";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
