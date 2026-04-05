package BackEnd.Talib.Places.GetAllPlaces;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import BackEnd.Talib.Utils.HttpUtils;

public class GetAllPlacesController implements HttpHandler {
    private final GetAllPlacesService service = new GetAllPlacesService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            HttpUtils.sendResponse(exchange, 200, service.getAllPlaces());
        } catch (Exception e) {
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"Server error\"}");
        }
    }
}
