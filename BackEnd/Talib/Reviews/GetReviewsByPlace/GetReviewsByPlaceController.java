package BackEnd.Talib.Reviews.GetReviewsByPlace;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import BackEnd.Talib.Utils.HttpUtils;

public class GetReviewsByPlaceController implements HttpHandler {
    private final GetReviewsByPlaceService service = new GetReviewsByPlaceService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                String[] parts = path.split("/");
                // Expecting path like: /reviews/place/{id}
                if (parts.length >= 4) {
                    int placeID = Integer.parseInt(parts[3]);
                    String json = service.getReviewsByPlace(placeID);
                    HttpUtils.sendResponse(exchange, 200, json);
                } else {
                    HttpUtils.sendResponse(exchange, 400, "{\"error\": \"Invalid path\"}");
                }
            } else {
                HttpUtils.sendResponse(exchange, 405, "{\"error\": \"Method Not Allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}