package BackEnd.Talib.Reviews.GetReviewsByPlace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import BackEnd.Talib.DBConnection;

public class GetReviewsByPlaceRepository {

    public String getReviewsByPlace(int placeID) throws Exception {
        String sql = "SELECT rating, comment, reviewDate, likes, dislikes FROM reviews WHERE placeID = ?";
        DBConnection DBC = new DBConnection();
        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, placeID);
            try (ResultSet rs = ps.executeQuery()) {
                StringBuilder json = new StringBuilder("[");
                while (rs.next()) {
                    if (json.length() > 1) json.append(",");
                    json.append("{")
                        .append("\"rating\":").append(rs.getInt("rating")).append(",")
                        .append("\"comment\":\"").append(rs.getString("comment").replace("\"", "\\\"")).append("\",")
                        .append("\"reviewDate\":\"").append(rs.getDate("reviewDate")).append("\",")
                        .append("\"likes\":").append(rs.getInt("likes")).append(",")
                        .append("\"dislikes\":").append(rs.getInt("dislikes"))
                        .append("}");
                }
                json.append("]");
                return json.toString();
            }
        }
    }
}