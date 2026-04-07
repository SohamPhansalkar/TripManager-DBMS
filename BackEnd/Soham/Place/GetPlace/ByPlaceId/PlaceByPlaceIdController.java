package BackEnd.Soham.Place.GetPlace.ByPlaceId;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import BackEnd.Soham.Place.GetPlace.ByPlaceId.PlaceByPlaceIdRepository.PlaceRecord;

public class PlaceByPlaceIdController implements HttpHandler {
    private final PlaceByPlaceIdRepository repository = new PlaceByPlaceIdRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String placeIdStr = params.get("placeID");
                if (placeIdStr == null) {
                    sendResponse(exchange, "Missing placeID", 400);
                    return;
                }

                int placeID;
                try {
                    placeID = Integer.parseInt(placeIdStr);
                } catch (NumberFormatException nfe) {
                    sendResponse(exchange, "Invalid placeID", 400);
                    return;
                }

                PlaceRecord p = repository.findByPlaceId(placeID);
                if (p == null) {
                    sendResponse(exchange, "Not Found", 404);
                    return;
                }

                StringBuilder sb = new StringBuilder();
                sb.append('{');
                sb.append("\"placeID\":").append(p.placeID).append(',');
                sb.append("\"eventID\":").append(p.eventID).append(',');
                sb.append("\"name\":\"").append(escape(p.name)).append("\"").append(',');
                sb.append("\"placetype\":\"").append(escape(p.placetype)).append("\"").append(',');
                sb.append("\"seasonalAvailability\":\"").append(escape(p.seasonalAvailability)).append("\"").append(',');
                sb.append("\"street\":\"").append(escape(p.street)).append("\"").append(',');
                sb.append("\"city\":\"").append(escape(p.city)).append("\"").append(',');
                sb.append("\"country\":\"").append(escape(p.country)).append("\"");
                sb.append('}');

                sendJsonResponse(exchange, sb.toString(), 200);
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

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
