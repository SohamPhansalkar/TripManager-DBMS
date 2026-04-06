package BackEnd.Soham.Day.GetDay.ByDayId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BackEnd.Soham.DBConnection;

public class DayByDayIDRepository {
	public static class DayRecord {
		public final int dayID;
		public final int tripID;
		public final String date;

		public DayRecord(int dayID, int tripID, String date) {
			this.dayID = dayID;
			this.tripID = tripID;
			this.date = date;
		}
	}

	public DayRecord findById(int dayID) {
		String query = "SELECT * FROM day WHERE dayID = ?";
		DBConnection DBC = new DBConnection();

		try (Connection conn = DBC.DataBaseConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, dayID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int tripID = rs.getInt("tripID");
				String date = rs.getString("date");
				return new DayRecord(dayID, tripID, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
