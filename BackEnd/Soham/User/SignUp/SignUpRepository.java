package BackEnd.Soham.User.SignUp;

import java.sql.*;

import BackEnd.Soham.DBConnection;

public class SignUpRepository {

    public boolean signUpuser(String email, String password, String first_name, String last_name, String dob) {
        String query = "{? = CALL SignUpFunction(?, ?, ?, ?, ?)}";
        DBConnection DBC = new DBConnection();

        try (Connection conn = DBC.DataBaseConnection();
             CallableStatement stmt = conn.prepareCall(query)) {

            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, first_name);
            stmt.setString(5, last_name); 
            stmt.setString(6, dob);

            stmt.execute();
            return stmt.getBoolean(1);

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
