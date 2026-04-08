package BackEnd.Soham;

import BackEnd.Soham.Day.CreateDay.CreateDayController;
import BackEnd.Soham.Day.GetDay.ByDayId.DayByDayIDController;
import BackEnd.Soham.Day.GetDay.ByTripId.DayByTripIdController;
import BackEnd.Soham.Event.CreateEvent.CreateEventController;
import BackEnd.Soham.Event.GetEvent.ByDayId.EventByDayIdController;
import BackEnd.Soham.Event.GetEvent.ByEventId.EventByEventIdController;
import BackEnd.Soham.Place.CreatePlace.CreatePlaceController;
import BackEnd.Soham.Place.GetPlace.ByEventId.PlaceByEventIdController;
import BackEnd.Soham.Place.GetPlace.ByPlaceId.PlaceByPlaceIdController;
import BackEnd.Soham.Trip.CreateTrip.CreateTripController;
import BackEnd.Soham.Trip.GetTrip.ByEmail.TripByEmailController;
import BackEnd.Soham.Trip.GetTrip.ById.TripByIdController;
import BackEnd.Soham.User.LogIn.LogInController;
import BackEnd.Soham.User.SignUp.SignUpController;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Global CORS Filter
        Filter corsFilter = new Filter() {
            @Override
            public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(204, -1);
                    return;
                }
                chain.doFilter(exchange);
            }

            @Override
            public String description() {
                return "Global CORS filter";
            }
        };

        addContextWithCors(server, "/login", new LogInController(), corsFilter);
        addContextWithCors(server, "/signup", new SignUpController(), corsFilter);
        addContextWithCors(server, "/createtrip", new CreateTripController(), corsFilter);
        addContextWithCors(server, "/gettripbyid", new TripByIdController(), corsFilter);
        addContextWithCors(server, "/gettripsbyemail", new TripByEmailController(), corsFilter);
        addContextWithCors(server, "/createday", new CreateDayController(), corsFilter);
        addContextWithCors(server, "/getdaybydayid", new DayByDayIDController(), corsFilter);
        addContextWithCors(server, "/getdaysbytripid", new DayByTripIdController(), corsFilter);
        addContextWithCors(server, "/createevent", new CreateEventController(), corsFilter);
        addContextWithCors(server, "/geteventsbydayid", new EventByDayIdController(), corsFilter);
        addContextWithCors(server, "/geteventbyeventid", new EventByEventIdController(), corsFilter);
        addContextWithCors(server, "/createplace", new CreatePlaceController(), corsFilter);
        addContextWithCors(server, "/getplacesbyeventid", new PlaceByEventIdController(), corsFilter);
        addContextWithCors(server, "/getplacebyplaceid", new PlaceByPlaceIdController(), corsFilter);

        System.out.println("Server started on http://localhost:8080");
        server.start();
    }

    private static void addContextWithCors(HttpServer server, String path, com.sun.net.httpserver.HttpHandler handler, Filter corsFilter) {
        HttpContext context = server.createContext(path, handler);
        context.getFilters().add(corsFilter);
    }
}
