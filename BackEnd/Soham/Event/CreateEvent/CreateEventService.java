package BackEnd.Soham.Event.CreateEvent;

public class CreateEventService {
    private final CreateEventRepository repository = new CreateEventRepository();

    public boolean insertEvent(int dayID, int tripID, String time, String type, String description, String link) {
        return repository.insertEvent(dayID, tripID, time, type, description, link);
    }
}

