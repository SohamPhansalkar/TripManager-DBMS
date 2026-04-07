package BackEnd.Talib.User.LogIn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import BackEnd.Talib.DBConnection;

public class LogInRepository {
    public boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        DBConnection DBC = new DBConnection();
        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
