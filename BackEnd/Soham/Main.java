package BackEnd.Soham;

import BackEnd.Soham.User.LogIn.LogInController;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/login", new LogInController());

        System.out.println("Server started on http://localhost:8080");
        server.start();
    }
}
