package BackEnd.Talib.Places.GetAllPlaces;
import java.sql.*;
import BackEnd.Talib.DBConnection;

public class GetAllPlacesRepository {
    public String execute() throws Exception {
        String query = "SELECT p.*, ts.popularityScore, ts.recommendedDuration, fs.priceRange, fs.mustTryDishes " +
                       "FROM place p LEFT JOIN touristSpot ts ON p.placeID = ts.placeID LEFT JOIN foodSpot fs ON p.placeID = fs.placeID";
        
        DBConnection DBC = new DBConnection();
        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            StringBuilder json = new StringBuilder("[");
            while (rs.next()) {
                if (json.length() > 1) json.append(",");
                json.append("{").append("\"placeID\":").append(rs.getInt("placeID")).append(",")
                    .append("\"popularityScore\":").append(rs.getObject("popularityScore")).append(",")
                    .append("\"priceRange\":\"").append(rs.getString("priceRange")).append("\"").append("}");
            }
            json.append("]");
            return json.toString();
        }
    }
}
