package BackEnd.Talib.Reviews.UpdateReview;
import java.sql.Connection;
import java.sql.PreparedStatement;
import BackEnd.Talib.Utils.BaseRepository;

public class UpdateReviewRepository extends BaseRepository {
    @Override
    public void validate() {}

    public void executeUpdate(String userEmail, int placeID, int rating, String comment) throws DatabaseException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE reviews SET rating=?, comment=? WHERE userEmail=? AND placeID=?")) {
            ps.setInt(1, rating);
            ps.setString(2, comment);
            ps.setString(3, userEmail);
            ps.setInt(4, placeID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Error updating review", e);
        }
    }
}
