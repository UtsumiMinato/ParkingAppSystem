package Controller;
import java.sql.*;

//每個Class所需的查詢方法都要放在這邊。
//使用singleton pattern建立 DatabaseManager

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    static String databaseName = "parkinglotsdb";
    private static final String URL = "jdbc:postgresql://localhost:5432/" + databaseName;
    private static final String USER = "xmith";
    private static final String PASS = "12345";

    private DatabaseManager() {
        try {
            // PostgreSQL數據庫驅動
            Class.forName("org.postgresql.Driver");

            // 建立數據庫連接，使用提供的用戶名和密碼
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //FindSpecific_Query
    public ResultSet querySpot(String keyWord) throws SQLException{
        String sql = "SELECT * FROM parkinglots WHERE parkinglot_address LIKE ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, "%" + keyWord + "%");
        return pstmt.executeQuery();//回傳的是沒處理的查詢結果
    }
}


