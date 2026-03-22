package BackEnd.Soham.User.LogIn;

import java.sql.*;

import BackEnd.Soham.DBConnection;
import BackEnd.Soham.User.UserEntity;

public class LogInRepository {
    // private final String url = "jdbc:mysql://localhost:3306/DBMSProject";
    // private final String user = "root";
    // private final String pass = "root";

    // static {
    //     try {
    //         Class.forName("com.mysql.cj.jdbc.Driver");
    //     } catch (ClassNotFoundException e) {
    //         throw new RuntimeException("MySQL Driver not found! Check your path.", e);
    //     }
    // }

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
