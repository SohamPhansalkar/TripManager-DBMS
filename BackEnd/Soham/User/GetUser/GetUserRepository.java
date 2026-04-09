package BackEnd.Soham.User.GetUser;

import BackEnd.Soham.DBConnection;
import BackEnd.Soham.User.UserEntity;
import java.sql.*;

public class GetUserRepository {
    public UserEntity getUserByEmail(String userEmail) {
        String query = "SELECT email, password, first_name, last_name, dob FROM user WHERE email = ?";
        DBConnection dbConn = new DBConnection();
        try (Connection conn = dbConn.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, userEmail);
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
