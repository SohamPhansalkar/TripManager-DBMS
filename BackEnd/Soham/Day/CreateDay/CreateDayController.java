package BackEnd.Soham.Day.CreateDay;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CreateDayController implements HttpHandler {
    private final CreateDayService service = new CreateDayService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String tripIdStr = params.get("tripID");
                String date = params.get("date");

                if (tripIdStr == null || date == null) {
                    sendResponse(exchange, "Missing required field(s)", 400);
                    return;
                }

                int tripID;
                try {
                    tripID = Integer.parseInt(tripIdStr);
                } catch (NumberFormatException nfe) {
                    sendResponse(exchange, "Invalid tripID", 400);
                    return;
                }

                boolean inserted = service.insertDay(tripID, date);
                if (inserted) {
                    sendResponse(exchange, "Day Created", 201);
                } else {
                    sendResponse(exchange, "Failed to create day", 500);
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
            String[] pairs = json.substring(1, json.length() - 1).split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
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

