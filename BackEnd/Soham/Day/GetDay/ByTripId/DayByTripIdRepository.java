package BackEnd.Soham.Day.GetDay.ByTripId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BackEnd.Soham.DBConnection;

public class DayByTripIdRepository {
	public static class DayRecord {
		public final int dayID;
		public final String date;

		public DayRecord(int dayID, String date) {
			this.dayID = dayID;
			this.date = date;
		}
	}

	public List<DayRecord> findByTripId(int tripID) {
		List<DayRecord> days = new ArrayList<>();
		String query = "SELECT * FROM day WHERE tripID = ? ORDER BY date DESC";
		DBConnection DBC = new DBConnection();

		try (Connection conn = DBC.DataBaseConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, tripID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int dayID = rs.getInt("dayID");
				String date = rs.getString("date");
				days.add(new DayRecord(dayID, date));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return days;
	}
}
