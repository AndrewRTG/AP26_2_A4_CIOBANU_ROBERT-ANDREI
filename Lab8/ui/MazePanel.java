package org.example.maze.ui;
import java.io.*;
import org.example.maze.model.Cell;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MazePanel extends JPanel {
    private Cell[][] grid;
    private int rows = 0;
    private int cols = 0;
    private List<Cell> solutionPath = null;

    public MazePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseListener( new java.awt.event.MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (grid==null) return;
                int mouseX = e.getX();
                int mouseY = e.getY();
                int spatiu=10;
                double availableWidth=getWidth()-2*spatiu;
                double availableHeight=getHeight()-2*spatiu;
                double cellSize=Math.min(availableWidth/cols,availableHeight/rows);
                double mazeWidth=cellSize*cols;
                double mazeHeight=cellSize*rows;
                double offsetX=(getWidth()-mazeWidth)/2;
                double offsetY=(getHeight()-mazeHeight)/2;
                if (mouseX<offsetX || mouseY<offsetY || mouseX>=mazeWidth+offsetX || mouseY>=mazeHeight+offsetY) {
                    return;
                }
                int column = (int) ((mouseX-offsetX)/cellSize);
                int row = (int) ((mouseY-offsetY)/cellSize); // pe ce celula suntem cu mouse ul
                double interiorX = (mouseX - offsetX) % cellSize;
                double interiorY = (mouseY - offsetY) % cellSize; // pe ce pixeli ne aflam cu mouse ul pe interior
                double distBottom = cellSize - interiorY;
                double distRight = cellSize - interiorX;
                double minDist = Math.min(Math.min(interiorY, distBottom), Math.min(interiorX, distRight));
                double clickThreshold = 5.0;
                if (minDist < clickThreshold) {
                    Cell cell= grid[row][column];
                    if (minDist==interiorY) {
                        cell.setWallTop(!cell.hasWallTop());
                        if (row > 0) grid[row - 1][column].setWallBottom(cell.hasWallTop());
                    }
                    else if (minDist==interiorX) {
                        cell.setWallLeft(!cell.hasWallLeft());
                        if (column > 0) grid[row][column-1].setWallRight(cell.hasWallLeft());

                    }
                    else if (minDist == distBottom) {
                        cell.setWallBottom(!cell.hasWallBottom());
                        if (row < rows - 1) grid[row + 1][column].setWallTop(cell.hasWallBottom());
                    }
                    else if (minDist == distRight) {
                        cell.setWallRight(!cell.hasWallRight());
                        if (column < cols - 1) grid[row][column + 1].setWallLeft(cell.hasWallRight());
                    }
                    repaint();
                }


            }
        });
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
                int y = (int) Math.round(offsetY + r * cellSize); // x si y sunt coordonatele din stanga sus a patratului
                int nextX = (int) Math.round(offsetX + (c + 1) * cellSize);
                int nextY = (int) Math.round(offsetY + (r + 1) * cellSize); // nextX si nextY sunt coordonatele din dreapta jos a patratului
                if (cell.hasWallTop()) g2d.drawLine(x, y, nextX, y); // de la stanga la dreapta pe acelasi y
                if (cell.hasWallBottom()) g2d.drawLine(x, nextY, nextX, nextY); // de la stanga la drapta pe nextY adica jos
                if (cell.hasWallLeft()) g2d.drawLine(x, y, x, nextY); // de sus in jos pe acelasi x
                if (cell.hasWallRight()) g2d.drawLine(nextX, y, nextX, nextY); // de sus in jos pe nextX adica dreapta
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
 public boolean validateMaze(){ //dfs
        solutionPath=null;
        Cell start=grid[0][0];
        Cell end=grid[rows-1][cols-1];
        boolean visited[][]=new boolean[rows][cols];
        Cell [][] parent= new Cell[rows][cols]; //nodul parinte pentru a putea sa ne intoarcem cand dam de fundatura
        Queue<Cell> queue = new LinkedList<>();
        queue.add(start);
         visited[start.getRow()][start.getCol()] = true;
         boolean drumGasit=false;
     while (!queue.isEmpty()) {
         Cell curr = queue.poll(); //cur ia valoarea de pe stiva si ii face pop
         int r = curr.getRow();
         int c = curr.getCol();

         if (curr == end) {
             drumGasit = true;
             break;
         }

        // verificam daca vecinii nu au perete si nu sunt vizitati
         // Sus
         if (!curr.hasWallTop() && r > 0 && !visited[r - 1][c]) {
             visited[r - 1][c] = true;
             parent[r - 1][c] = curr;
             queue.add(grid[r - 1][c]);
         }
         // Jos
         if (!curr.hasWallBottom() && r < rows - 1 && !visited[r + 1][c]) {
             visited[r + 1][c] = true;
             parent[r + 1][c] = curr;
             queue.add(grid[r + 1][c]);
         }
         // Stanga
         if (!curr.hasWallLeft() && c > 0 && !visited[r][c - 1]) {
             visited[r][c - 1] = true;
             parent[r][c - 1] = curr;
             queue.add(grid[r][c - 1]);
         }
         // Dreapta
         if (!curr.hasWallRight() && c < cols - 1 && !visited[r][c + 1]) {
             visited[r][c + 1] = true;
             parent[r][c + 1] = curr;
             queue.add(grid[r][c + 1]);
         }
     }
     if (drumGasit) {
         solutionPath = new ArrayList<>();
         Cell nod = end;
         while (nod != null) {
             solutionPath.add(0, nod); // adaugam drumul in lista pentru a l memora
             nod = parent[nod.getRow()][nod.getCol()]; // luam parintele nodului adaugat in lista si il adaugam si pe el si tot asa
         }
     }
     repaint();
     return drumGasit;
 }
    public void saveMaze(java.io.File file) throws java.io.IOException {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file))) {
            oos.writeObject(grid);
            oos.writeInt(rows);
            oos.writeInt(cols);
        }
    }
    public void loadMaze(java.io.File file) throws java.io.IOException, ClassNotFoundException {
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(file))) {
            grid = (Cell[][]) ois.readObject();
            rows = ois.readInt();
            cols = ois.readInt();

            solutionPath = null;
            repaint();
        }
    }


}