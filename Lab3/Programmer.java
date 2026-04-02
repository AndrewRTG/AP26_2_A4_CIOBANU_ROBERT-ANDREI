import java.time.LocalDate;
public class Programmer extends Person{
    private String mainLanguage;


    public Programmer(int id,String name, LocalDate birthDate, String nationality, String mainLanguage) {
        super( id,name, birthDate, nationality);
        this.mainLanguage = mainLanguage;
    }
    public String getMainLanguage() {
        return mainLanguage;
    }
}
