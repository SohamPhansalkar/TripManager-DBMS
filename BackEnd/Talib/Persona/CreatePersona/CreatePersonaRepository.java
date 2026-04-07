package BackEnd.Talib.Persona.CreatePersona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import BackEnd.Talib.DBConnection;

public class CreatePersonaRepository {

    public void createPersona(String email, String type, String dietary, String bucketList, String activities, String riskLevel) throws Exception {
        DBConnection DBC = new DBConnection();
        try (Connection conn = DBC.DataBaseConnection()) {
            String insertMain = "INSERT INTO travel_persona (email, personaType) VALUES (?, ?) ON DUPLICATE KEY UPDATE personaType=VALUES(personaType)";
            try (PreparedStatement psMain = conn.prepareStatement(insertMain)) {
                psMain.setString(1, email);
                psMain.setString(2, type);
                psMain.executeUpdate();
            }
            
            if ("foodie".equalsIgnoreCase(type)) {
                String q = "INSERT INTO foodie (email, dietaryPreference) VALUES (?, ?) ON DUPLICATE KEY UPDATE dietaryPreference=VALUES(dietaryPreference)";
                try (PreparedStatement ps = conn.prepareStatement(q)) { 
                    ps.setString(1, email); ps.setString(2, dietary); ps.executeUpdate(); 
                }
            } else if ("explorer".equalsIgnoreCase(type)) {
                String q = "INSERT INTO explorer (email, bucketList) VALUES (?, ?) ON DUPLICATE KEY UPDATE bucketList=VALUES(bucketList)";
                try (PreparedStatement ps = conn.prepareStatement(q)) { 
                    ps.setString(1, email); ps.setString(2, bucketList); ps.executeUpdate(); 
                }
            } else if ("adventurer".equalsIgnoreCase(type)) {
                String q = "INSERT INTO adventurer (email, activities, riskLevel) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE activities=VALUES(activities), riskLevel=VALUES(riskLevel)";
                try (PreparedStatement ps = conn.prepareStatement(q)) { 
                    ps.setString(1, email); ps.setString(2, activities); ps.setString(3, riskLevel); ps.executeUpdate(); 
                }
            }
        }
    }
}
