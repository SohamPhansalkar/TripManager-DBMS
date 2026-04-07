package BackEnd.Talib.Persona.DeletePersona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import BackEnd.Talib.Utils.BaseRepository;

public class DeletePersonaRepository extends BaseRepository {
    @Override
    public void validate() {}
    public void executeDelete(String email) throws DatabaseException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM travel_persona WHERE email=?")) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Error deleting persona", e);
        }
    }
}
