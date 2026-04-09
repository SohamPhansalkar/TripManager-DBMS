package BackEnd.Soham.Trip.ViewTrip;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ViewTripController implements HttpHandler {
    private final ViewTripService service = new ViewTripService();

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

                int tripID = Integer.parseInt(tripIdStr);
                TripViewDTO tripData = service.getTripDetails(tripID);

                if (tripData != null) {
                    String jsonResponse = toJson(tripData);
                    sendResponse(exchange, jsonResponse, 200);
                } else {
                    sendResponse(exchange, "Trip not found", 404);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, "Invalid request", 400);
            }
        } else {
            sendResponse(exchange, "Method not allowed", 405);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
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

    private String toJson(TripViewDTO trip) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"tripID\":").append(trip.getTripID()).append(",");
        sb.append("\"creatorEmail\":\"").append(trip.getCreatorEmail()).append("\",");
        sb.append("\"destination\":\"").append(trip.getDestination()).append("\",");
        sb.append("\"budget\":").append(trip.getBudget()).append(",");
        sb.append("\"startDate\":\"").append(trip.getStartDate()).append("\",");
        sb.append("\"endDate\":\"").append(trip.getEndDate()).append("\",");
        sb.append("\"days\":[");
        List<TripViewDTO.DayViewDTO> days = trip.getDays();
        for (int i = 0; i < days.size(); i++) {
            TripViewDTO.DayViewDTO day = days.get(i);
            sb.append("{");
            sb.append("\"dayID\":").append(day.getDayID()).append(",");
            sb.append("\"date\":\"").append(day.getDate()).append("\",");
            sb.append("\"events\":[");
            List<TripViewDTO.EventViewDTO> events = day.getEvents();
            for (int j = 0; j < events.size(); j++) {
                TripViewDTO.EventViewDTO event = events.get(j);
                sb.append("{");
                sb.append("\"eventID\":").append(event.getEventID()).append(",");
                sb.append("\"time\":\"").append(event.getTime()).append("\",");
                sb.append("\"type\":\"").append(event.getType()).append("\",");
                sb.append("\"description\":\"").append(event.getDescription()).append("\"");
                sb.append("}");
                if (j < events.size() - 1) sb.append(",");
            }
            sb.append("]");
            sb.append("}");
            if (i < days.size() - 1) sb.append(",");
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
