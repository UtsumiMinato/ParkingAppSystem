package Model;

import Controller.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Synchronous
//user 輸入 keyword 後，使用keyword查詢database的table內相符合的路段。ex:使用者輸入大同路，SELECT ＊, FROM pork_spot, WHERE;
public class FindSpecific {
    String KeyWord;
    public void querySpot(String KeyWord){
        try {
            // 獲取資料庫連接
            Connection conn = DatabaseConnection.getConnection();

            // 準備 SQL 查詢
            String sql = "SELECT * FROM parkinglots WHERE parkinglot_address LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + KeyWord + "%");

            // 執行查詢
            ResultSet rs = pstmt.executeQuery();

            // 處理結果
            while (rs.next()) {
                int parkinglot_id = rs.getInt("parkinglot_id");
                String parkinglot_name = rs.getString("parkinglot_name");
                // 其他欄位類似
                System.out.println("ID: " + parkinglot_id + ", Name: " + parkinglot_name);
            }

            // 關閉資源
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
