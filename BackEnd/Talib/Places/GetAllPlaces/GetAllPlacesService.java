package BackEnd.Talib.Places.GetAllPlaces;

public class GetAllPlacesService {
    private final GetAllPlacesRepository repository = new GetAllPlacesRepository();
    public String getAllPlaces() throws Exception { return repository.execute(); }
}
