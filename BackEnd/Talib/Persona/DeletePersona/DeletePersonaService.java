package BackEnd.Talib.Persona.DeletePersona;
public class DeletePersonaService {
    private final DeletePersonaRepository repository = new DeletePersonaRepository();

    public void validate() {
        repository.validate();
    }
    public void executeDelete(String email) throws Exception {
        repository.executeDelete(email);
    }
}
