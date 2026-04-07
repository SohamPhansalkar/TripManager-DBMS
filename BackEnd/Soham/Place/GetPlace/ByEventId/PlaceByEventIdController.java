package BackEnd.Soham.Place.GetPlace.ByEventId;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import BackEnd.Soham.Place.GetPlace.ByEventId.PlaceByEventIdRepository.PlaceRecord;

public class PlaceByEventIdController implements HttpHandler {
    private final PlaceByEventIdRepository repository = new PlaceByEventIdRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String eventIdStr = params.get("eventID");
                if (eventIdStr == null) {
                    sendResponse(exchange, "Missing eventID", 400);
                    return;
                }

                int eventID;
                try {
                    eventID = Integer.parseInt(eventIdStr);
                } catch (NumberFormatException nfe) {
                    sendResponse(exchange, "Invalid eventID", 400);
                    return;
                }

                List<PlaceRecord> places = repository.findByEventId(eventID);

                StringBuilder sb = new StringBuilder();
                sb.append("{\"places\":[");
                for (int i = 0; i < places.size(); i++) {
                    PlaceRecord p = places.get(i);
                    if (i > 0) sb.append(',');
                    sb.append("{\"placeID\":").append(p.placeID)
                      .append(",\"eventID\":").append(p.eventID)
                      .append(",\"name\":\"").append(escape(p.name)).append("\"")
                      .append(",\"placetype\":\"").append(escape(p.placetype)).append("\"")
                      .append(",\"seasonalAvailability\":\"").append(escape(p.seasonalAvailability)).append("\"")
                      .append(",\"street\":\"").append(escape(p.street)).append("\"")
                      .append(",\"city\":\"").append(escape(p.city)).append("\"")
                      .append(",\"country\":\"").append(escape(p.country)).append("\"")
                      .append('}');
                }
                sb.append("]}");

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
