package BackEnd.Talib.Reviews.CreateReview;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CreateReviewController implements HttpHandler {
    private final CreateReviewRepository repository = new CreateReviewRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String email = params.get("userEmail");
                String placeIDStr = params.get("placeID");
                String accIDStr = params.get("accommodationID");
                String ratingStr = params.get("rating");
                String comment = params.get("comment");
                String reviewDate = params.get("reviewDate");

                if(email == null || ratingStr == null || reviewDate == null) {
                    sendResponse(exchange, "Missing fields", 400); return;
                }

                Integer placeID = (placeIDStr != null && !placeIDStr.equals("null") && !placeIDStr.isEmpty()) ? Integer.parseInt(placeIDStr) : null;
                Integer accID = (accIDStr != null && !accIDStr.equals("null") && !accIDStr.isEmpty()) ? Integer.parseInt(accIDStr) : null;
                int rating = Integer.parseInt(ratingStr);

                repository.createReview(email, placeID, accID, rating, comment, reviewDate);
                sendJsonResponse(exchange, "{\"message\":\"Review created successfully\"}", 201);
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
