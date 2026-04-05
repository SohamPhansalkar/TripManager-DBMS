package BackEnd.Soham.Trip.CreateTrip;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import BackEnd.Soham.Trip.TripEntity;

public class CreateTripController implements HttpHandler {
	private final CreateTripService service = new CreateTripService();

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			try {
				InputStream is = exchange.getRequestBody();
				String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				Map<String, String> params = parseJson(body);

				String creatorEmail = params.get("creatorEmail");
				String destination = params.get("destination");
				String budgetStr = params.get("budget");
				String startDate = params.get("startDate");
				String endDate = params.get("endDate");

				if (creatorEmail == null || destination == null || budgetStr == null || startDate == null || endDate == null) {
					sendResponse(exchange, "Missing required field(s)", 400);
					return;
				}

				Integer budget;
				try {
					budget = Integer.parseInt(budgetStr);
				} catch (NumberFormatException nfe) {
					sendResponse(exchange, "Invalid budget", 400);
					return;
				}

				TripEntity trip = new TripEntity(creatorEmail, destination, budget, startDate, endDate);

				boolean created = service.createTrip(trip);
				if (created) {
					sendResponse(exchange, "Trip Created", 201);
				} else {
					sendResponse(exchange, "Failed to create trip", 500);
				}
			} catch (Exception e) {
				sendResponse(exchange, "Invalid request", 400);
			}
		} else {
			sendResponse(exchange, "Method not allowed", 405);
		}
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
