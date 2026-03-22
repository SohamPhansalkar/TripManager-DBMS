package BackEnd.Soham.User.SignUp;

import java.sql.*;

import BackEnd.Soham.DBConnection;

public class SignUpRepository {

    public boolean signUpuser(String email, String password, String first_name, String last_name, String dob) {
        String query = "INSERT INTO user(email, password, first_name, last_name, dob) VALUES(?, ?, ?, ?, ?)";
        DBConnection DBC = new DBConnection();

        try (Connection conn = DBC.DataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, first_name);
            stmt.setString(4, last_name);
            stmt.setString(5, dob);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            } else {
                System.out.println("User insertion failed: " + email + " (No rows affected)");
                return false; 
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("User with email " + email + " already exists or constraint violation: " + e.getMessage());
            e.printStackTrace();
            return false; 
        } catch (SQLException e) {
            System.err.println("Database error during user signup: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
