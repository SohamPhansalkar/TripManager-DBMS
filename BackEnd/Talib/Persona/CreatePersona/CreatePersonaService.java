package BackEnd.Talib.Persona.CreatePersona;
public class CreatePersonaService {
    private final CreatePersonaRepository repository = new CreatePersonaRepository();

    public void createPersona(String email, String type, String dietary, String bucketList, String activities, String riskLevel) throws Exception {
        repository.createPersona(email, type, dietary, bucketList, activities, riskLevel);
    }
}
