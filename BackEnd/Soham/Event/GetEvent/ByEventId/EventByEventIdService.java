package BackEnd.Soham.Event.GetEvent.ByEventId;

public class EventByEventIdService {
    private final EventByEventIdRepository repository = new EventByEventIdRepository();

    public EventByEventIdRepository.EventRecord findByEventId(int eventID) {
        return repository.findByEventId(eventID);
    }
}

