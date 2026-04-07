package BackEnd.Talib.Reviews.CreateReview;

import java.util.*;
public class CreateReviewService {
    private final CreateReviewRepository repository = new CreateReviewRepository();

    public void createReview(String userEmail, Integer placeID, Integer accommodationID, int rating, String comment, String reviewDate) throws Exception {
        repository.createReview(userEmail, placeID, accommodationID, rating, comment, reviewDate);
    }
}
