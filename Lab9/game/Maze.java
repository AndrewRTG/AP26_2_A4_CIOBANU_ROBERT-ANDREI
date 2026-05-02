package org.example.maze.game;

import org.example.maze.model.Cell;

public class Maze {
    private final Cell[][] matrix;

    public Maze(Cell[][] matrix) {
        this.matrix = matrix;
    }

    public boolean visit(int row, int col, Runnable entity) {
        Cell targetCell = matrix[row][col];
        //blocam accesul la aceasta celula pentru a face mutual exclusion sa ne asiguram ca 2 thread uri nu modifica aceleasi date in acelasi timp
        synchronized (targetCell) {
            if (!targetCell.isOccupied()) {
                targetCell.setOccupied(true);
                System.out.println(Thread.currentThread().getName() + " a ocupat celula (" + row + ", " + col + ")");
                return true;
            } else {
                return false; // celula e ocupata de altcineva
            }
        }
    }

    public void leave(int row, int col) {
        Cell targetCell = matrix[row][col];
        synchronized (targetCell) {
            targetCell.setOccupied(false);
        }
    }

    public Cell[][] getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        return "Maze with dimensions: " + matrix.length + "x" + matrix[0].length;
    }
}