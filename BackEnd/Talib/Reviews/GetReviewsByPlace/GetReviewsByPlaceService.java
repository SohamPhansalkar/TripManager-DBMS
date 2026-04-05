package BackEnd.Talib.Reviews.GetReviewsByPlace;

public class GetReviewsByPlaceService {
    private final GetReviewsByPlaceRepository repository = new GetReviewsByPlaceRepository();

    public String getReviewsByPlace(int placeID) throws Exception {
        return repository.getReviewsByPlace(placeID);
    }
}