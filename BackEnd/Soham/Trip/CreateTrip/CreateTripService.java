package BackEnd.Soham.Trip.CreateTrip;

import BackEnd.Soham.Trip.TripEntity;

public class CreateTripService {
	private final CreateTripRepository repository = new CreateTripRepository();

	public int createTrip(TripEntity trip) {
		System.out.println("Creating trip for: " + trip.getCreatorEmail());
		return repository.saveTrip(trip);
	}
}
