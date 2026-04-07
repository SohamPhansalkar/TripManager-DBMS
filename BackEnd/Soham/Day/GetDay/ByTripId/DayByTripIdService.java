package BackEnd.Soham.Day.GetDay.ByTripId;

import java.util.List;

public class DayByTripIdService {
    private final DayByTripIdRepository repository = new DayByTripIdRepository();

    public List<DayByTripIdRepository.DayRecord> findByTripId(int tripID) {
        return repository.findByTripId(tripID);
    }
}

