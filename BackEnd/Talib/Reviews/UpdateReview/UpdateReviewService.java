package BackEnd.Talib.Reviews.UpdateReview;
public class UpdateReviewService {
    private final UpdateReviewRepository repository = new UpdateReviewRepository();

    public void validate() {
        repository.validate();
    }
    public void executeUpdate(String userEmail, int placeID, int rating, String comment) throws Exception {
        repository.executeUpdate(userEmail, placeID, rating, comment);
    }
}
