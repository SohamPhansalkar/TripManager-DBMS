package BackEnd.Soham.Day.GetDay.ByDayId;

public class DayByDayIDService {
    private final DayByDayIDRepository repository = new DayByDayIDRepository();

    public DayByDayIDRepository.DayRecord findById(int dayID) {
        return repository.findById(dayID);
    }
}

