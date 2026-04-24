package BackEnd.Soham.Trip.ViewTrip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import BackEnd.Soham.DBConnection;

public class ViewTripRepository {
    public TripViewDTO getFullTripData(int tripID) {
        String query = "SELECT t.tripID, t.creatorEmail, t.destination, t.budget, t.startDate, t.endDate, " +
                       "d.dayID, d.date AS dayDate, " +
                       "e.eventID, e.time, e.type, e.description " +
                       "FROM trip t " +
                       "LEFT JOIN day d ON t.tripID = d.tripID " +
                       "LEFT JOIN event e ON d.dayID = e.dayID " +
                       "WHERE t.tripID = ?";

        DBConnection DBC = new DBConnection();
        TripViewDTO tripView = null;
        Map<Integer, TripViewDTO.DayViewDTO> daysMap = new HashMap<>();

        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, tripID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (tripView == null) {
                        tripView = new TripViewDTO();
                        tripView.setTripID(rs.getInt("tripID"));
                        tripView.setCreatorEmail(rs.getString("creatorEmail"));
                        tripView.setDestination(rs.getString("destination"));
                        tripView.setBudget(rs.getInt("budget"));
                        tripView.setStartDate(rs.getString("startDate"));
                        tripView.setEndDate(rs.getString("endDate"));
                    }

                    int dayID = rs.getInt("dayID");
                    if (dayID > 0) {
                        TripViewDTO.DayViewDTO day = daysMap.get(dayID);
                        if (day == null) {
                            day = new TripViewDTO.DayViewDTO();
                            day.setDayID(dayID);
                            day.setDate(rs.getString("dayDate"));
                            tripView.getDays().add(day);
                            daysMap.put(dayID, day);
                        }

                        int eventID = rs.getInt("eventID");
                        if (eventID > 0) {
                            TripViewDTO.EventViewDTO event = new TripViewDTO.EventViewDTO();
                            event.setEventID(eventID);
                            event.setTime(rs.getString("time"));
                            event.setType(rs.getString("type"));
                            event.setDescription(rs.getString("description"));
                            day.getEvents().add(event);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tripView;
    }
}
