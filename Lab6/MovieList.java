import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private Integer id;
    private String name;
    private LocalDateTime creationTimestamp;
    private List<Movie> movies = new ArrayList<>();

    public MovieList(Integer id, String name, LocalDateTime creationTimestamp) {
        this.id = id;
        this.name = name;
        this.creationTimestamp = creationTimestamp;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreationTimestamp() { return creationTimestamp; }
    public void setCreationTimestamp(LocalDateTime creationTimestamp) { this.creationTimestamp = creationTimestamp; }

    public List<Movie> getMovies() { return movies; }
    public void setMovies(List<Movie> movies) { this.movies = movies; }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }

    @Override
    public String toString() {
        return "MovieList{id=" + id + ", name='" + name + "', movies=" + movies.size() + "}";
    }
}