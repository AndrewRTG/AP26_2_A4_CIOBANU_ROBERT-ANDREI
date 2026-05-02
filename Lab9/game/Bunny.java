package org.example.maze.game;

import org.example.maze.model.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bunny implements Runnable {
    private String name;
    private boolean running = true;
    private Game game;
    private int row, col;
    private final Random random = new Random();

    public Bunny(String name) {
        this.name = name;
    }

    public void setGame(Game game) { this.game = game; }
    public void setPosition(int row, int col) { this.row = row; this.col = col; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public void stop() { running = false; }

    @Override
    public void run() {
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        int exitRow = game.getMaze().getMatrix().length - 1;
        int exitCol = game.getMaze().getMatrix()[0].length - 1;

        while (running) {
            if (row == exitRow && col == exitCol) {
                System.out.println("🐰 " + name + " Iepurele a castigat!");
                game.stopGame();
                break;
            }

            Cell currentCell = game.getMaze().getMatrix()[row][col];
            List<int[]> availableMoves = new ArrayList<>();

            if (!currentCell.hasWallTop() && row > 0) availableMoves.add(new int[]{row - 1, col});
            if (!currentCell.hasWallBottom() && row < game.getMaze().getMatrix().length - 1) availableMoves.add(new int[]{row + 1, col});
            if (!currentCell.hasWallLeft() && col > 0) availableMoves.add(new int[]{row, col - 1});
            if (!currentCell.hasWallRight() && col < game.getMaze().getMatrix()[0].length - 1) availableMoves.add(new int[]{row, col + 1});

            if (!availableMoves.isEmpty()) {
                int[] nextMove = availableMoves.get(random.nextInt(availableMoves.size()));

                if (game.getMaze().visit(nextMove[0], nextMove[1], this)) {
                    game.getMaze().leave(row, col);
                    row = nextMove[0];
                    col = nextMove[1];
                    System.out.println("🐰 " + name + " este la pozitia (" + row + ", " + col + ")");
                }
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    public String getName() {
        return name;
    }
}