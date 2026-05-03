package org.example.maze.game;

import org.example.maze.model.Cell;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Robot implements Runnable, Controllable {

    private final String name;
    private volatile boolean running  = true;
    private volatile boolean paused   = false;
    private final Object pauseLock    = new Object();
    private volatile int stepDelayMs  = 300; // default step delay

    private static final int BACKTRACK_DELAY_DIVISOR = 2; // backtrack is 2× faster than advance
    private static final int IDLE_DELAY_MS = 500;

    private Thread thread;
    private Game game;
    private int row, col;

    private final Deque<int[]> explorationStack = new ArrayDeque<>();
    private boolean explorationFinished = false;

    public Robot(String name) { this.name = name; }

    public void setGame(Game game)            { this.game = game; }
    public void setPosition(int row, int col) { this.row = row; this.col = col; }
    public int getRow()                       { return row; }
    public int getCol()                       { return col; }


    @Override public String  getName()      { return name; }
    @Override public int     getStepDelay() { return stepDelayMs; }
    @Override public boolean isPaused()     { return paused; }

    @Override
    public void setStepDelay(int delayMs) {
        this.stepDelayMs = Math.max(10, Math.min(2000, delayMs));
        System.out.println("🤖 " + name + " viteză setată la " + stepDelayMs + " ms/pas");
    }

    @Override
    public void pause() {
        paused = true;
        System.out.println("🤖 " + name + " a fost PAUSAT.");
    }

    @Override
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
        System.out.println("🤖 " + name + " a fost RELUAT.");
    }

    public void stop() {
        running = false;
        resume(); // unblock if paused so the thread can exit cleanly
        if (thread != null) thread.interrupt();
    }


    @Override
    public void run() {
        thread = Thread.currentThread();

        try { Thread.sleep(100); } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); return;
        }

        game.getSharedMemory().claimCell(row, col);
        explorationStack.push(new int[]{row, col});
        System.out.println("🤖 " + name + " Explorarea a inceput (" + row + ", " + col + ")");

        while (running) {
            // Block here while paused
            checkPaused();
            if (!running) break;

            boolean didAdvance = false;

            if (!explorationFinished) {
                didAdvance = tryAdvance();

                if (!didAdvance) {
                    boolean didBacktrack = tryBacktrack();
                    if (!didBacktrack) {
                        explorationFinished = true;
                        game.getSharedMemory().robotFinished(name);
                    }
                }
            }

            checkCaughtBunny();

            int delay = didAdvance
                    ? stepDelayMs
                    : (explorationFinished ? IDLE_DELAY_MS : stepDelayMs / BACKTRACK_DELAY_DIVISOR);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }

        System.out.println("🤖 " + name + " thread terminat.");
    }


    private boolean tryAdvance() {
        for (int[] neighbour : getUnexploredPassableNeighbours(row, col)) {
            int nextRow = neighbour[0], nextCol = neighbour[1];
            if (!game.getSharedMemory().claimCell(nextRow, nextCol)) continue;
            if (game.getMaze().visit(nextRow, nextCol, this)) {
                game.getMaze().leave(row, col);
                row = nextRow;
                col = nextCol;
                explorationStack.push(new int[]{row, col});
                System.out.println("🤖 " + name + " explorat (" + row + ", " + col + ")");
                game.getSharedMemory().addInfo(name + " a explorat celula (" + row + ", " + col + ")");
                return true;
            }
        }
        return false;
    }

    private boolean tryBacktrack() {
        if (explorationStack.isEmpty()) return false;
        explorationStack.pop();
        if (explorationStack.isEmpty()) return false;

        int[] previous = explorationStack.peek();
        int prevRow = previous[0], prevCol = previous[1];

        if (game.getMaze().visit(prevRow, prevCol, this)) {
            game.getMaze().leave(row, col);
            row = prevRow;
            col = prevCol;
            System.out.println("🤖 " + name + " backtrack → (" + row + ", " + col + ")");
        }
        return true;
    }

    private List<int[]> getUnexploredPassableNeighbours(int r, int c) {
        Cell cell = game.getMaze().getMatrix()[r][c];
        int totalRows = game.getMaze().getMatrix().length;
        int totalCols = game.getMaze().getMatrix()[0].length;
        List<int[]> result = new ArrayList<>();
        if (!cell.hasWallTop()    && r > 0           && !game.getSharedMemory().isExplored(r-1, c)) result.add(new int[]{r-1, c});
        if (!cell.hasWallBottom() && r < totalRows-1 && !game.getSharedMemory().isExplored(r+1, c)) result.add(new int[]{r+1, c});
        if (!cell.hasWallLeft()   && c > 0           && !game.getSharedMemory().isExplored(r, c-1)) result.add(new int[]{r, c-1});
        if (!cell.hasWallRight()  && c < totalCols-1 && !game.getSharedMemory().isExplored(r, c+1)) result.add(new int[]{r, c+1});
        return result;
    }


    private void checkCaughtBunny() {
        Bunny bunny = game.getBunny();
        if (bunny != null && running && row == bunny.getRow() && col == bunny.getCol()) {
            System.out.println("🚨 " + name + " A PRINS IEPURAȘUL la (" + row + ", " + col + ")!");
            game.stopGame();
        }
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