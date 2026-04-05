package BackEnd.Talib.Reviews.CreateReview;

import java.sql.Connection;
import java.sql.PreparedStatement;
import BackEnd.Talib.DBConnection;

public class CreateReviewRepository {

    public void createReview(String email, Integer placeID, Integer accID, int rating, String comment, String reviewDate) throws Exception {
        String sql = "INSERT INTO reviews (userEmail, placeID, accommodationID, rating, comment, reviewDate) VALUES (?, ?, ?, ?, ?, ?)";
        
        DBConnection DBC = new DBConnection();
        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            if (placeID != null) ps.setInt(2, placeID); else ps.setNull(2, java.sql.Types.INTEGER);
            if (accID != null) ps.setInt(3, accID); else ps.setNull(3, java.sql.Types.INTEGER);
            ps.setInt(4, rating);
            ps.setString(5, comment);
            ps.setDate(6, java.sql.Date.valueOf(reviewDate));
            
            ps.executeUpdate();
        }
    }
}