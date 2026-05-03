package org.example.maze.game;

import org.example.maze.model.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bunny implements Runnable, Controllable {

    private final String name;
    private volatile boolean running = true;
    private volatile boolean paused  = false;
    private final Object pauseLock   = new Object(); // used for wait/notifyAll
    private volatile int stepDelayMs = 250;          // default speed

    private Thread thread;
    private Game game;
    private int row, col;
    private final Random random = new Random();

    public Bunny(String name) { this.name = name; }

    public void setGame(Game game)              { this.game = game; }
    public void setPosition(int row, int col)   { this.row = row; this.col = col; }
    public int getRow()                         { return row; }
    public int getCol()                         { return col; }
    @Override public String getName()        { return name; }
    @Override public int    getStepDelay()   { return stepDelayMs; }
    @Override public boolean isPaused()      { return paused; }

    @Override
    public void setStepDelay(int delayMs) {
        this.stepDelayMs = Math.max(10, Math.min(2000, delayMs));
        System.out.println("🐰 " + name + " viteză setată la " + stepDelayMs + " ms/pas");
    }

    @Override
    public void pause() {
        paused = true;
        System.out.println("🐰 " + name + " a fost PAUSAT.");
    }

    @Override
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // wake up the thread blocked in checkPaused()
        }
        System.out.println("🐰 " + name + " a fost RELUAT.");
    }

    public void stop() {
        running = false;
        resume(); // unblock if paused, so the thread can exit
        if (thread != null) thread.interrupt();
    }


    @Override
    public void run() {
        thread = Thread.currentThread();
        try { Thread.sleep(100); } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); return;
        }

        int exitRow = game.getMaze().getMatrix().length - 1;
        int exitCol = game.getMaze().getMatrix()[0].length - 1;

        while (running) {
            // Block here while paused
            checkPaused();
            if (!running) break;

            // Win condition
            if (row == exitRow && col == exitCol) {
                System.out.println("🐰 " + name + " a ajuns la ieșire și a câștigat!");
                game.stopGame();
                break;
            }

            Cell currentCell = game.getMaze().getMatrix()[row][col];
            List<int[]> availableMoves = new ArrayList<>();
            if (!currentCell.hasWallTop()    && row > 0)                                      availableMoves.add(new int[]{row - 1, col});
            if (!currentCell.hasWallBottom() && row < game.getMaze().getMatrix().length - 1)  availableMoves.add(new int[]{row + 1, col});
            if (!currentCell.hasWallLeft()   && col > 0)                                      availableMoves.add(new int[]{row, col - 1});
            if (!currentCell.hasWallRight()  && col < game.getMaze().getMatrix()[0].length - 1) availableMoves.add(new int[]{row, col + 1});

            if (!availableMoves.isEmpty()) {
                int[] nextMove = availableMoves.get(random.nextInt(availableMoves.size()));
                if (game.getMaze().visit(nextMove[0], nextMove[1], this)) {
                    game.getMaze().leave(row, col);
                    row = nextMove[0];
                    col = nextMove[1];
                    System.out.println("🐰 " + name + " este la poziția (" + row + ", " + col + ")");
                }
            }

            try {
                Thread.sleep(stepDelayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }

        System.out.println("🐰 " + name + " thread terminat.");
    }

    private void checkPaused() {
        synchronized (pauseLock) {
            while (paused && running) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    running = false;
                }
            }
        }
    }
}