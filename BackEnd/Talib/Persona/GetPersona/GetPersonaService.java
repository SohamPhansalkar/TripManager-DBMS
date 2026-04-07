package BackEnd.Talib.Persona.GetPersona;

import java.util.*;
public class GetPersonaService {
    private final GetPersonaRepository repository = new GetPersonaRepository();

    public GetPersonaRepository.PersonaRecord getPersona(String email) throws Exception {
        return repository.getPersona(email);
    }
}

