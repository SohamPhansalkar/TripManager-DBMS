package BackEnd.Talib.Places.GetPlaceById;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import BackEnd.Talib.Places.GetPlaceById.GetPlaceByIdRepository.PlaceDetailRecord;

public class GetPlaceByIdController implements HttpHandler {
    private final GetPlaceByIdService service = new GetPlaceByIdService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String idStr = params.get("placeID");
                if (idStr == null) {
                    sendResponse(exchange, "Missing placeID", 400); return;
                }

                int placeID = Integer.parseInt(idStr);
                PlaceDetailRecord place = service.getPlaceById(placeID);
                if (place != null) {
                    StringBuilder json = new StringBuilder("{");
                    json.append("\"placeID\":").append(place.placeID).append(",");
                    json.append("\"name\":\"").append(escape(place.name)).append("\",");
                    json.append("\"location\":\"").append(escape(place.location)).append("\",");
                    json.append("\"description\":\"").append(escape(place.description)).append("\"");

                    if (place.popularityScore != null) {
                        json.append(",\"popularityScore\":").append(place.popularityScore).append(",");
                        json.append("\"recommendedDuration\":\"").append(escape(place.recommendedDuration)).append("\"");
                    }
                    if (place.priceRange != null) {
                        json.append(",\"priceRange\":\"").append(escape(place.priceRange)).append("\",");
                        json.append("\"mustTryDishes\":\"").append(escape(place.mustTryDishes)).append("\"");
                    }
                    json.append("}");
                    sendJsonResponse(exchange, json.toString(), 200);
                } else {
                    sendResponse(exchange, "Place not found", 404);
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, "Invalid ID format", 400);
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
