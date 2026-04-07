package BackEnd.Talib.Places.GetPlaceById;
public class GetPlaceByIdService {
    private final GetPlaceByIdRepository repository = new GetPlaceByIdRepository();

    public GetPlaceByIdRepository.PlaceDetailRecord getPlaceById(int placeID) throws Exception {
        return repository.getPlaceById(placeID);
    }
}

