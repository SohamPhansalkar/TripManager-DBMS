package BackEnd.Talib.Persona.GetPersona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import BackEnd.Talib.DBConnection;

public class GetPersonaRepository {

    public String getPersona(String email) throws Exception {
        String sql = "SELECT tp.personaType, f.dietaryPreference, e.bucketList, a.activities, a.riskLevel " +
                     "FROM travel_persona tp " +
                     "LEFT JOIN foodie f ON tp.email = f.email " +
                     "LEFT JOIN explorer e ON tp.email = e.email " +
                     "LEFT JOIN adventurer a ON tp.email = a.email " +
                     "WHERE tp.email = ?";
        
        DBConnection DBC = new DBConnection();
        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "{" +
                        "\"personaType\":\"" + rs.getString("personaType") + "\"," +
                        "\"dietaryPreference\":\"" + rs.getString("dietaryPreference") + "\"," +
                        "\"bucketList\":\"" + rs.getString("bucketList") + "\"," +
                        "\"activities\":\"" + rs.getString("activities") + "\"," +
                        "\"riskLevel\":\"" + rs.getString("riskLevel") + "\"" +
                        "}";
                }
            }
        }
        return null;
    }
}