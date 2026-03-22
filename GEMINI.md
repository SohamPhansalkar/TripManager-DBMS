# Project Files

## Notes.txt
```
```

## README.md
```markdown
# TripManager-DBMS
```

## BackEnd/EgJDBC.java
```java
package BackEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EgJDBC {
    private static final String URL = "jdbc:mysql://localhost:3306/DBMSProject";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
```

## BackEnd/EgServer.java
```java
// This is for reference 

package BackEnd;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class EgServer {
    public static void main(String[] args) throws IOException {
        // 1. Create a server instance on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 2. Define the /hello endpoint
        server.createContext("/hello", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "Hello World";
                
                // Set headers and status code (200 OK)
                exchange.sendResponseHeaders(200, response.length());
                
                // Write the body
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        // 3. Start the server
        System.out.println("Server started on http://localhost:8080/hello");
        server.start();
    }
}
```

## BackEnd/Soham/ToDo.txt
```
User ,trip, dayplan,place, accomodation ,transport.

```

## BackEnd/Soham/User/Main.java
```java
```

## BackEnd/Talib/user.txt
```
Talib's code 

make changes only in this folder to avoid merge problems
```

## FrontEnd/Shubham/user.txt
```
Shubham's code 

make changes only in this folder to avoid merge problems
```

## FrontEnd/Tanmay/user.txt
```
Tanmay's code 

make changes only in this folder to avoid merge problems
```
