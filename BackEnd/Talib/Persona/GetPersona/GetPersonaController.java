package BackEnd.Talib.Persona.GetPersona;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import BackEnd.Talib.Utils.HttpUtils;

public class GetPersonaController implements HttpHandler {
    private final GetPersonaService service = new GetPersonaService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                String path = exchange.getRequestURI().getPath();
                String[] parts = path.split("/");

                // Expecting /persona/get/{email}
                if (parts.length >= 4) {
                    String email = parts[3];
                    String response = service.getPersona(email);
                    if (response != null) {
                        HttpUtils.sendResponse(exchange, 200, response);
                    } else {
                        HttpUtils.sendResponse(exchange, 404, "{\"error\": \"Persona not found\"}");
                    }
                } else if (parts.length == 3 && path.startsWith("/persona/")) {
                    String email = parts[2];
                    String response = service.getPersona(email);
                    if (response != null) {
                        HttpUtils.sendResponse(exchange, 200, response);
                    } else {
                        HttpUtils.sendResponse(exchange, 404, "{\"error\": \"Persona not found\"}");
                    }
                } else {
                    HttpUtils.sendResponse(exchange, 400, "{\"error\": \"Invalid path\"}");
                }
            } else {
                HttpUtils.sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}