package BackEnd.Soham.Event.CreateEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import BackEnd.Soham.DBConnection;

public class CreateEventRepository {
	public boolean insertEvent(int dayID, int tripID, String time, String type, String description, String link) {
		String insert = "INSERT INTO event(dayID, tripID, time, type, description, link) VALUES (?, ?, ?, ?, ?, ?)";
		DBConnection DBC = new DBConnection();

		try (Connection conn = DBC.DataBaseConnection();
			 PreparedStatement stmt = conn.prepareStatement(insert)) {

			stmt.setInt(1, dayID);
			stmt.setInt(2, tripID);
			stmt.setString(3, time);
			stmt.setString(4, type);
			stmt.setString(5, description);
			if (link == null || link.isEmpty()) {
				stmt.setNull(6, java.sql.Types.VARCHAR);
			} else {
				stmt.setString(6, link);
			}

			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
