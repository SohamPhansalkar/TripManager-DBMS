package BackEnd.Talib.Persona.UpdatePersona;

import java.util.*;
public class UpdatePersonaService {
    private final UpdatePersonaRepository repository = new UpdatePersonaRepository();

    public void validate() {
        repository.validate();
    }
    public void executeUpdate(String email, String personaType) throws Exception {
        repository.executeUpdate(email, personaType);
    }
}
