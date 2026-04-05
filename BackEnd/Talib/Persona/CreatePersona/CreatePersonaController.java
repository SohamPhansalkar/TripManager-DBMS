package BackEnd.Talib.Persona.CreatePersona;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import BackEnd.Talib.Utils.HttpUtils;

public class CreatePersonaController implements HttpHandler {
    private final CreatePersonaService service = new CreatePersonaService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if ("POST".equalsIgnoreCase(method)) {
                String body = HttpUtils.getBody(exchange);
                String email = HttpUtils.extractJsonField(body, "email");
                String type = HttpUtils.extractJsonField(body, "personaType");
                
                String dietary = "foodie".equalsIgnoreCase(type) ? HttpUtils.extractJsonField(body, "dietaryPreference") : null;
                String bucketList = "explorer".equalsIgnoreCase(type) ? HttpUtils.extractJsonField(body, "bucketList") : null;
                String activities = "adventurer".equalsIgnoreCase(type) ? HttpUtils.extractJsonField(body, "activities") : null;
                String riskLevel = "adventurer".equalsIgnoreCase(type) ? HttpUtils.extractJsonField(body, "riskLevel") : null;

                service.createPersona(email, type, dietary, bucketList, activities, riskLevel);
                HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Persona created successfully\"}");
            } else {
                HttpUtils.sendResponse(exchange, 405, "{\"error\": \"Method Not Allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}