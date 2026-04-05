package BackEnd.Talib.Reviews.CreateReview;

public class CreateReviewService {
    private final CreateReviewRepository repository = new CreateReviewRepository();

    public void createReview(String email, Integer placeID, Integer accID, int rating, String comment, String reviewDate) throws Exception {
        repository.createReview(email, placeID, accID, rating, comment, reviewDate);
    }
}