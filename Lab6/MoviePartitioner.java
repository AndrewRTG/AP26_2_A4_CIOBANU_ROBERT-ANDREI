import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

//problema de colorare dupa cerinta
public class MoviePartitioner {

    public List<List<Movie>> partitionMovies(List<Movie> movies) {
        int n = movies.size();
        if (n == 0) return new ArrayList<>();
// facem matricea de adiacenta ca sa construim graful
        boolean[][] related = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (shareActor(movies.get(i), movies.get(j))) {
                    related[i][j] = true;
                    related[j][i] = true;
                }
            }
        }

        //  Cautam cel mai mic numar de liste
        for (int k = 1; k <= n; k++) {
            Model model = new Model("Cel mai mic numar de liste=" + k);

            // Fiecare film primeste o culoare de la 0 la k-1
            IntVar[] listAssignments = model.intVarArray("movies", n, 0, k - 1);

            // cele care au aceeasi culoare trebuie sa fie in liste diferite
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (related[i][j]) {
                        model.arithm(listAssignments[i], "!=", listAssignments[j]).post();
                    }
                }
            }

            // Impartim listele astfel incat sa fie egale sau diferenta sa fie de 1
            // Dacă împărțim N filme la K liste, o listă poate avea minim N/K și maxim N/K filme.
            int minSize = n / k;
            int maxSize = (int) Math.ceil((double) n / k);

            for (int c = 0; c < k; c++) {
                IntVar countVar = model.intVar("countList_" + c, minSize, maxSize);
                model.count(c, listAssignments, countVar).post();
            }
            if (model.getSolver().solve()) {
                System.out.println("S-a găsit soluția optimă cu " + k + " liste!");
                return buildResult(movies, listAssignments, k);
            }
        }

        return new ArrayList<>();
    }


    private boolean shareActor(Movie m1, Movie m2) {
        for (Actor a1 : m1.getActors()) {
            for (Actor a2 : m2.getActors()) {
                if (a1.getName().equals(a2.getName())) {
                    return true;
                }
            }
        }
        return false;
    }


    private List<List<Movie>> buildResult(List<Movie> movies, IntVar[] assignments, int k) {
        List<List<Movie>> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            result.add(new ArrayList<>());
        }

        for (int i = 0; i < movies.size(); i++) {
            int listId = assignments[i].getValue();
            result.get(listId).add(movies.get(i));
        }
        return result;
    }
}