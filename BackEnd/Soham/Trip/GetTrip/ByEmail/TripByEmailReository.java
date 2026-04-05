package BackEnd.Soham.Trip.GetTrip.ByEmail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BackEnd.Soham.DBConnection;

public class TripByEmailReository {
	public List<Integer> findTripIdsByEmail(String creatorEmail) {
		List<Integer> ids = new ArrayList<>();
		String query = "SELECT tripID FROM trip WHERE creatorEmail = ?";
		DBConnection DBC = new DBConnection();

		try (Connection conn = DBC.DataBaseConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, creatorEmail);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt("tripID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ids;
	}
}
