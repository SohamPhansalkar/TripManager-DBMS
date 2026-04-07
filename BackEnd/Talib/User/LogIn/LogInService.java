package BackEnd.Talib.User.LogIn;

import java.util.*;
public class LogInService {
    private final LogInRepository repository = new LogInRepository();

    public boolean authenticateUser(String email, String password) {
        return repository.authenticateUser(email, password);
    }
}
