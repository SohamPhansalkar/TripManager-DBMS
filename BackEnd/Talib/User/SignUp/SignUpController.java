package BackEnd.Talib.User.SignUp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import BackEnd.Talib.Utils.InputValidator;

public class SignUpController implements HttpHandler {
    private final SignUpService service = new SignUpService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);

                String email = params.get("email");
                if(email!=null) InputValidator.validateEmail(email);
                String password = params.get("password");
                if(password!=null) InputValidator.validatePassword(password);
                String first_name = params.get("first_name");
                String last_name = params.get("last_name");
                String dob = params.get("dob");
                if(dob!=null) InputValidator.validateDate(dob);

                if (email == null || password == null || first_name == null || last_name == null || dob == null) {
                    sendResponse(exchange, "Fields missing", 400);
                    return;
                }

                boolean success = service.signUpuser(email, password, first_name, last_name, dob);

                if (success) {
                    sendJsonResponse(exchange, "{\"message\":\"Sign up Successful\"}", 200);
                } else {
                    sendResponse(exchange, "Unauthorized", 401);
                }
            } catch (IllegalArgumentException e) {
                sendResponse(exchange, "Validation Error: " + e.getMessage(), 400);
            } catch (Exception e) {
                e.printStackTrace();
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
