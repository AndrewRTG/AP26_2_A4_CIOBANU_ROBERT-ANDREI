import java.util.Objects;

public class Company implements Profile, Comparable<Company>{
    private String name;
    private int numberOfEmployees;
    int id;

    public Company(int id, String name) {

        this.id=id;
        this.name = name;
        this.numberOfEmployees = 0;
    }
    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Profile profile)) return false;
        return this.getId() == profile.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Company other) {
        if (other==null) return 1;
        return this.name.compareTo(other.name);

    }
}
