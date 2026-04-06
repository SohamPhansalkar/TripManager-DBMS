package BackEnd.Soham;

import BackEnd.Soham.Day.CreateDay.CreateDayController;
import BackEnd.Soham.Day.GetDay.ByDayId.DayByDayIDController;
import BackEnd.Soham.Day.GetDay.ByTripId.DayByTripIdController;
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

        System.out.println("Server started on http://localhost:8080");
        server.start();
    }
}
