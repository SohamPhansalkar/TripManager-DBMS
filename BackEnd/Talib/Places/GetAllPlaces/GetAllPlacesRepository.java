package BackEnd.Talib.Places.GetAllPlaces;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import BackEnd.Talib.DBConnection;

public class GetAllPlacesRepository {
    public static class PlaceRecord {
        public final int placeID;
        public final String popularityScore;
        public final String priceRange;

        public PlaceRecord(int placeID, String popularityScore, String priceRange) {
            this.placeID = placeID;
            this.popularityScore = popularityScore;
            this.priceRange = priceRange;
        }
    }

    public List<PlaceRecord> execute() throws Exception {
        String query = "SELECT p.placeID, ts.popularityScore, fs.priceRange FROM place p LEFT JOIN touristSpot ts ON p.placeID = ts.placeID LEFT JOIN foodSpot fs ON p.placeID = fs.placeID";
        DBConnection DBC = new DBConnection();
        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            List<PlaceRecord> places = new ArrayList<>();
            while (rs.next()) {
                String popScore = rs.getObject("popularityScore") == null ? null : String.valueOf(rs.getObject("popularityScore"));
                places.add(new PlaceRecord(rs.getInt("placeID"), popScore, rs.getString("priceRange")));
            }
            return places;
        }
    }
}
