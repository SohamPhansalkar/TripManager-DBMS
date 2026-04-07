package BackEnd.Talib.Persona.UpdatePersona;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import BackEnd.Talib.Utils.InputValidator;

public class UpdatePersonaController implements HttpHandler {
    private final UpdatePersonaService service = new UpdatePersonaService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                Map<String, String> params = parseJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                service.executeUpdate(params.get("email"), params.get("personaType"));
                sendJsonResponse(exchange, "{\"message\":\"Persona updated\"}", 200);
            } catch (IllegalArgumentException e) {
                sendResponse(exchange, "Validation Error: " + e.getMessage(), 400);
            } catch (Exception e) {
                sendResponse(exchange, "Server error: " + e.getMessage(), 500);
            }
        }
    }
    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void sendJsonResponse(HttpExchange exchange, String json, int statusCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, json.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
    }

    private Map<String, String> parseJson(String json) {
        Map<String, String> result = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            String[] pairs = json.substring(1, json.length() - 1).split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim().replace("\"", "");
                    result.put(key, value);
                }
            }
        }
        return result;
    }
}
