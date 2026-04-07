package BackEnd.Soham.User.LogIn;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LogInController implements HttpHandler {
    private final LogInService service = new LogInService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String email = params.get("email");
                String password = params.get("password");

                if (email == null || password == null) {
                    sendResponse(exchange, "Email or password missing", 400);
                    return;
                }

                if (!isEmailValid(email)) {
                    sendResponse(exchange, "Invalid email", 400);
                    return;
                }

                boolean success = service.verifyLogin(email, password);

                if (success) {
                    sendResponse(exchange, "Login Successful", 200);
                } else {
                    sendResponse(exchange, "Unauthorized", 401);
                }
            } catch (Exception e) {
                sendResponse(exchange, "Invalid request", 400);
            }
        } else {
            sendResponse(exchange, "Method not allowed", 405);
        }
    }

    private boolean isEmailValid(String email) {
        if (email == null) return false;
        email = email.trim();
        if (email.isEmpty()) return false;
        int at = email.indexOf('@');
        int dot = email.lastIndexOf('.');
        if (at <= 0 || dot <= at + 1 || dot == email.length() - 1) return false;
        if (email.contains(" ")) return false;
        return true;
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
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