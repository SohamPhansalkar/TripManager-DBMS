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


import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress; 

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/login", new LogInController());

        server.createContext("/signup", new SignUpController());

        server.createContext("/createtrip", new CreateTripController());

        server.createContext("/gettripbyid", new TripByIdController());

        server.createContext("/gettripsbyemail", new TripByEmailController());

        server.createContext("/createday", new CreateDayController());

        server.createContext("/getdaybydayid", new DayByDayIDController());

        server.createContext("/getdaysbytripid", new DayByTripIdController());

        server.createContext("/createevent", new CreateEventController());

        server.createContext("/geteventsbydayid", new EventByDayIdController());

        server.createContext("/geteventbyeventid", new EventByEventIdController());

        server.createContext("/createplace", new CreatePlaceController());

        server.createContext("/getplacesbyeventid", new PlaceByEventIdController());

        server.createContext("/getplacebyplaceid", new PlaceByPlaceIdController());

        System.out.println("Server started on http://localhost:8080");
        server.start();
    }
}
