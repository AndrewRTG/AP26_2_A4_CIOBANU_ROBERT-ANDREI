package com.example.demo.client;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import com.example.demo.entitati.Movie;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;


//@Component
public class MovieRestClient implements CommandLineRunner {


    private static final String BASE_URL = "http://localhost:8081/api/movies";

    private final RestTemplate restTemplate;

    public MovieRestClient() {

        this.restTemplate = new RestTemplate();
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("---Testare POST");
            Movie newMovie = new Movie(null, "Avatar", LocalDate.of(2009, 12, 18), 162, 7.8, null);

            ResponseEntity<String> postResponse = restTemplate.postForEntity(BASE_URL, newMovie, String.class);
            System.out.println("Răspuns server: " + postResponse.getBody());


            System.out.println("\n---Testare GET ");

            Movie[] movies = restTemplate.getForObject(BASE_URL, Movie[].class);
            if (movies != null) {
                for (Movie m : movies) {
                    System.out.println("- " + m.getTitle() + " (Scor: " + m.getScore() + ")");
                }
            }

            int idDeTest = 23;


            System.out.println("\n--- Testare PUT ");
            Movie updatedMovie = new Movie(null, "Avatar: Calea Apei", LocalDate.of(2022, 12, 16), 192, 7.6, null);

            restTemplate.put(BASE_URL + "/" + idDeTest, updatedMovie);
            System.out.println("Cerere PUT trimisă cu succes.");

            System.out.println("\n---Testare DELETE ");
            restTemplate.delete(BASE_URL + "/" + idDeTest);
            System.out.println("Cerere DELETE trimisă cu succes.");
            System.out.println("\n---Testare PATCH");
            Movie patchMovie = new Movie();
            patchMovie.setScore(9.9);
            try {
                restTemplate.postForEntity(BASE_URL + "/" + idDeTest, patchMovie, String.class);
            } catch (Exception e) {
                System.err.println("Eroare la PATCH: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("❌ Eroare la comunicarea cu serverul: " + e.getMessage());
            System.err.println("Asigură-te că serverul principal rulează pe portul 8081!");
        }

    }
}