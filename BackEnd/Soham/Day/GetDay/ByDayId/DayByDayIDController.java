package BackEnd.Soham.Day.GetDay.ByDayId;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import BackEnd.Soham.Day.GetDay.ByDayId.DayByDayIDRepository.DayRecord;

public class DayByDayIDController implements HttpHandler {
	private final DayByDayIDRepository repository = new DayByDayIDRepository();

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

				DayRecord rec = repository.findById(dayID);
				if (rec == null) {
					sendResponse(exchange, "Not Found", 404);
					return;
				}

				String json = "{" +
						"\"tripID\":" + rec.tripID + "," +
						"\"date\":\"" + escape(rec.date) + "\"" +
						"}";

				sendJsonResponse(exchange, json, 200);
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
