package Model;

import Controller.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//Synchronous
//user 輸入 keyword 後，使用keyword查詢database的table內相符合的路段。ex:使用者輸入大同路後->SELECT ＊, FROM pork_spot, WHERE parkinglot_address LIKE ?;
public class FindSpecific {
    String KeyWord;
    public Map querySpot(String KeyWord){
        Map <Integer, String> parkingSpot = new HashMap<>();
//        String parkingSpot = null; //還懶著想怎麼存，先string 測試先
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
//                System.out.println("ID: " + parkinglot_id + ", Name: " + parkinglot_name);
                parkingSpot.put(parkinglot_id, parkinglot_name);
            }

            // 關閉資源
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    return parkingSpot;
    }
}
