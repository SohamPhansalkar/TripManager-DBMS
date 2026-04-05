package BackEnd.Soham.Trip.CreateTrip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class CreateTripRepository {
	public boolean saveTrip(TripEntity trip) {
		String insert = "INSERT INTO trip (creatorEmail, destination, budget, startDate, endDate) VALUES (?, ?, ?, ?, ?)";
		DBConnection DBC = new DBConnection();

		try (Connection conn = DBC.DataBaseConnection();
			 PreparedStatement stmt = conn.prepareStatement(insert)) {

			stmt.setString(1, trip.getCreatorEmail());
			stmt.setString(2, trip.getDestination());
			if (trip.getBudget() == null) {
				stmt.setNull(3, java.sql.Types.INTEGER);
			} else {
				stmt.setInt(3, trip.getBudget());
			}
			stmt.setString(4, trip.getStartDate());
			stmt.setString(5, trip.getEndDate());

			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
