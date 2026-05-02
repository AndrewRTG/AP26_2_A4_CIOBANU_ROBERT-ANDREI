package org.example.maze.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final Maze maze;
    private Bunny bunny;
    private final List<Robot> robots = new ArrayList<>();
    private final SharedMemory mem = new SharedMemory();
    private final Random random = new Random();
    private boolean isGameRunning = false;

    public Game(Maze maze) {
        this.maze = maze;
    }

    public void setBunny(Bunny bunny) {
        this.bunny = bunny;
        this.bunny.setGame(this);
        placeAtRandomLocation(bunny);
    }

    public void addRobot(Robot robot) {
        this.robots.add(robot);
        robot.setGame(this);
        placeAtRandomLocation(robot);
    }

    public Bunny getBunny() {
        return bunny;
    }

    public void stopGame() {
        isGameRunning = false;
        if (bunny != null) {
            bunny.stop();
        }
        for (Robot robot : robots) {
            robot.stop();
        }
    }

    public void start() {
        if (bunny == null) {
            return;
        }
        isGameRunning = true;
        Thread bunnyThread = new Thread(bunny);
        bunnyThread.setName("Thread-Bunny");
        bunnyThread.start();

        for (Robot robot : robots) {
            Thread robotThread = new Thread(robot);
            robotThread.setName("Thread-Robot-" + robot.getName());
            robotThread.start();
        }
        Thread monitorThread = new Thread(() -> {
            while (isGameRunning) {
                if (bunny != null) {
                    System.out.println("🐰 " + bunny.getName() + " -> Rând: " + bunny.getRow() + ", Coloană: " + bunny.getCol());
                }


                for (Robot robot : robots) {
                    System.out.println("🤖 " + robot.getName() + " -> Rând: " + robot.getRow() + ", Coloană: " + robot.getCol());
                }
                System.out.println("================================\n");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        monitorThread.setName("Thread-Monitor");
        monitorThread.start();
    }

    private void placeAtRandomLocation(Object entity) {
        int rows = maze.getMatrix().length;
        int cols = maze.getMatrix()[0].length;
        boolean placed = false;

        while (!placed) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);

            if (maze.visit(r, c, (Runnable) entity)) {
                if (entity instanceof Bunny) {
                    ((Bunny) entity).setPosition(r, c);
                } else if (entity instanceof Robot) {
                    ((Robot) entity).setPosition(r, c);
                }
                placed = true;
            }
        }
    }

    public Maze getMaze() {
        return maze;
    }

    public SharedMemory getMem() {
        return mem;
    }
}