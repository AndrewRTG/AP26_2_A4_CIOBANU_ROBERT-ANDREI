import java.time.LocalDate;
import java.util.*;
public class Movie {
    private Integer id;
    private String title;
    private LocalDate releaseDate;
    private Integer duration;
    private Double score;
    private Genre genre;
    private List<Actor> actors=new ArrayList<>();
    public Movie(Integer id, String title, LocalDate releaseDate, Integer duration, Double score, Genre genre) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
        this.genre = genre;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }

    public List<Actor> getActors() { return actors; }
    public void setActors(List<Actor> actors) { this.actors = actors; }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }
    @Override
    public String toString() {
        return "Movie{title='" + title + "', genre=" + (genre != null ? genre.getName() : "null") +
                ", score=" + score + ", actors=" + actors.size() + "}";
    }

}
