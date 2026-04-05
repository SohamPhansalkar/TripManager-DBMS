package BackEnd.Soham.Trip.GetTrip.ByEmail;

import java.util.List;

public class TripByEmailService {
	private final TripByEmailReository repository = new TripByEmailReository();

	public List<Integer> getTripIdsByEmail(String creatorEmail) {
		return repository.findTripIdsByEmail(creatorEmail);
	}
}
