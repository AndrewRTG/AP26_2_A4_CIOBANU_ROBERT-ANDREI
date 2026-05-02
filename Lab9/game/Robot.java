package org.example.maze.game;

import org.example.maze.model.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Robot implements Runnable {
    private String name;
    private boolean running = true;
    private Game game;
    private int row, col;
    private final Random random = new Random();

    public Robot(String name) {
        this.name = name;
    }

    public void setGame(Game game) { this.game = game; }
    public void setPosition(int row, int col) { this.row = row; this.col = col; }
    public String getName() { return name; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public void stop() { running = false; }

    @Override
    public void run() {
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        while (running) {
            Cell currentCell = game.getMaze().getMatrix()[row][col];
            List<int[]> availableMoves = new ArrayList<>();

            if (!currentCell.hasWallTop() && row > 0) availableMoves.add(new int[]{row - 1, col});
            if (!currentCell.hasWallBottom() && row < game.getMaze().getMatrix().length - 1) availableMoves.add(new int[]{row + 1, col});
            if (!currentCell.hasWallLeft() && col > 0) availableMoves.add(new int[]{row, col - 1});
            if (!currentCell.hasWallRight() && col < game.getMaze().getMatrix()[0].length - 1) availableMoves.add(new int[]{row, col + 1});

            if (!availableMoves.isEmpty()) {
                int[] nextMove = availableMoves.get(random.nextInt(availableMoves.size()));
                int nextRow = nextMove[0];
                int nextCol = nextMove[1];

                if (game.getMaze().visit(nextRow, nextCol, this)) {
                    game.getMaze().leave(row, col);
                    row = nextRow;
                    col = nextCol;
                    String memoryLog = name + " a explorat celula (" + row + ", " + col + ")";
                    game.getMem().addInfo(memoryLog);
                    if (game.getBunny() != null && row == game.getBunny().getRow() && col == game.getBunny().getCol()) {
                        System.out.println("🚨 " + name + " A PRINS IEPURAȘUL! Jocul s-a terminat.");
                        game.stopGame();
                    }
                }
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }
}