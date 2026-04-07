package BackEnd.Soham.Place.GetPlace.ByEventId;

import java.util.*;
import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class PlaceByEventIdService {
    private final PlaceByEventIdRepository repository = new PlaceByEventIdRepository();

    public List<PlaceByEventIdRepository.PlaceRecord> findByEventId(int eventID) {
        return repository.findByEventId(eventID);
    }
}

