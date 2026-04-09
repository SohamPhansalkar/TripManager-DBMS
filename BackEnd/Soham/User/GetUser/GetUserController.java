package BackEnd.Soham.User.GetUser;

import BackEnd.Soham.User.UserEntity;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GetUserController implements HttpHandler {
    private final GetUserRepository repository = new GetUserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String email = params.get("email");

                if (email == null) {
                    sendResponse(exchange, "Email missing", 400);
                    return;
                }

                UserEntity user = repository.getUserByEmail(email);

                if (user != null) {
                    String jsonResponse = String.format(
                        "{\"email\":\"%s\", \"password\":\"%s\", \"firstName\":\"%s\", \"lastName\":\"%s\", \"dob\":\"%s\"}",
                        user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getDob()
                    );
                    sendResponse(exchange, jsonResponse, 200);
                } else {
                    sendResponse(exchange, "User not found", 404);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, "Internal server error", 500);
            }
        } else {
            sendResponse(exchange, "Method not allowed", 405);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
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
