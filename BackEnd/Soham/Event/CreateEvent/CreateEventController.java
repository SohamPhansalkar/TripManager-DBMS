package BackEnd.Soham.Event.CreateEvent;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CreateEventController implements HttpHandler {
	private final CreateEventRepository repository = new CreateEventRepository();

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			try {
				InputStream is = exchange.getRequestBody();
				String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				Map<String, String> params = parseJson(body);

				String dayIdStr = params.get("dayID");
				String tripIdStr = params.get("tripID");
				String time = params.get("time");
				String type = params.get("type");
				String description = params.get("description");
				String link = params.get("link");

				if (dayIdStr == null || tripIdStr == null || time == null || type == null || description == null) {
					sendResponse(exchange, "Missing required field(s).", 400);
					return;
				}

				int dayID, tripID;
				try {
					dayID = Integer.parseInt(dayIdStr);
					tripID = Integer.parseInt(tripIdStr);
				} catch (NumberFormatException nfe) {
					sendResponse(exchange, "Invalid numeric field", 400);
					return;
				}

				boolean inserted = repository.insertEvent(dayID, tripID, time, type, description, link);
				if (inserted) {
					sendResponse(exchange, "Event Created", 201);
				} else {
					sendResponse(exchange, "Failed to create event", 500);
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
			String content = json.substring(1, json.length() - 1).trim();
			// split on commas that are not inside quotes would be ideal; keep simple: split then
			// split each pair on the first ':' to allow values containing ':' (e.g. time "12:30").
			String[] pairs = content.split(",");
			for (String pair : pairs) {
				int colon = pair.indexOf(':');
				if (colon <= 0) continue;
				String key = pair.substring(0, colon).trim().replace("\"", "");
				String value = pair.substring(colon + 1).trim();
				// remove surrounding quotes if present
				if (value.startsWith("\"") && value.endsWith("\"")) {
					value = value.substring(1, value.length() - 1);
				}
				result.put(key, value);
			}
		}
		return result;
	}
}
