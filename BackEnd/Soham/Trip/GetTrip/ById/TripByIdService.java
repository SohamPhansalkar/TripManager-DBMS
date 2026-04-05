package BackEnd.Soham.Trip.GetTrip.ById;

import BackEnd.Soham.Trip.TripEntity;

public class TripByIdService {
	private final TripByIdRepository repository = new TripByIdRepository();

	public TripEntity getTripById(int tripId) {
		return repository.findById(tripId);
	}
}
