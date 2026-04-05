package BackEnd.Talib.User.LogIn;

import java.sql.*;

import BackEnd.Talib.DBConnection;
import BackEnd.Talib.User.UserEntity;

public class LogInRepository {

    public UserEntity findByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        DBConnection DBC = new DBConnection();
        
        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UserEntity(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("dob")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}