package BackEnd.Soham.Day.CreateDay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import BackEnd.Soham.DBConnection;

public class CreateDayRepository {
    public int insertDay(int tripID, String date) {
        String insert = "INSERT INTO day(tripID, date) VALUES (?, ?)";
        DBConnection DBC = new DBConnection();

        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(insert, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, tripID);
            stmt.setString(2, date);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (java.sql.ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
