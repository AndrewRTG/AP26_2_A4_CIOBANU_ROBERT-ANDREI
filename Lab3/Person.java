import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.time.LocalDate;

public class Person implements Profile, Comparable<Person> {
    private String name;
    private Map<Profile, String> relationships= new HashMap<>();
    private LocalDate birthDate;
    private String nationality;
    private int id;

    public Person(int id, String name, LocalDate birthDate, String nationality) {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public int importance(Profile profile) {

        return relationships.size();
   }
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Profile, String> getRelationships() {
        return relationships;
    }

    public void addRelationships(Profile p,  String type) {
       relationships.put(p, type);
    }

    @Override
    public int compareTo(Person other) {
        if (other==null)
            return 1;
        return this.name.compareTo(other.name);

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person person)) return false;
        return this.getId() == person.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        StringBuilder sb= new StringBuilder();
        sb.append("Person{name='").append(name).append("', relationships={");
        relationships.forEach((profile,type) ->{
            sb.append(profile.getName()).append("=").append(type).append(", ");
        });
        if (!relationships.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}}");
        return sb.toString();
    }
}
