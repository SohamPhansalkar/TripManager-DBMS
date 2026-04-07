package BackEnd.Talib.Places.GetPlaceById;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import BackEnd.Talib.DBConnection;

public class GetPlaceByIdRepository {
    public static class PlaceDetailRecord {
        public final int placeID;
        public final String name;
        public final String location;
        public final String description;
        public final String popularityScore;
        public final String recommendedDuration;
        public final String priceRange;
        public final String mustTryDishes;

        public PlaceDetailRecord(int placeID, String name, String location, String description, String popularityScore, String recommendedDuration, String priceRange, String mustTryDishes) {
            this.placeID = placeID; this.name = name; this.location = location; this.description = description;
            this.popularityScore = popularityScore; this.recommendedDuration = recommendedDuration;
            this.priceRange = priceRange; this.mustTryDishes = mustTryDishes;
        }
    }

    public PlaceDetailRecord getPlaceById(int placeID) throws Exception {
        String sql = "SELECT place.*, t.popularityScore, t.recommendedDuration, " +
                     "f.priceRange, f.mustTryDishes " +
                     "FROM place " +
                     "LEFT JOIN touristSpot t ON place.placeID = t.placeID " +
                     "LEFT JOIN foodSpot f ON place.placeID = f.placeID " +
                     "WHERE place.placeID = ?";

        DBConnection DBC = new DBConnection();
        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, placeID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PlaceDetailRecord(
                        rs.getInt("placeID"),
                        rs.getString("name"),
                        rs.getString("city") + ", " + rs.getString("country"),
                        "", 
                        rs.getString("popularityScore"),
                        rs.getString("recommendedDuration"),
                        rs.getString("priceRange"),
                        rs.getString("mustTryDishes")
                    );
                }
            }
        }
        return null;
    }
}
