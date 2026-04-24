package BackEnd.Soham.Trip.TripDuration;

import BackEnd.Soham.DBConnection;
import java.sql.*;

public class TripDurationRepository {
    public int getTripDuration(int tripID) {
        String query = "{? = CALL tripDuration(?)}";
        DBConnection dbConn = new DBConnection();
        try (Connection conn = dbConn.DataBaseConnection();
             CallableStatement stmt = conn.prepareCall(query)) {
            
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, tripID);
            
            stmt.execute();
            return stmt.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
