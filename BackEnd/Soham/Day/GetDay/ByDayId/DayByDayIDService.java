package BackEnd.Soham.Day.GetDay.ByDayId;

import java.util.*;
import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class DayByDayIDService {
    private final DayByDayIDRepository repository = new DayByDayIDRepository();

    public DayByDayIDRepository.DayRecord findById(int dayID) {
        return repository.findById(dayID);
    }
}

