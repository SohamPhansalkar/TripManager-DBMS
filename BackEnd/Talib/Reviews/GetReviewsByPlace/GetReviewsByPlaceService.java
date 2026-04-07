package BackEnd.Talib.Reviews.GetReviewsByPlace;

import java.util.List;
public class GetReviewsByPlaceService {
    private final GetReviewsByPlaceRepository repository = new GetReviewsByPlaceRepository();

    public List<GetReviewsByPlaceRepository.ReviewRecord> getReviewsByPlace(int placeID) throws Exception {
        return repository.getReviewsByPlace(placeID);
    }
}

