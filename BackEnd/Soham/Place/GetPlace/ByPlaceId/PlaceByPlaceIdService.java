package BackEnd.Soham.Place.GetPlace.ByPlaceId;

import java.util.*;
import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class PlaceByPlaceIdService {
    private final PlaceByPlaceIdRepository repository = new PlaceByPlaceIdRepository();

    public PlaceByPlaceIdRepository.PlaceRecord findByPlaceId(int placeID) {
        return repository.findByPlaceId(placeID);
    }
}

