package BackEnd.Talib.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import BackEnd.Talib.DBConnection;

// INHERITANCE: Base abstract class for DB repositories
// POLYMORPHISM: Abstract dbAction defined
// EXCEPTION HANDLING: Try/Catch generic handling
public abstract class BaseRepository {
    
    protected Connection getConnection() throws DatabaseException {
        try {
            DBConnection DBC = new DBConnection();
            return DBC.DataBaseConnection();
        } catch (Exception e) {
            throw new DatabaseException("Failed to get database connection", e);
        }
    }

    public abstract void validate() throws IllegalArgumentException;

    // Custom Exception Handling Class
    public static class DatabaseException extends Exception {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
