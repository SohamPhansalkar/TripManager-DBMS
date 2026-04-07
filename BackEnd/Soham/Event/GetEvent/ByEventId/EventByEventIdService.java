package BackEnd.Soham.Event.GetEvent.ByEventId;

import java.util.*;
import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class EventByEventIdService {
    private final EventByEventIdRepository repository = new EventByEventIdRepository();

    public EventByEventIdRepository.EventRecord findByEventId(int eventID) {
        return repository.findByEventId(eventID);
    }
}

