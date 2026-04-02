import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String location;
    private String year;
    private String author;
    private Set<Concept> concepts;

    public Resource(String id, String title, String location, String year, String author, Concept... concepts) { //vector de forma concept[]
        this.id = id;
        this.title = title;
        this.location = location;
        this.year = year;
        this.author = author;
        this.concepts = new HashSet<>(Arrays.asList(concepts));
    }

    public Set<Concept> getConcepts() { return concepts; }
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getYear() { return year; }
    public String getAuthor() { return author; }


    @Override
    public String toString() {
        return "Resource{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", concepts=" + concepts +
                '}';
    }
}