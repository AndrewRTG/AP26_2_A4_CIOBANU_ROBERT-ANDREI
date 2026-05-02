package org.example.maze.model;

import java.awt.event.ActionListener;
import java.io.Serializable;
public class Cell implements Serializable {
    private int row, col;
    private static final long serialVersionUID = 1L;
    private boolean wallTop = true;
    private boolean wallRight = true;
    private boolean wallBottom = true;
    private boolean wallLeft = true;
    private boolean occupied = false;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean hasWallTop() { return wallTop; }
    public boolean hasWallRight() { return wallRight; }
    public boolean hasWallBottom() { return wallBottom; }
    public boolean hasWallLeft() { return wallLeft; }
    public void setWallTop(boolean wallTop) { this.wallTop = wallTop; }
    public void setWallRight(boolean wallRight) { this.wallRight = wallRight; }
    public void setWallBottom(boolean wallBottom) { this.wallBottom = wallBottom; }
    public void setWallLeft(boolean wallLeft) { this.wallLeft = wallLeft; }
    public boolean isOccupied() {return occupied; }
    public void setOccupied(boolean occupied) {this.occupied = occupied; }
}