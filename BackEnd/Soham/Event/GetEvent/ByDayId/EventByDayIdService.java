package BackEnd.Soham.Event.GetEvent.ByDayId;

import java.util.List;

public class EventByDayIdService {
    private final EventByDayIdRepository repository = new EventByDayIdRepository();

    public List<EventByDayIdRepository.EventRecord> findByDayId(int dayID) {
        return repository.findByDayId(dayID);
    }
}

