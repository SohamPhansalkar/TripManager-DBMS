package BackEnd.Talib.Reviews.DeleteReview;
import java.sql.Connection;
import java.sql.PreparedStatement;
import BackEnd.Talib.Utils.BaseRepository;

public class DeleteReviewRepository extends BaseRepository {
    @Override
    public void validate() {}

    public void executeDelete(String userEmail, int placeID) throws DatabaseException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM reviews WHERE userEmail=? AND placeID=?")) {
            ps.setString(1, userEmail);
            ps.setInt(2, placeID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Error deleting review", e);
        }
    }
}
