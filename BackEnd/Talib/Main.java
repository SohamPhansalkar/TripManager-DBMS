package BackEnd.Talib;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

import BackEnd.Talib.Places.GetAllPlaces.GetAllPlacesController;
import BackEnd.Talib.Places.GetPlaceById.GetPlaceByIdController;
import BackEnd.Talib.Reviews.CreateReview.CreateReviewController;
import BackEnd.Talib.Reviews.GetReviewsByPlace.GetReviewsByPlaceController;
import BackEnd.Talib.Persona.CreatePersona.CreatePersonaController;
import BackEnd.Talib.Persona.GetPersona.GetPersonaController;
import BackEnd.Talib.User.LogIn.LogInController;
import BackEnd.Talib.User.SignUp.SignUpController;


import BackEnd.Talib.Reviews.UpdateReview.UpdateReviewController;
import BackEnd.Talib.Reviews.DeleteReview.DeleteReviewController;
import BackEnd.Talib.Persona.UpdatePersona.UpdatePersonaController;
import BackEnd.Talib.Persona.DeletePersona.DeletePersonaController;
public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/places/all", new GetAllPlacesController());
        server.createContext("/places/", new GetPlaceByIdController());
        
        server.createContext("/reviews", new CreateReviewController());
        server.createContext("/reviews/place/", new GetReviewsByPlaceController());

        server.createContext("/persona", new CreatePersonaController());
        server.createContext("/persona/get/", new GetPersonaController());

        server.createContext("/login", new LogInController());
        server.createContext("/signup", new SignUpController());
        server.createContext("/reviews/update", new UpdateReviewController());
        server.createContext("/reviews/delete", new DeleteReviewController());
        server.createContext("/persona/update", new UpdatePersonaController());
        server.createContext("/persona/delete", new DeletePersonaController());


        System.out.println("Server started on http://localhost:8080");
        server.start();
    }
}
