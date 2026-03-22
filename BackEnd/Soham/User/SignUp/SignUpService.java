package BackEnd.Soham.User.SignUp;


public class SignUpService {
    private SignUpRepository repository = new SignUpRepository();

    public boolean verifySignUp(String email, String password, String  first_name, String  last_name,String dob) {
        boolean user = repository.signUpuser(email, password, first_name, last_name, dob);
        
        return user;
    }
}
