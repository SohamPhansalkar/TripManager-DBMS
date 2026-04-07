package BackEnd.Talib.Reviews.GetReviewsByPlace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import BackEnd.Talib.DBConnection;

public class GetReviewsByPlaceRepository {
    public static class ReviewRecord {
        public final int rating;
        public final String comment;
        public final String reviewDate;
        public final int likes;
        public final int dislikes;

        public ReviewRecord(int rating, String comment, String reviewDate, int likes, int dislikes) {
            this.rating = rating; this.comment = comment; this.reviewDate = reviewDate;
            this.likes = likes; this.dislikes = dislikes;
        }
    }

    public List<ReviewRecord> getReviewsByPlace(int placeID) throws Exception {
        String sql = "SELECT rating, comment, reviewDate, likes, dislikes FROM reviews WHERE placeID = ?";
        DBConnection DBC = new DBConnection();
        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, placeID);
            try (ResultSet rs = ps.executeQuery()) {
                List<ReviewRecord> reviews = new ArrayList<>();
                while (rs.next()) {
                    reviews.add(new ReviewRecord(
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getString("reviewDate"),
                        rs.getInt("likes"),
                        rs.getInt("dislikes")
                    ));
                }
                return reviews;
            }
        }
    }
}
