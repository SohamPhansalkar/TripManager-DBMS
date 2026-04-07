package BackEnd.Talib.CLI;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class InteractiveCLI {
    private static final String BASE_URL = "http://localhost:8080";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Starting Interactive CLI for Talib CRUD Operations...");
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. User Operations");
            System.out.println("2. Persona Operations");
            System.out.println("3. Place Operations");
            System.out.println("4. Review Operations");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": userMenu(); break;
                case "2": personaMenu(); break;
                case "3": placeMenu(); break;
                case "4": reviewMenu(); break;
                case "5": 
                    System.out.println("Exiting...");
                    System.exit(0);
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void userMenu() {
        System.out.println("\n--- USER MENU ---");
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
            sendRequest("/signup", "POST", json);
        } else if (choice.equals("2")) {
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Password: "); String pwd = scanner.nextLine();
            String json = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, pwd);
            sendRequest("/login", "POST", json);
        }
    }

    private static void personaMenu() {
        System.out.println("\n--- PERSONA MENU ---");
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
            sendRequest("/persona", "POST", json);
        } else if (choice.equals("2")) {
            System.out.print("Email: "); String email = scanner.nextLine();
            sendRequest("/persona/get/", "POST", "{\"email\":\"" + email + "\"}");
        } else if (choice.equals("3")) {
            System.out.print("Email: "); String email = scanner.nextLine();
            sendRequest("/persona/delete", "POST", "{\"email\":\"" + email + "\"}");
        }
    }

    private static void placeMenu() {
        System.out.println("\n--- PLACE MENU ---");
        System.out.println("1. Get All Places");
        System.out.println("2. Get Place by ID");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            sendRequest("/places/all", "POST", "{}");
        } else if (choice.equals("2")) {
            System.out.print("Place ID: "); String id = scanner.nextLine();
            sendRequest("/places/", "POST", "{\"placeID\":\"" + id + "\"}");
        }
    }

    private static void reviewMenu() {
        System.out.println("\n--- REVIEW MENU ---");
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
            sendRequest("/reviews", "POST", json);
        } else if (choice.equals("2")) {
            System.out.print("Review ID: "); String rid = scanner.nextLine();
            System.out.print("User Email: "); String email = scanner.nextLine();
            System.out.print("New Rating (1-5): "); String rating = scanner.nextLine();
            System.out.print("New Comment: "); String comment = scanner.nextLine();
            String json = String.format("{\"reviewID\":\"%s\", \"userEmail\":\"%s\", \"rating\":\"%s\", \"comment\":\"%s\"}", rid, email, rating, comment);
            sendRequest("/reviews/update", "POST", json);
        } else if (choice.equals("3")) {
            System.out.print("Place ID: "); String pid = scanner.nextLine();
            sendRequest("/reviews/place/", "POST", "{\"placeID\":\"" + pid + "\"}");
        } else if (choice.equals("4")) {
            System.out.print("Review ID: "); String rid = scanner.nextLine();
            System.out.print("User Email: "); String email = scanner.nextLine();
            sendRequest("/reviews/delete", "POST", "{\"reviewID\":\"" + rid + "\", \"userEmail\":\"" + email + "\"}");
        }
    }

    private static void sendRequest(String endpoint, String method, String jsonBody) {
        try {
            URL url = new URL(BASE_URL + endpoint);
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
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}