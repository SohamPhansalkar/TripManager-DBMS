package BackEnd.Soham.Day.CreateDay;

public class CreateDayService {
    private final CreateDayRepository repository = new CreateDayRepository();

    public int insertDay(int tripID, String date) {
        return repository.insertDay(tripID, date);
    }
}

