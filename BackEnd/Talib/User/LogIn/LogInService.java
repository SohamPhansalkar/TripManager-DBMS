package BackEnd.Talib.User.LogIn;
import BackEnd.Talib.User.UserEntity;


public class LogInService {
    private LogInRepository repository = new LogInRepository();

    public boolean verifyLogin(String email, String providedPassword) {
        UserEntity user = repository.findByEmail(email);
        
        if (user != null) {
            return user.getPassword().equals(providedPassword);
        }
        return false;
    }
}