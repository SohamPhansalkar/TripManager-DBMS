package BackEnd.Soham.Day.CreateDay;

import java.util.*;
import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class CreateDayService {
    private final CreateDayRepository repository = new CreateDayRepository();

    public boolean insertDay(int tripID, String date) {
        return repository.insertDay(tripID, date);
    }
}

