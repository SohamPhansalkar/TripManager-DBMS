package BackEnd.Soham.Place.CreatePlace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import BackEnd.Soham.DBConnection;

public class CreatePlaceRepository {
    public boolean insertPlace(int eventID, String name, String placetype, String seasonalAvailability, String street, String city, String country) {
        String insert = "INSERT INTO place(eventID, name, placetype, seasonalAvailability, street, city, country) VALUES (?, ?, ?, ?, ?, ?, ?)";
        DBConnection DBC = new DBConnection();

        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setInt(1, eventID);
            stmt.setString(2, name);
            stmt.setString(3, placetype);
            stmt.setString(4, seasonalAvailability);
            stmt.setString(5, street);
            stmt.setString(6, city);
            stmt.setString(7, country);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
