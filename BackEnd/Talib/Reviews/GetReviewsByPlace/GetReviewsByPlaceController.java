package BackEnd.Talib.Reviews.GetReviewsByPlace;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import BackEnd.Talib.Utils.InputValidator;
import java.util.List;
import BackEnd.Talib.Reviews.GetReviewsByPlace.GetReviewsByPlaceRepository.ReviewRecord;

public class GetReviewsByPlaceController implements HttpHandler {
    private final GetReviewsByPlaceRepository repository = new GetReviewsByPlaceRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String placeIdStr = params.get("placeID");
                if (placeIdStr == null) {
                    sendResponse(exchange, "Missing placeID", 400); return;
                }

                int placeID = Integer.parseInt(placeIdStr);
                List<ReviewRecord> reviews = repository.getReviewsByPlace(placeID);
                
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < reviews.size(); i++) {
                    ReviewRecord r = reviews.get(i);
                    if (i > 0) sb.append(',');
                    sb.append("{\"rating\":").append(r.rating)
                      .append(",\"comment\":\"").append(escape(r.comment))
                      .append("\",\"reviewDate\":\"").append(escape(r.reviewDate))
                      .append("\",\"likes\":").append(r.likes)
                      .append(",\"dislikes\":").append(r.dislikes).append("}");
                }
                sb.append("]");
                sendJsonResponse(exchange, sb.toString(), 200);
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
