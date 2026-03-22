// This is for reference 

package BackEnd.ReferenceCode;

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