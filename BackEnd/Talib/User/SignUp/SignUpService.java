package BackEnd.Talib.User.SignUp;

import java.util.*;
public class SignUpService {
    private final SignUpRepository repository = new SignUpRepository();

    public boolean signUpuser(String email, String password, String first_name, String last_name, String dob) {
        return repository.signUpuser(email, password, first_name, last_name, dob);
    }
}
