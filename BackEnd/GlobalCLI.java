package BackEnd;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@SuppressWarnings("deprecation")
public class GlobalCLI {
    // Soham's backend (Winning conflicts)
    private static final String SOHAM_URL = "http://localhost:8080";
    // Talib's backend
    private static final String TALIB_URL = "http://localhost:8081";

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   Global TripManager CLI (Soham & Talib)   ");
        System.out.println("===========================================");

        while (true) {
            System.out.println("\n--- GLOBAL MAIN MENU ---");
            System.out.println("1. User Operations (Soham)");
            System.out.println("2. Persona Operations (Talib)");
            System.out.println("3. Trip Operations (Soham)");
            System.out.println("4. Day Operations (Soham)");
            System.out.println("5. Event Operations (Soham)");
            System.out.println("6. Place Operations (Mixed)");
            System.out.println("7. Review Operations (Talib)");
            System.out.println("8. Exit");
            System.out.print("Select an option: ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": userMenu(); break;
                case "2": personaMenu(); break;
                case "3": tripMenu(); break;
                case "4": dayMenu(); break;
                case "5": eventMenu(); break;
                case "6": placeMenu(); break;
                case "7": reviewMenu(); break;
                case "8": 
                    System.out.println("Exiting Global CLI...");
                    System.exit(0);
                    break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void userMenu() {
        System.out.println("\n--- USER MENU (Soham) ---");
        System.out.println("1. Sign Up");
        System.out.println("2. Log In");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Password: "); String pwd = scanner.nextLine();
            System.out.print("First Name: "); String fn = scanner.nextLine();
            System.out.print("Last Name: "); String ln = scanner.nextLine();
            System.out.print("DOB (YYYY-MM-DD): "); String dob = scanner.nextLine();
            String json = String.format("{\"email\":\"%s\", \"password\":\"%s\", \"first_name\":\"%s\", \"last_name\":\"%s\", \"dob\":\"%s\"}", email, pwd, fn, ln, dob);
            sendRequest(SOHAM_URL, "/signup", "POST", json);
        } else if (choice.equals("2")) {
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Password: "); String pwd = scanner.nextLine();
            String json = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, pwd);
            sendRequest(SOHAM_URL, "/login", "POST", json);
        }
    }

    private static void personaMenu() {
        System.out.println("\n--- PERSONA MENU (Talib) ---");
        System.out.println("1. Create/Update Persona");
        System.out.println("2. Get Persona");
        System.out.println("3. Delete Persona");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Persona Type (foodie/explorer/adventurer): "); String type = scanner.nextLine().trim().toLowerCase();
            String extra = "";
            if (type.equals("foodie")) {
                System.out.print("Dietary Preference: "); extra = ",\"dietaryPreference\":\"" + scanner.nextLine() + "\"";
            } else if (type.equals("explorer")) {
                System.out.print("Bucket List: "); extra = ",\"bucketList\":\"" + scanner.nextLine() + "\"";
            } else if (type.equals("adventurer")) {
                System.out.print("Activities: "); String acts = scanner.nextLine();
                System.out.print("Risk Level: "); String risk = scanner.nextLine();
                extra = ",\"activities\":\"" + acts + "\", \"riskLevel\":\"" + risk + "\"";
            }
            String json = String.format("{\"email\":\"%s\", \"personaType\":\"%s\"%s}", email, type, extra);
            sendRequest(TALIB_URL, "/persona", "POST", json);
        } else if (choice.equals("2")) {
            System.out.print("Email: "); String email = scanner.nextLine();
            sendRequest(TALIB_URL, "/persona/get/", "POST", "{\"email\":\"" + email + "\"}");
        } else if (choice.equals("3")) {
            System.out.print("Email: "); String email = scanner.nextLine();
            sendRequest(TALIB_URL, "/persona/delete", "POST", "{\"email\":\"" + email + "\"}");
        }
    }

    private static void tripMenu() {
        System.out.println("\n--- TRIP MENU (Soham) ---");
        System.out.println("1. Create Trip");
        System.out.println("2. Get Trip by ID");
        System.out.println("3. Get Trips by Email");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            System.out.print("Creator Email: "); String email = scanner.nextLine();
            System.out.print("Destination: "); String dest = scanner.nextLine();
            System.out.print("Budget: "); String budget = scanner.nextLine();
            System.out.print("Start Date (YYYY-MM-DD): "); String start = scanner.nextLine();
            System.out.print("End Date (YYYY-MM-DD): "); String end = scanner.nextLine();
            String json = String.format("{\"creatorEmail\":\"%s\", \"destination\":\"%s\", \"budget\":\"%s\", \"startDate\":\"%s\", \"endDate\":\"%s\"}", email, dest, budget, start, end);
            sendRequest(SOHAM_URL, "/createtrip", "POST", json);
        } else if (choice.equals("2")) {
            System.out.print("Trip ID: "); String id = scanner.nextLine();
            sendRequest(SOHAM_URL, "/gettripbyid", "POST", "{\"tripID\":\"" + id + "\"}");
        } else if (choice.equals("3")) {
            System.out.print("Creator Email: "); String email = scanner.nextLine();
            sendRequest(SOHAM_URL, "/gettripsbyemail", "POST", "{\"creatorEmail\":\"" + email + "\"}");
        }
    }

    private static void dayMenu() {
        System.out.println("\n--- DAY MENU (Soham) ---");
        System.out.println("1. Create Day");
        System.out.println("2. Get Days by Trip ID");
        System.out.println("3. Get Day by Day ID");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            System.out.print("Trip ID: "); String id = scanner.nextLine();
            System.out.print("Date (YYYY-MM-DD): "); String date = scanner.nextLine();
            sendRequest(SOHAM_URL, "/createday", "POST", "{\"tripID\":\"" + id + "\", \"date\":\"" + date + "\"}");
        } else if (choice.equals("2")) {
            System.out.print("Trip ID: "); String id = scanner.nextLine();
            sendRequest(SOHAM_URL, "/getdaysbytripid", "POST", "{\"tripID\":\"" + id + "\"}");
        } else if (choice.equals("3")) {
            System.out.print("Day ID: "); String id = scanner.nextLine();
            sendRequest(SOHAM_URL, "/getdaybydayid", "POST", "{\"dayID\":\"" + id + "\"}");
        }
    }

    private static void eventMenu() {
        System.out.println("\n--- EVENT MENU (Soham) ---");
        System.out.println("1. Create Event");
        System.out.println("2. Get Events by Day ID");
        System.out.println("3. Get Event by Event ID");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            System.out.print("Day ID: "); String day = scanner.nextLine();
            System.out.print("Trip ID: "); String trip = scanner.nextLine();
            System.out.print("Time (HH:MM:SS): "); String time = scanner.nextLine();
            System.out.print("Type (Place/Accommodation/Transport): "); String type = scanner.nextLine();
            System.out.print("Description: "); String desc = scanner.nextLine();
            System.out.print("Link: "); String link = scanner.nextLine();
            String json = String.format("{\"dayID\":\"%s\", \"tripID\":\"%s\", \"time\":\"%s\", \"type\":\"%s\", \"description\":\"%s\", \"link\":\"%s\"}", day, trip, time, type, desc, link);
            sendRequest(SOHAM_URL, "/createevent", "POST", json);
        } else if (choice.equals("2")) {
            System.out.print("Day ID: "); String id = scanner.nextLine();
            sendRequest(SOHAM_URL, "/geteventsbydayid", "POST", "{\"dayID\":\"" + id + "\"}");
        } else if (choice.equals("3")) {
            System.out.print("Event ID: "); String id = scanner.nextLine();
            sendRequest(SOHAM_URL, "/geteventbyeventid", "POST", "{\"eventID\":\"" + id + "\"}");
        }
    }

    private static void placeMenu() {
        System.out.println("\n--- PLACE MENU (Mixed) ---");
        System.out.println("1. Create Place (Soham)");
        System.out.println("2. Get Places by Event ID (Soham)");
        System.out.println("3. Get Place by Place ID (Soham Winning Conflict over Talib)");
        System.out.println("4. Get All Places (Talib)");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            System.out.print("Event ID: "); String eid = scanner.nextLine();
            System.out.print("Name: "); String name = scanner.nextLine();
            System.out.print("Place Type: "); String type = scanner.nextLine();
            System.out.print("Seasonal Availability: "); String sea = scanner.nextLine();
            System.out.print("Street: "); String st = scanner.nextLine();
            System.out.print("City: "); String city = scanner.nextLine();
            System.out.print("Country: "); String country = scanner.nextLine();
            String json = String.format("{\"eventID\":\"%s\", \"name\":\"%s\", \"placetype\":\"%s\", \"seasonalAvailability\":\"%s\", \"street\":\"%s\", \"city\":\"%s\", \"country\":\"%s\"}", eid, name, type, sea, st, city, country);
            sendRequest(SOHAM_URL, "/createplace", "POST", json);
        } else if (choice.equals("2")) {
            System.out.print("Event ID: "); String id = scanner.nextLine();
            sendRequest(SOHAM_URL, "/getplacesbyeventid", "POST", "{\"eventID\":\"" + id + "\"}");
        } else if (choice.equals("3")) {
            System.out.print("Place ID: "); String id = scanner.nextLine();
            sendRequest(SOHAM_URL, "/getplacebyplaceid", "POST", "{\"placeID\":\"" + id + "\"}");
        } else if (choice.equals("4")) {
            // Uniquely exists in Talib
            sendRequest(TALIB_URL, "/places/all", "POST", "{}");
        }
    }

    private static void reviewMenu() {
        System.out.println("\n--- REVIEW MENU (Talib) ---");
        System.out.println("1. Create Review");
        System.out.println("2. Update Review");
        System.out.println("3. Get Reviews by Place");
        System.out.println("4. Delete Review");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            System.out.print("User Email: "); String email = scanner.nextLine();
            System.out.print("Place ID: "); String pid = scanner.nextLine();
            System.out.print("Rating (1-5): "); String rating = scanner.nextLine();
            System.out.print("Date (YYYY-MM-DD): "); String date = scanner.nextLine();
            System.out.print("Comment: "); String comment = scanner.nextLine();
            String json = String.format("{\"userEmail\":\"%s\", \"placeID\":\"%s\", \"rating\":\"%s\", \"reviewDate\":\"%s\", \"comment\":\"%s\"}", email, pid, rating, date, comment);
            sendRequest(TALIB_URL, "/reviews", "POST", json);
        } else if (choice.equals("2")) {
            System.out.print("Review ID: "); String rid = scanner.nextLine();
            System.out.print("User Email: "); String email = scanner.nextLine();
            System.out.print("New Rating (1-5): "); String rating = scanner.nextLine();
            System.out.print("New Comment: "); String comment = scanner.nextLine();
            String json = String.format("{\"reviewID\":\"%s\", \"userEmail\":\"%s\", \"rating\":\"%s\", \"comment\":\"%s\"}", rid, email, rating, comment);
            sendRequest(TALIB_URL, "/reviews/update", "POST", json);
        } else if (choice.equals("3")) {
            System.out.print("Place ID: "); String pid = scanner.nextLine();
            sendRequest(TALIB_URL, "/reviews/place/", "POST", "{\"placeID\":\"" + pid + "\"}");
        } else if (choice.equals("4")) {
            System.out.print("Review ID: "); String rid = scanner.nextLine();
            System.out.print("User Email: "); String email = scanner.nextLine();
            sendRequest(TALIB_URL, "/reviews/delete", "POST", "{\"reviewID\":\"" + rid + "\", \"userEmail\":\"" + email + "\"}");
        }
    }

    private static void sendRequest(String baseUrl, String endpoint, String method, String jsonBody) {
        try {
            URL url = new URL(baseUrl + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            if (jsonBody != null && !jsonBody.isEmpty()) {
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                }
            }

            int responseCode = conn.getResponseCode();
            System.out.println("HTTP Status: " + responseCode);
            
            InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
            if (is != null) {
                String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("Response:\n" + response);
            }
        } catch (Exception e) {
            System.out.println("Error connecting to server (" + baseUrl + endpoint + "): " + e.getMessage());
        }
    }
}