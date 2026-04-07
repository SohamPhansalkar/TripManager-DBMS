package BackEnd.Soham.Place.CreatePlace;

public class CreatePlaceService {
    private final CreatePlaceRepository repository = new CreatePlaceRepository();

    public boolean insertPlace(int eventID, String name, String placetype, String seasonalAvailability, String street, String city, String country) {
        return repository.insertPlace(eventID, name, placetype, seasonalAvailability, street, city, country);
    }
}

