package BackEnd.Soham.Day.GetDay.ByTripId;

import java.util.*;
import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class DayByTripIdService {
    private final DayByTripIdRepository repository = new DayByTripIdRepository();

    public List<DayByTripIdRepository.DayRecord> findByTripId(int tripID) {
        return repository.findByTripId(tripID);
    }
}

