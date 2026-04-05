package BackEnd.Soham.Trip.GetTrip.ByEmail;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class TripByEmailController implements HttpHandler {
	private final TripByEmailService service = new TripByEmailService();

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			try {
				InputStream is = exchange.getRequestBody();
				String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				Map<String, String> params = parseJson(body);

				String creatorEmail = params.get("creatorEmail");
				if (creatorEmail == null) {
					sendResponse(exchange, "Missing creatorEmail", 400);
					return;
				}

				List<Integer> ids = service.getTripIdsByEmail(creatorEmail);

				StringBuilder sb = new StringBuilder();
				sb.append("{\"tripIDs\":[");
				for (int i = 0; i < ids.size(); i++) {
					if (i > 0) sb.append(',');
					sb.append(ids.get(i));
				}
				sb.append("]}");

				sendResponse(exchange, sb.toString(), 200);
			} catch (Exception e) {
				sendResponse(exchange, "Invalid request", 400);
			}
		} else {
			sendResponse(exchange, "Method not allowed", 405);
		}
	}

	private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
		exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
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
