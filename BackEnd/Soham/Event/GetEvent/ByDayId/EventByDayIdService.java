package BackEnd.Soham.Event.GetEvent.ByDayId;

import java.util.*;
import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class EventByDayIdService {
    private final EventByDayIdRepository repository = new EventByDayIdRepository();

    public List<EventByDayIdRepository.EventRecord> findByDayId(int dayID) {
        return repository.findByDayId(dayID);
    }
}

