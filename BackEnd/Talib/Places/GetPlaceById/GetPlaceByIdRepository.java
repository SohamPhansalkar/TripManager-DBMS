package BackEnd.Talib.Places.GetPlaceById;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import BackEnd.Talib.DBConnection;

public class GetPlaceByIdRepository {

    public String getPlaceById(int placeID) throws Exception {
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
                    StringBuilder json = new StringBuilder("{");
                    json.append("\"placeID\":").append(rs.getInt("placeID")).append(",");
                    json.append("\"name\":\"").append(rs.getString("name")).append("\",");
                    json.append("\"location\":\"").append(rs.getString("location")).append("\",");
                    json.append("\"description\":\"").append(rs.getString("description")).append("\"");

                    String popScore = rs.getString("popularityScore");
                    if (popScore != null) {
                        json.append(",\"popularityScore\":").append(popScore).append(",");
                        json.append("\"recommendedDuration\":\"").append(rs.getString("recommendedDuration")).append("\"");
                    }
                    String priceRange = rs.getString("priceRange");
                    if (priceRange != null) {
                        json.append(",\"priceRange\":\"").append(priceRange).append("\",");
                        json.append("\"mustTryDishes\":\"").append(rs.getString("mustTryDishes")).append("\"");
                    }
                    json.append("}");
                    return json.toString();
                }
            }
        }
        return null;
    }
}