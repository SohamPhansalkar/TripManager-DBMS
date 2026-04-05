package BackEnd.Talib.Places.GetPlaceById;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import BackEnd.Talib.Utils.HttpUtils;

public class GetPlaceByIdController implements HttpHandler {
    private final GetPlaceByIdService service = new GetPlaceByIdService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                String[] parts = path.split("/");
                // Expecting /places/{id}
                if (parts.length >= 3) {
                    int placeID = Integer.parseInt(parts[2]);
                    String json = service.getPlaceById(placeID);
                    if (json != null) {
                        HttpUtils.sendResponse(exchange, 200, json);
                    } else {
                        HttpUtils.sendResponse(exchange, 404, "{\"error\": \"Place not found\"}");
                    }
                } else {
                    HttpUtils.sendResponse(exchange, 400, "{\"error\": \"Invalid path, missing ID\"}");
                }
            } else {
                HttpUtils.sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        } catch (NumberFormatException e) {
            HttpUtils.sendResponse(exchange, 400, "{\"error\": \"Invalid ID format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}