package BackEnd.Soham;
import java.sql.*;


public class DBConnection {

    public Connection DataBaseConnection() throws SQLException{
        final String url = "jdbc:mysql://localhost:3306/DBMSProject";
        final String user = "root";
        final String pass = "root";

        // static {
        //     try {
        //         Class.forName("com.mysql.cj.jdbc.Driver");
        //     } catch (ClassNotFoundException e) {
        //         throw new RuntimeException("MySQL Driver not found! Check your path.", e);
        //     }
        // }

        return DriverManager.getConnection(url, user, pass);
    }
    
}
