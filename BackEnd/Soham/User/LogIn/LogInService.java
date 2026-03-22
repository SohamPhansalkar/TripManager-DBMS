package BackEnd.Soham.User.LogIn;
import BackEnd.Soham.User.UserEntity;


public class LogInService {
    private LogInRepository repository = new LogInRepository();

    public boolean verifyLogin(String email, String providedPassword) {
        UserEntity user = repository.findByEmail(email);
        
        if (user != null) {
            // In a real app, use BCrypt.checkpw() here!
            return user.getPassword().equals(providedPassword);
        }
        return false;
    }
}
