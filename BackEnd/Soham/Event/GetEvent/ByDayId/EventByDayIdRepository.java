package BackEnd.Soham.Event.GetEvent.ByDayId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BackEnd.Soham.DBConnection;

public class EventByDayIdRepository {
	public static class EventRecord {
		public final int eventID;
		public final int dayID;
		public final int tripID;
		public final String time;
		public final String type;
		public final String description;
		public final String link;

		public EventRecord(int eventID, int dayID, int tripID, String time, String type, String description, String link) {
			this.eventID = eventID;
			this.dayID = dayID;
			this.tripID = tripID;
			this.time = time;
			this.type = type;
			this.description = description;
			this.link = link;
		}
	}

	public List<EventRecord> findByDayId(int dayID) {
		List<EventRecord> events = new ArrayList<>();
		String query = "SELECT * FROM event WHERE dayID = ? ORDER BY time DESC";
		DBConnection DBC = new DBConnection();

		try (Connection conn = DBC.DataBaseConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, dayID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int eventID = rs.getInt("eventID");
				int tripID = rs.getInt("tripID");
				String time = rs.getString("time");
				String type = rs.getString("type");
				String description = rs.getString("description");
				String link = rs.getString("link");
				events.add(new EventRecord(eventID, dayID, tripID, time, type, description, link));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return events;
	}
}
