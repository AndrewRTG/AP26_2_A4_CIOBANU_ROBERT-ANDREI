package com.example.demo.controller;

import com.example.demo.dao.MovieDAO;
import com.example.demo.entitati.Movie;
import com.example.demo.solver.IndependentMovieListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Filme API", description = "Operatii CRUD pentru gestionarea filmelor din baza de date")
public class MovieController {

    private final MovieDAO movieDAO;
    private final IndependentMovieListService independentService;

    public MovieController(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
        this.independentService = new IndependentMovieListService();

    }

    @Operation(summary = "Obține toate filmele", description = "Returnează o listă completă cu filmele din baza de date.")
    @GetMapping
    public ResponseEntity<List<Movie>> getMovies() throws SQLException {
        return ResponseEntity.ok(movieDAO.getAll());
    }

    @Operation(summary = "Adaugă un film nou", description = "Inserează un film în baza de date. ID-ul este generat automat.")
    @PostMapping
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) throws SQLException {
        movieDAO.create(movie);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Film adăugat cu succes cu ID-ul: " + movie.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovie(@PathVariable Integer id, @RequestBody Movie movie) throws SQLException {
        movieDAO.update(id, movie);
        return ResponseEntity.ok("Filmul a fost actualizat cu succes.");
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> updateMovieScore(@PathVariable Integer id, @RequestBody Movie movie) throws SQLException {
        movieDAO.updateScore(id, movie);
        return ResponseEntity.ok("Scorul filmului a fost actualizat.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer id) throws SQLException {
        movieDAO.delete(id);
        return ResponseEntity.ok("Filmul a fost șters cu succes.");
    }

    @GetMapping("/independent")
    public ResponseEntity<?> getIndependentMovies(@RequestParam(defaultValue = "3") int minSize) {
        try {
            List<Movie> allMovies = movieDAO.getAll();
            List<Movie> independentList = independentService.findIndependentMovies(allMovies, minSize);
            if (independentList.isEmpty()) {
                return ResponseEntity.status(404).body("Nu s a gasit lista de marimea impusa");
            }
            return ResponseEntity.ok(independentList);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Eroare la accesarea bazei de date.");
        }
    }
}