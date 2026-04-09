package BackEnd.Soham.User.UpdateUser;

import BackEnd.Soham.DBConnection;
import java.sql.*;

public class UpdateUserRepository {
    public boolean updateUser(String email, String password, String firstName, String lastName, String dob) {
        String query = "UPDATE user SET password = ?, first_name = ?, last_name = ?, dob = ? WHERE email = ?";
        DBConnection dbConn = new DBConnection();
        try (Connection conn = dbConn.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, password);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, dob);
            stmt.setString(5, email);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
