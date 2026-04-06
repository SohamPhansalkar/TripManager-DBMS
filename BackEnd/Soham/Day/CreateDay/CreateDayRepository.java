package BackEnd.Soham.Day.CreateDay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import BackEnd.Soham.DBConnection;

public class CreateDayRepository {
    public boolean insertDay(int tripID, String date) {
        String insert = "INSERT INTO day(tripID, date) VALUES (?, ?)";
        DBConnection DBC = new DBConnection();

        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setInt(1, tripID);
            stmt.setString(2, date);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
