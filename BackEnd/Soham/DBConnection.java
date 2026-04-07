package BackEnd.Soham;
import java.sql.*;


public class DBConnection {

    public Connection DataBaseConnection() throws SQLException{
        final String url = "jdbc:mysql://localhost:3306/DBMSProject";
        final String user = "root";
        final String pass = "root";

        return DriverManager.getConnection(url, user, pass);
    }
    
}
