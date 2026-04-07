package BackEnd.Soham.Event.GetEvent.ByEventId;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import BackEnd.Soham.Event.GetEvent.ByEventId.EventByEventIdRepository.EventRecord;

public class EventByEventIdController implements HttpHandler {
	private final EventByEventIdRepository repository = new EventByEventIdRepository();

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

				EventRecord er = repository.findByEventId(eventID);
				if (er == null) {
					sendResponse(exchange, "Not Found", 404);
					return;
				}

				StringBuilder sb = new StringBuilder();
				sb.append('{');
				sb.append("\"eventID\":").append(er.eventID).append(',');
				sb.append("\"dayID\":").append(er.dayID).append(',');
				sb.append("\"tripID\":").append(er.tripID).append(',');
				sb.append("\"time\":\"").append(escape(er.time)).append("\"").append(',');
				sb.append("\"type\":\"").append(escape(er.type)).append("\"").append(',');
				sb.append("\"description\":\"").append(escape(er.description)).append("\"").append(',');
				if (er.link == null) sb.append("\"link\":null"); else sb.append("\"link\":\"").append(escape(er.link)).append("\"");
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

	private String escape(String s) {
		if (s == null) return "";
		return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
	}
}
