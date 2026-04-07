package BackEnd.Talib.Utils;

public class InputValidator {
    public static void validateEmail(String email) throws IllegalArgumentException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format. Must contain '@' and '.'.");
        }
    }

    public static void validatePassword(String password) throws IllegalArgumentException {
        if (password == null || password.length() < 3) {
            throw new IllegalArgumentException("Password must be at least 3 characters long.");
        }
    }

    public static void validateRating(int rating) throws IllegalArgumentException {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }

    public static void validateDate(String dateStr) throws IllegalArgumentException {
        if (dateStr == null || !dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date must be in YYYY-MM-DD format.");
        }
    }
    
    public static void validateRequired(String value, String fieldName) throws IllegalArgumentException {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required and cannot be empty.");
        }
    }
}
