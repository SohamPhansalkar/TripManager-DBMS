package BackEnd.Soham.Place.GetPlace.ByEventId;

import java.util.List;

public class PlaceByEventIdService {
    private final PlaceByEventIdRepository repository = new PlaceByEventIdRepository();

    public List<PlaceByEventIdRepository.PlaceRecord> findByEventId(int eventID) {
        return repository.findByEventId(eventID);
    }
}

