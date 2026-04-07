package BackEnd.Talib.Reviews.DeleteReview;
public class DeleteReviewService {
    private final DeleteReviewRepository repository = new DeleteReviewRepository();

    public void validate() {
        repository.validate();
    }
    public void executeDelete(String userEmail, int placeID) throws Exception {
        repository.executeDelete(userEmail, placeID);
    }
}
