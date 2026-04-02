import java.time.LocalDate;

public class Designer extends Person{
    private String specialization;
    public Designer(int id,String name, LocalDate birthDate, String nationality, String designType) {
        super(id,name, birthDate, nationality);
        this.specialization = designType;
    }
    public String getSpecialization() {
        return specialization;
    }
}
