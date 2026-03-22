package BackEnd.ReferenceCode;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryRunner {
    public static void main(String[] args) {
        // The SQL query to be executed
        String sql = "SHOW TABLES;";

        System.out.println("Connecting to database and executing query: " + sql);

        try (Connection conn = EgJDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nTables in database 'DBMSProject':");
            System.out.println("--------------------");

            // Loop through the result set and print table names
            int count = 0;
            while (rs.next()) {
                // The table name is in the first column of the result
                String tableName = rs.getString(1);
                System.out.println(tableName);
                count++;
            }
            System.out.println("--------------------");
            System.out.println("Found " + count + " tables.");

        } catch (SQLException e) {
            System.err.println("An error occurred while executing the query.");
            e.printStackTrace();
        }
    }
}
