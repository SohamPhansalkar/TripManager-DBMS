package BackEnd.ReferenceCode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the connection to the MySQL database.
 * NOTE: This requires the MySQL JDBC driver (Connector/J) to be in the classpath at runtime.
 */
public class EgJDBC {
    private static final String URL = "jdbc:mysql://localhost:3306/DBMSProject";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection getConnection() throws SQLException {
        // Load the MySQL JDBC driver class
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
