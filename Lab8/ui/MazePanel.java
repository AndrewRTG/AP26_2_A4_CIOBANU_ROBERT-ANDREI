package org.example.maze.ui;

import org.example.maze.model.Cell;
import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    private Cell[][] grid;
    private int rows = 0;
    private int cols = 0;

    public MazePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    public void initMaze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c);
            }
        }
        repaint();
    }
    public boolean isMazeInitialized() {
        return grid != null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grid == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));

        int spatiu = 10;//spatiu fata de panel uri


        double availableWidth = getWidth() - 2 * spatiu;
        double availableHeight = getHeight() - 2 * spatiu;
        double cellSize = Math.min(availableWidth / cols, availableHeight / rows);


        double mazeWidth = cellSize * cols;
        double mazeHeight = cellSize * rows;
        double offsetX = (getWidth() - mazeWidth) / 2.0;
        double offsetY = (getHeight() - mazeHeight) / 2.0;


        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];


                int x = (int) Math.round(offsetX + c * cellSize);
                int y = (int) Math.round(offsetY + r * cellSize);
                int nextX = (int) Math.round(offsetX + (c + 1) * cellSize);
                int nextY = (int) Math.round(offsetY + (r + 1) * cellSize);
                if (cell.hasWallTop()) g2d.drawLine(x, y, nextX, y);
                if (cell.hasWallBottom()) g2d.drawLine(x, nextY, nextX, nextY);
                if (cell.hasWallLeft()) g2d.drawLine(x, y, x, nextY);
                if (cell.hasWallRight()) g2d.drawLine(nextX, y, nextX, nextY);
            }
        }
    }
    public void resetMaze() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c].setWallTop(true);
                grid[r][c].setWallBottom(true);
                grid[r][c].setWallLeft(true);
                grid[r][c].setWallRight(true);
            }
        }
        repaint();
    }
    public void generateRandomMaze() {

        resetMaze();

        java.util.Random rand = new java.util.Random();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r < rows - 1 && rand.nextDouble() < 0.4) {
                    grid[r][c].setWallBottom(false);
                    grid[r + 1][c].setWallTop(false);
                }
                if (c < cols - 1 && rand.nextDouble() < 0.4) {
                    grid[r][c].setWallRight(false);
                    grid[r][c + 1].setWallLeft(false);
                }
            }
        }
        repaint();
    }
}