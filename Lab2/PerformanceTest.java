public class PerformanceTest {
    public static void main(String[] args) {
        Problem problem = new Problem();

        // Pregătire măsurători conform imaginii tale
        System.gc();
        Runtime runtime = Runtime.getRuntime();

        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long initialTime = System.currentTimeMillis();

        // Generăm o instanță mare: 5.000 locații, 15.000 drumuri
        problem.generateRandomInstance(5000, 15000);

        // Executăm algoritmul Dijkstra (test.run())
        if (problem.valid()) {
            Solution sol = problem.getBestRoute(problem.getLocations()[0], problem.getLocations()[4999], false);
        }

        long runningTime = System.currentTimeMillis() - initialTime;
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = usedMemoryAfter - usedMemoryBefore;

        System.out.println("Running Time: " + runningTime + " ms");
        System.out.println("Memory Consumption: " + (memoryIncrease / 1024) + " KB");
    }
}