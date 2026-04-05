package BackEnd.Talib.Reviews.CreateReview;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import BackEnd.Talib.Utils.HttpUtils;

public class CreateReviewController implements HttpHandler {
    private final CreateReviewService service = new CreateReviewService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if ("POST".equalsIgnoreCase(method)) {
                String body = HttpUtils.getBody(exchange);
                String email = HttpUtils.extractJsonField(body, "userEmail");
                String placeIDStr = HttpUtils.extractJsonField(body, "placeID");
                String accIDStr = HttpUtils.extractJsonField(body, "accommodationID");
                String ratingStr = HttpUtils.extractJsonField(body, "rating");
                String comment = HttpUtils.extractJsonField(body, "comment");
                String reviewDate = HttpUtils.extractJsonField(body, "reviewDate");

                Integer placeID = placeIDStr != null && !placeIDStr.equals("null") ? Integer.parseInt(placeIDStr) : null;
                Integer accID = accIDStr != null && !accIDStr.equals("null") ? Integer.parseInt(accIDStr) : null;
                int rating = Integer.parseInt(ratingStr);

                service.createReview(email, placeID, accID, rating, comment, reviewDate);
                HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Review created successfully\"}");
            } else {
                HttpUtils.sendResponse(exchange, 405, "{\"error\": \"Method Not Allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}