package BackEnd.Soham.Place.GetPlace.ByPlaceId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BackEnd.Soham.DBConnection;

public class PlaceByPlaceIdRepository {
    public static class PlaceRecord {
        public final int placeID;
        public final int eventID;
        public final String name;
        public final String placetype;
        public final String seasonalAvailability;
        public final String street;
        public final String city;
        public final String country;

        public PlaceRecord(int placeID, int eventID, String name, String placetype, String seasonalAvailability, String street, String city, String country) {
            this.placeID = placeID;
            this.eventID = eventID;
            this.name = name;
            this.placetype = placetype;
            this.seasonalAvailability = seasonalAvailability;
            this.street = street;
            this.city = city;
            this.country = country;
        }
    }

    public PlaceRecord findByPlaceId(int placeID) {
        String query = "SELECT * FROM place WHERE placeID = ?";
        DBConnection DBC = new DBConnection();

        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, placeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int placeID2 = rs.getInt("placeID");
                int eventID = rs.getInt("eventID");
                String name = rs.getString("name");
                String placetype = rs.getString("placetype");
                String seasonalAvailability = rs.getString("seasonalAvailability");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String country = rs.getString("country");
                return new PlaceRecord(placeID2, eventID, name, placetype, seasonalAvailability, street, city, country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
