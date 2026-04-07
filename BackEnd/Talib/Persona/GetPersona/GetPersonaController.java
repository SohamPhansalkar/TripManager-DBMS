package BackEnd.Talib.Persona.GetPersona;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import BackEnd.Talib.Persona.GetPersona.GetPersonaRepository.PersonaRecord;

public class GetPersonaController implements HttpHandler {
    private final GetPersonaRepository repository = new GetPersonaRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String email = params.get("email");
                if (email == null) {
                    sendResponse(exchange, "Missing email", 400); return;
                }

                PersonaRecord persona = repository.getPersona(email);
                if (persona != null) {
                    String json = "{" +
                        "\"personaType\":\"" + escape(persona.type) + "\"," +
                        "\"dietaryPreference\":\"" + escape(persona.dietary) + "\"," +
                        "\"bucketList\":\"" + escape(persona.bucketList) + "\"," +
                        "\"activities\":\"" + escape(persona.activities) + "\"," +
                        "\"riskLevel\":\"" + escape(persona.riskLevel) + "\"" +
                        "}";
                    sendJsonResponse(exchange, json, 200);
                } else {
                    sendResponse(exchange, "Persona not found", 404);
                }
            } catch (Exception e) {
                sendResponse(exchange, "Server error", 500);
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
