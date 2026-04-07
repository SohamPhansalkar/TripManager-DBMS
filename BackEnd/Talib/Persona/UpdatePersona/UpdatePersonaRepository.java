package BackEnd.Talib.Persona.UpdatePersona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import BackEnd.Talib.Utils.BaseRepository;

public class UpdatePersonaRepository extends BaseRepository {
    @Override
    public void validate() {}
    public void executeUpdate(String email, String personaType) throws DatabaseException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE travel_persona SET personaType=? WHERE email=?")) {
            ps.setString(1, personaType);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Error updating persona", e);
        }
    }
}
