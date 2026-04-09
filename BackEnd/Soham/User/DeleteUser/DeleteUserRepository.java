package BackEnd.Soham.User.DeleteUser;

import BackEnd.Soham.DBConnection;
import java.sql.*;

public class DeleteUserRepository {
    public boolean deleteUser(String email) {
        String query = "DELETE FROM user WHERE email = ?";
        DBConnection dbConn = new DBConnection();
        try (Connection conn = dbConn.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
