package BackEnd.Soham.Event.GetEvent.ByDayId;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import BackEnd.Soham.Event.GetEvent.ByDayId.EventByDayIdRepository.EventRecord;

public class EventByDayIdController implements HttpHandler {
	private final EventByDayIdService service = new EventByDayIdService();

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			try {
				InputStream is = exchange.getRequestBody();
				String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				Map<String, String> params = parseJson(body);

				String dayIdStr = params.get("dayID");
				if (dayIdStr == null) {
					sendResponse(exchange, "Missing dayID", 400);
					return;
				}

				int dayID;
				try {
					dayID = Integer.parseInt(dayIdStr);
				} catch (NumberFormatException nfe) {
					sendResponse(exchange, "Invalid dayID", 400);
					return;
				}

				List<EventRecord> events = service.findByDayId(dayID);

				StringBuilder sb = new StringBuilder();
				sb.append("{\"events\":[");
				for (int i = 0; i < events.size(); i++) {
					EventRecord e = events.get(i);
					if (i > 0) sb.append(',');
					sb.append("{\"eventID\":" + e.eventID + ",\"dayID\":" + e.dayID + ",\"tripID\":" + e.tripID + ",\"time\":\"" + escape(e.time) + "\",\"type\":\"" + escape(e.type) + "\",\"description\":\"" + escape(e.description) + "\",");
					if (e.link == null) sb.append("\"link\":null}"); else sb.append("\"link\":\"" + escape(e.link) + "\"}");
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
