package BackEnd.Soham.Trip.GetTrip.ById;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BackEnd.Soham.DBConnection;
import BackEnd.Soham.Trip.TripEntity;

public class TripByIdRepository {
	public TripEntity findById(int tripId) {
		String query = "SELECT creatorEmail, destination, budget, startDate, endDate FROM trip WHERE tripID = ?";
		DBConnection DBC = new DBConnection();

		try (Connection conn = DBC.DataBaseConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, tripId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String creatorEmail = rs.getString("creatorEmail");
				String destination = rs.getString("destination");
				Integer budget = rs.getObject("budget") == null ? null : rs.getInt("budget");
				String startDate = rs.getString("startDate");
				String endDate = rs.getString("endDate");

				return new TripEntity(creatorEmail, destination, budget, startDate, endDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
