package BackEnd.Soham.Place.CreatePlace;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CreatePlaceController implements HttpHandler {
    private final CreatePlaceRepository repository = new CreatePlaceRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String eventIdStr = params.get("eventID");
                String name = params.get("name");
                String placetype = params.get("placetype");
                String seasonalAvailability = params.get("seasonalAvailability");
                String street = params.get("street");
                String city = params.get("city");
                String country = params.get("country");

                if (eventIdStr == null || name == null || placetype == null || seasonalAvailability == null || street == null || city == null || country == null) {
                    sendResponse(exchange, "Missing required field(s)", 400);
                    return;
                }

                int eventID;
                try {
                    eventID = Integer.parseInt(eventIdStr);
                } catch (NumberFormatException nfe) {
                    sendResponse(exchange, "Invalid eventID", 400);
                    return;
                }

                boolean inserted = repository.insertPlace(eventID, name, placetype, seasonalAvailability, street, city, country);
                if (inserted) {
                    sendResponse(exchange, "Place Created", 201);
                } else {
                    sendResponse(exchange, "Failed to create place", 500);
                }
            } catch (Exception e) {
                sendResponse(exchange, "Invalid request", 400);
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

    private Map<String, String> parseJson(String json) {
        Map<String, String> result = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            String content = json.substring(1, json.length() - 1).trim();
            String[] pairs = content.split(",");
            for (String pair : pairs) {
                int colon = pair.indexOf(':');
                if (colon <= 0) continue;
                String key = pair.substring(0, colon).trim().replace("\"", "");
                String value = pair.substring(colon + 1).trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                result.put(key, value);
            }
        }
        return result;
    }
}
