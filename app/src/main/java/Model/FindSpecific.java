package Model;

import Controller.DatabaseManager;

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
    Map <Integer, String> parkingSpot = new HashMap<>();
    public Map queryParkingLots(String keyWord){
        try {
            // 獲取databaseManager單例實例
            DatabaseManager databaseManager = DatabaseManager.getInstance();

            ResultSet resultSet = databaseManager.querySpot(keyWord);

            while (resultSet.next()) {

                int parkinglot_id = resultSet.getInt("parkinglot_id");
                String parkinglot_name = resultSet.getString("parkinglot_name");
//                System.out.println("ID: " + parkinglot_id + ", Name: " + parkinglot_name);
                parkingSpot.put(parkinglot_id, parkinglot_name);
            }


            resultSet.close();
            databaseManager.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parkingSpot;
    }
}
