package com.example.demo.solver;

import com.example.demo.entitati.Actor;
import com.example.demo.entitati.Movie;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;

public class IndependentMovieListService {

    public List<Movie> findIndependentMovies(List<Movie> allMovies, int minMovies) {
        int size = allMovies.size();
        if (size < minMovies) return new ArrayList<>();

        boolean[][] related = new boolean[size][size];
        for (int nod1 = 0; nod1 < size; nod1++) {
            for (int nod2 = nod1 + 1; nod2 < size; nod2++) {
                if (shareActor(allMovies.get(nod1), allMovies.get(nod2))) {
                    related[nod1][nod2] = true;
                    related[nod2][nod1] = true;
                }
            }
        }

        Model model = new Model("Independent Movie Set");


        BoolVar[] selectedMovies = model.boolVarArray("selected_movies", size);


        IntVar totalSelected = model.intVar("total_selected", minMovies, size);
        model.sum(selectedMovies, "=", totalSelected).post();


        for (int nod1 = 0; nod1 < size; nod1++) {
            for (int nod2 = nod1 + 1; nod2 < size; nod2++) {
                if (related[nod1][nod2]) {
                    model.arithm(selectedMovies[nod1], "+", selectedMovies[nod2], "<=", 1).post();
                }
            }
        }

        System.out.println("Căutăm o listă cu minim " + minMovies + " filme independente...");

        if (model.getSolver().solve()) {
            System.out.println("Soluție găsită! Lista conține " + totalSelected.getValue() + " filme.");

            List<Movie> independentList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (selectedMovies[i].getValue() == 1) {
                    independentList.add(allMovies.get(i));
                }
            }
            return independentList;
        } else {
            System.out.println("Nu s-a putut găsi nicio listă de " + minMovies + " filme complet independente.");
            return new ArrayList<>();
        }
    }

    private boolean shareActor(Movie m1, Movie m2) {
        if (m1.getActors() == null || m2.getActors() == null) return false;

        for (Actor a1 : m1.getActors()) {
            for (Actor a2 : m2.getActors()) {
                if (a1.getName().equals(a2.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}