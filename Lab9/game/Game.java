package org.example.maze.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final Maze maze;
    private Bunny bunny;
    private final List<Robot> robots = new ArrayList<>();
    private final SharedMemory sharedMemory = new SharedMemory();
    private final GameController controller = new GameController();
    private final Random random = new Random();
    private volatile boolean isGameRunning = false;
    private int timeLimitSeconds = 60;

    public Game(Maze maze) { this.maze = maze; }

    public void setBunny(Bunny bunny) {
        this.bunny = bunny;
        this.bunny.setGame(this);
        placeAtRandomLocation(bunny);
    }

    public void addRobot(Robot robot) {
        robots.add(robot);
        robot.setGame(this);
        placeAtRandomLocation(robot);
    }

    public Bunny getBunny()                { return bunny; }
    public Maze getMaze()                  { return maze; }
    public SharedMemory getSharedMemory()  { return sharedMemory; }
    public GameController getController()  { return controller; }
    public void setTimeLimitSeconds(int limit) { this.timeLimitSeconds = limit; }


    public synchronized void stopGame() {
        if (!isGameRunning) return;
        isGameRunning = false;
        System.out.println("\n⏹ Jocul s-a terminat. Se opresc toate thread-urile...");
        if (bunny != null) bunny.stop();
        for (Robot robot : robots) robot.stop();
        System.out.println("✅ Toate thread-urile au primit semnal de oprire.");
    }


    public void start() {
        if (bunny == null) return;
        isGameRunning = true;

        controller.register(bunny);
        controller.registerAll(robots);

        sharedMemory.setTotalRobots(robots.size());
        sharedMemory.setOnAllRobotsFinished(() -> {
            if (isGameRunning) {
                System.out.println("🔍 Toți roboții au explorat labirintul fără să prindă iepurele.");
                stopGame();
            }
        });

        Thread bunnyThread = new Thread(bunny);
        bunnyThread.setName("Thread-Bunny");
        bunnyThread.start();

        for (Robot robot : robots) {
            Thread t = new Thread(robot);
            t.setName("Thread-Robot-" + robot.getName());
            t.start();
        }

        long startTime = System.currentTimeMillis();

        Thread managerThread = new Thread(() -> {
            while (isGameRunning) {
                long currentElapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;

                if (currentElapsedSeconds >= timeLimitSeconds) {
                    System.out.println("\n⏳ TIMEOUT! Jocul a depășit limita de " + timeLimitSeconds + " secunde. Se oprește simularea...");
                    stopGame();
                    break;
                }

                System.out.println("--- Status Joc ---");
                System.out.println("Timp scurs: " + currentElapsedSeconds + "s / " + timeLimitSeconds + "s");

                if (bunny != null)
                    System.out.printf("🐰 %-15s -> (%d, %d) %s%n",
                            bunny.getName(), bunny.getRow(), bunny.getCol(),
                            bunny.isPaused() ? "[PAUZAT]" : "");
                for (Robot r : robots)
                    System.out.printf("🤖 %-15s -> (%d, %d) %s%n",
                            r.getName(), r.getRow(), r.getCol(),
                            r.isPaused() ? "[PAUZAT]" : "");

                System.out.println("📊 Celule explorate: " + sharedMemory.exploredCount());
                System.out.println("================================\n");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        managerThread.setName("Thread-Manager");
        managerThread.setDaemon(true);
        managerThread.start();
    }


    private void placeAtRandomLocation(Object entity) {
        int rows = maze.getMatrix().length;
        int cols = maze.getMatrix()[0].length;
        boolean placed = false;
        while (!placed) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            if (maze.visit(r, c, (Runnable) entity)) {
                if (entity instanceof Bunny) ((Bunny) entity).setPosition(r, c);
                else if (entity instanceof Robot) ((Robot) entity).setPosition(r, c);
                placed = true;
            }
        }
    }
    public List<Robot> getRobots() {
        return robots;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }
}