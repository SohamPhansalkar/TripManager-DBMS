package BackEnd.Soham.Event.GetNumberOfEvents;

import BackEnd.Soham.DBConnection;
import java.sql.*;

public class TotalEventsRepository {
    public int getTotalEvents(int tripID) {
        String query = "{? = CALL totalEvents(?)}";
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
