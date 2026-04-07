package BackEnd.Soham.Place.GetPlace.ByPlaceId;

public class PlaceByPlaceIdService {
    private final PlaceByPlaceIdRepository repository = new PlaceByPlaceIdRepository();

    public PlaceByPlaceIdRepository.PlaceRecord findByPlaceId(int placeID) {
        return repository.findByPlaceId(placeID);
    }
}

