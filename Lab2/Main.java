public class Main {
    public static void main(String[] args) {
        Problem problem = new Problem();

        Location iasi = new City("Iasi", 10.0, 10.0, 300000);
        Location otopeni = new Airport("Otopeni", 50.0, 50.0, 2);
        Location petrom = new GasStation("Petrom", 20.0, 20.0, 7.2);
        Location cluj = new City("Cluj", 100.0, 100.0, 320000);

        // 3. Le adăugăm în problemă
        System.out.println("--- Adăugare Locații ---");
        problem.addLocation(iasi);
        problem.addLocation(otopeni);
        problem.addLocation(petrom);
        problem.addLocation(cluj);

        Location iasiDuplicat = new City("Iasi", 10.0, 10.0, 300000);
        problem.addLocation(iasiDuplicat); // Ar trebui să zică: "Locația deja există!"


        // Drum între Iași și Petrom (Distanța euclidiană e ~14.14, deci punem lungime 15)
        Road r1 = new Road(RoadTypes.HIGHWAY, 15.0f, 130, iasi, petrom);
        // Drum între Petrom și Otopeni
        Road r2 = new Road(RoadTypes.COUNTRY, 60.0f, 90, petrom, otopeni);

        System.out.println("\n--- Adăugare Drumuri ---");
        problem.addLocation(petrom);
        problem.addRoad(r1);
        problem.addRoad(r2);

        problem.addRoad(r2);

        // 5. Verificăm dacă problema este validă geometric
        System.out.println("\n--- Validare ---");
        if (problem.valid()) {
            System.out.println("Instanța este validă!");
        } else {
            System.out.println("Instanța are erori de logică!");
        }

        // 6. Testăm DFS (Can Reach)

        System.out.println("\n--- Testare DFS (Conectivitate) ---");

        // Verificăm dacă putem ajunge de la Iași la Otopeni (via Petrom)
        boolean reach1 = problem.canGo(iasi, otopeni);
        System.out.println("Putem ajunge de la Iasi la Otopeni? " + (reach1 ? "DA" : "NU"));

        // Verificăm dacă putem ajunge de la Iași la Cluj (nu am pus drum spre Cluj)
        boolean reach2 = problem.canGo(iasi, cluj);
        System.out.println("Putem ajunge de la Iasi la Cluj? " + (reach2 ? "DA" : "NU"));

        // 7. Afișăm toată problema
        System.out.println("\n--- Starea finală a problemei ---");
        System.out.println(problem);
        // 8. Testăm algoritmul pentru "Best Route" (Dijkstra)
        System.out.println("\n--- Testare Best Route (Dijkstra) ---");

        // Adăugăm un drum alternativ între Iași și Otopeni (mai lung, dar mai rapid)
        // Distanța Iasi-Otopeni directă e ~56, punem 70km dar cu viteză de autostradă
        Road r3 = new Road(RoadTypes.HIGHWAY, 70.0f, 130, iasi, otopeni);
        problem.addRoad(r3);

        // Calculăm cea mai scurtă rută (km)
        Solution shortest = problem.getBestRoute(iasi, otopeni, false);
        System.out.println("Cea mai SCURTĂ rută:");
        System.out.println(shortest);

        // Calculăm cea mai rapidă rută (timp: t = d/v)
        Solution fastest = problem.getBestRoute(iasi, otopeni, true);
        System.out.println("\nCea mai RAPIDĂ rută:");
        System.out.println(fastest);


        // 9. Analiza de Performanță (Large Instances)
        System.out.println("\n--- Analiză Performanță (Instanță Random Mare) ---");

        Problem largeProblem = new Problem();

        System.gc();
        Runtime runtime = Runtime.getRuntime();

        long usedMemoryBefore =
                runtime.totalMemory() - runtime.freeMemory();

        long initialTime = System.currentTimeMillis();

        // Generăm 5000 locații și 15000 drumuri (Large Instance)
        largeProblem.generateRandomInstance(5000, 15000);


        // Căutăm drumul între prima locație adăugată și ultima

        Solution largeSol = largeProblem.getBestRoute(
                largeProblem.getLocations()[0],
                largeProblem.getLocations()[4999],
                false
        );

        long runningTime = System.currentTimeMillis() - initialTime;

        long usedMemoryAfter =
                runtime.totalMemory() - runtime.freeMemory();

        long memoryIncrease = usedMemoryAfter - usedMemoryBefore;

        System.out.println("Statistici Performanță:");
        System.out.println("Timp de execuție: " + runningTime + " ms");
        System.out.println("Consum Memorie: " + (memoryIncrease / 1024) + " KB");
        System.out.println("Rezultat găsit: " + (largeSol != null ? "DA" : "NU"));
    }

}