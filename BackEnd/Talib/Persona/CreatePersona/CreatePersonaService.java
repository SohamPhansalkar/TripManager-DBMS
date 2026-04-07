package BackEnd.Talib.Persona.CreatePersona;

import java.util.*;
public class CreatePersonaService {
    private final CreatePersonaRepository repository = new CreatePersonaRepository();

    public void createPersona(String email, String type, String dietary, String bucketList, String activities, String riskLevel) throws Exception {
        repository.createPersona(email, type, dietary, bucketList, activities, riskLevel);
    }
}
