package BackEnd.Soham.Place.GetPlace.ByEventId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BackEnd.Soham.DBConnection;

public class PlaceByEventIdRepository {
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

    public List<PlaceRecord> findByEventId(int eventID) {
        List<PlaceRecord> places = new ArrayList<>();
        String query = "SELECT * FROM place WHERE eventID = ?";
        DBConnection DBC = new DBConnection();

        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int placeID = rs.getInt("placeID");
                String name = rs.getString("name");
                String placetype = rs.getString("placetype");
                String seasonalAvailability = rs.getString("seasonalAvailability");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String country = rs.getString("country");
                places.add(new PlaceRecord(placeID, eventID, name, placetype, seasonalAvailability, street, city, country));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return places;
    }
}
