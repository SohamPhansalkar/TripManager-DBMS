package BackEnd.Soham.Trip.CreateTrip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class CreateTripRepository {
	public int saveTrip(TripEntity trip) {
		String insert = "INSERT INTO trip (creatorEmail, destination, budget, startDate, endDate) VALUES (?, ?, ?, ?, ?)";
		DBConnection DBC = new DBConnection();

		try (Connection conn = DBC.DataBaseConnection();
			 PreparedStatement stmt = conn.prepareStatement(insert, java.sql.Statement.RETURN_GENERATED_KEYS)) {

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
			if (rows > 0) {
				try (java.sql.ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						return generatedKeys.getInt(1);
					}
				}
			}
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
