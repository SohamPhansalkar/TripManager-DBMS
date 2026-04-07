package BackEnd.Talib.Persona.CreatePersona;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import BackEnd.Talib.Utils.InputValidator;

public class CreatePersonaController implements HttpHandler {
    private final CreatePersonaRepository repository = new CreatePersonaRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String email = params.get("email");
                if(email!=null) InputValidator.validateEmail(email);
                String type = params.get("personaType");
                
                String dietary = null;
                String bucketList = null;
                String activities = null;
                String riskLevel = null;

                if ("foodie".equalsIgnoreCase(type)) {
                    dietary = params.get("dietaryPreference");
                    if (dietary == null || dietary.isEmpty()) {
                        throw new IllegalArgumentException("dietaryPreference is required for foodie type");
                    }
                } else if ("explorer".equalsIgnoreCase(type)) {
                    bucketList = params.get("bucketList");
                    if (bucketList == null || bucketList.isEmpty()) {
                        throw new IllegalArgumentException("bucketList is required for explorer type");
                    }
                } else if ("adventurer".equalsIgnoreCase(type)) {
                    activities = params.get("activities");
                    riskLevel = params.get("riskLevel");
                    if (activities == null || activities.isEmpty() || riskLevel == null || riskLevel.isEmpty()) {
                        throw new IllegalArgumentException("activities and riskLevel are required for adventurer type");
                    }
                } else {
                    throw new IllegalArgumentException("Invalid personaType: must be foodie, explorer, or adventurer");
                }

                if (email == null || type == null) {
                    throw new IllegalArgumentException("email and personaType are required");
                }

                repository.createPersona(email, type, dietary, bucketList, activities, riskLevel);
                sendJsonResponse(exchange, "{\"message\": \"Persona created successfully\"}", 201);
            } catch (IllegalArgumentException e) {
                sendResponse(exchange, "Validation Error: " + e.getMessage(), 400);
            } catch (Exception e) {
                sendResponse(exchange, "Server error: " + e.getMessage(), 500);
            }
        } else {
            sendResponse(exchange, "Method not allowed", 405);
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

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
