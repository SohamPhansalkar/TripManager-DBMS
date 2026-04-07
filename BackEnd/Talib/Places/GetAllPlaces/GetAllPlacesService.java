package BackEnd.Talib.Places.GetAllPlaces;

import java.util.List;
public class GetAllPlacesService {
    private final GetAllPlacesRepository repository = new GetAllPlacesRepository();

    public List<GetAllPlacesRepository.PlaceRecord> execute() throws Exception {
        return repository.execute();
    }
}

