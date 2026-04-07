package BackEnd.Soham.Day.GetDay.ByTripId;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import BackEnd.Soham.Day.GetDay.ByTripId.DayByTripIdRepository.DayRecord;

public class DayByTripIdController implements HttpHandler {
	private final DayByTripIdService service = new DayByTripIdService();

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			try {
				InputStream is = exchange.getRequestBody();
				String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				Map<String, String> params = parseJson(body);

				String tripIdStr = params.get("tripID");
				if (tripIdStr == null) {
					sendResponse(exchange, "Missing tripID", 400);
					return;
				}

				int tripID;
				try {
					tripID = Integer.parseInt(tripIdStr);
				} catch (NumberFormatException nfe) {
					sendResponse(exchange, "Invalid tripID", 400);
					return;
				}

				List<DayRecord> days = service.findByTripId(tripID);

				StringBuilder sb = new StringBuilder();
				sb.append("{\"days\":[");
				for (int i = 0; i < days.size(); i++) {
					DayRecord d = days.get(i);
					if (i > 0) sb.append(',');
					sb.append("{\"dayID\":" + d.dayID + ",\"date\":\"" + escape(d.date) + "\"}");
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
