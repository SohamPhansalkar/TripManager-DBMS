package BackEnd.Talib.Persona.GetPersona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import BackEnd.Talib.DBConnection;

public class GetPersonaRepository {
    public static class PersonaRecord {
        public final String type;
        public final String dietary;
        public final String bucketList;
        public final String activities;
        public final String riskLevel;

        public PersonaRecord(String type, String dietary, String bucketList, String activities, String riskLevel) {
            this.type = type; this.dietary = dietary; this.bucketList = bucketList;
            this.activities = activities; this.riskLevel = riskLevel;
        }
    }

    public PersonaRecord getPersona(String email) throws Exception {
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
                    return new PersonaRecord(
                        rs.getString("personaType"),
                        rs.getString("dietaryPreference"),
                        rs.getString("bucketList"),
                        rs.getString("activities"),
                        rs.getString("riskLevel")
                    );
                }
            }
        }
        return null;
    }
}
