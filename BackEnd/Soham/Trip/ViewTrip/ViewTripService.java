package BackEnd.Soham.Trip.ViewTrip;

public class ViewTripService {
    private final ViewTripRepository repository = new ViewTripRepository();

    public TripViewDTO getTripDetails(int tripID) {
        return repository.getFullTripData(tripID);
    }
}
