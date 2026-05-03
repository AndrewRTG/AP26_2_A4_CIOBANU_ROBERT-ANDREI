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
    private boolean[][] visited;
    private boolean[][] onStack;
    private Cell currentCell;
    private Deque<Cell> dfsStack;
    private Random rand;
    private javax.swing.Timer animTimer;
    private boolean animating = false;
    private int animDelayMs = 30;
    private org.example.maze.game.Game activeGame;
    private javax.swing.Timer gameRenderTimer;

    private static final Color COL_WALL     = Color.WHITE;
    private static final Color COL_VISITED  = new Color(25, 55, 95);       // dark navy
    private static final Color COL_FRONTIER = new Color(0, 130, 210, 180); // bright blue (stack)
    private static final Color COL_CURRENT  = new Color(255, 215, 0);      // pentru nod curent
    private static final Color COL_PATH     = new Color(0, 200, 90, 190);  // solutie
    private static final Color COL_START    = new Color(30, 120, 255);     // start
    private static final Color COL_END      = new Color(220, 50, 50);      // finish

    private static final Color COL_BUNNY   = new Color(255, 105, 180); // Roz aprins
    private static final Color COL_ROBOT   = new Color(255, 69, 0);    // Roșu-Portocaliu

    public interface GenerationListener {
        void onGenerationFinished(boolean valid);
    }
    private GenerationListener generationListener;
    public void setGenerationListener(GenerationListener l) { this.generationListener = l; }


    public MazePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (grid == null || animating) return; // nu putem modifica maze ul cat timp este animat
                int mouseX = e.getX(), mouseY = e.getY();
                int padding = 10;
                double availableWidth  = getWidth()  - 2 * padding;
                double availableHeight = getHeight() - 2 * padding;
                double cellSize = Math.min(availableWidth / cols, availableHeight / rows);
                double mazeWidth  = cellSize * cols;
                double mazeHeight = cellSize * rows;
                double mazeOffsetX = (getWidth()  - mazeWidth)  / 2.0;
                double mazeOffsetY = (getHeight() - mazeHeight) / 2.0;
                if (mouseX < mazeOffsetX || mouseY < mazeOffsetY
                        || mouseX >= mazeWidth  + mazeOffsetX
                        || mouseY >= mazeHeight + mazeOffsetY) return;
                int clickedCol = (int) ((mouseX - mazeOffsetX) / cellSize);
                int clickedRow = (int) ((mouseY - mazeOffsetY) / cellSize);
                double distFromTopEdge    = (mouseY - mazeOffsetY) % cellSize;
                double distFromLeftEdge   = (mouseX - mazeOffsetX) % cellSize;
                double distFromBottomEdge = cellSize - distFromTopEdge;
                double distFromRightEdge  = cellSize - distFromLeftEdge;
                double distToNearestWall = Math.min(
                        Math.min(distFromTopEdge, distFromBottomEdge),
                        Math.min(distFromLeftEdge, distFromRightEdge));
                double clickThreshold = 5.0;
                if (distToNearestWall < clickThreshold) {
                    Cell clickedCell = grid[clickedRow][clickedCol];
                    if (distToNearestWall == distFromTopEdge) {
                        clickedCell.setWallTop(!clickedCell.hasWallTop());
                        if (clickedRow > 0)
                            grid[clickedRow - 1][clickedCol].setWallBottom(clickedCell.hasWallTop());
                    } else if (distToNearestWall == distFromLeftEdge) {
                        clickedCell.setWallLeft(!clickedCell.hasWallLeft());
                        if (clickedCol > 0)
                            grid[clickedRow][clickedCol - 1].setWallRight(clickedCell.hasWallLeft());
                    } else if (distToNearestWall == distFromBottomEdge) {
                        clickedCell.setWallBottom(!clickedCell.hasWallBottom());
                        if (clickedRow < rows - 1)
                            grid[clickedRow + 1][clickedCol].setWallTop(clickedCell.hasWallBottom());
                    } else if (distToNearestWall == distFromRightEdge) {
                        clickedCell.setWallRight(!clickedCell.hasWallRight());
                        if (clickedCol < cols - 1)
                            grid[clickedRow][clickedCol + 1].setWallLeft(clickedCell.hasWallRight());
                    }
                    solutionPath = null;
                    repaint();
                }
            }
        });
    }


    public void initMaze(int rows, int cols) {
        stopAnimation();
        this.rows = rows; this.cols = cols;
        grid = new Cell[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Cell(r, c);
        solutionPath = null;
        visited = null; onStack = null; currentCell = null;
        repaint();
    }

    public boolean isMazeInitialized() { return grid != null; }
    public boolean isAnimating()        { return animating; }

    /**
     * Converts a speed percentage (1–100) to a timer delay.
     *   1%   → 500 ms per step  (very slow, educational)
     *  50%   →  30 ms per step  (comfortable default)
     * 100%   →   1 ms per step  (as fast as Swing allows)
     */
    public void setSpeed(int speedPercent) {
        animDelayMs = Math.max(1, 500 - (int)(499.0 * speedPercent / 100.0));
        if (animTimer != null && animTimer.isRunning())
            animTimer.setDelay(animDelayMs);
    }


    public void generatePerfectMazeAnimated() {
        stopAnimation();
        resetMaze();
        solutionPath = null;

        visited  = new boolean[rows][cols];
        onStack  = new boolean[rows][cols];
        rand     = new Random();
        dfsStack = new ArrayDeque<>();

        visited[0][0] = true;
        onStack[0][0] = true;
        dfsStack.push(grid[0][0]);
        currentCell = grid[0][0];
        animating = true;

        animTimer = new javax.swing.Timer(animDelayMs, e -> animStep());
        animTimer.start();
    }

    private void animStep() {
        if (dfsStack.isEmpty()) {
            finishAnimation();
            return;
        }

        currentCell = dfsStack.peek();
        int r = currentCell.getRow();
        int  c = currentCell.getCol();

        List<int[]> nbrs = new ArrayList<>();
        if (r > 0        && !visited[r-1][c]) nbrs.add(new int[]{r-1, c, 0}); // dir N ->0
        if (r < rows - 1 && !visited[r+1][c]) nbrs.add(new int[]{r+1, c, 1}); // dir S ->1
        if (c > 0        && !visited[r][c-1]) nbrs.add(new int[]{r,  c-1, 2}); // dir W ->2
        if (c < cols - 1 && !visited[r][c+1]) nbrs.add(new int[]{r,  c+1, 3}); // dir E ->3

        if (!nbrs.isEmpty()) {
            int[] ch = nbrs.get(rand.nextInt(nbrs.size()));
            int randnou = ch[0], coloananoua = ch[1], directie = ch[2];
            switch (directie) {
                case 0: currentCell.setWallTop(false);    grid[randnou][coloananoua].setWallBottom(false); break;
                case 1: currentCell.setWallBottom(false); grid[randnou][coloananoua].setWallTop(false);    break;
                case 2: currentCell.setWallLeft(false);   grid[randnou][coloananoua].setWallRight(false);  break;
                case 3: currentCell.setWallRight(false);  grid[randnou][coloananoua].setWallLeft(false);   break;
            }
            visited[randnou][coloananoua] = true;
            onStack[randnou][coloananoua] = true;
            dfsStack.push(grid[randnou][coloananoua]);
        } else {
            onStack[r][c] = false;
            dfsStack.pop();
        }

        repaint();
    }

    private void finishAnimation() {
        animTimer.stop();
        animating = false;
        currentCell = null;
        visited = null; onStack = null;
        repaint();

        boolean valid = validateMaze(); // cand se termina de animat verifica daca e valid

        if (generationListener != null) {
            generationListener.onGenerationFinished(valid);
        } else {
            String msg = valid ? "Labirintul a fost perfect generat" : " A fost o eroare de generare";

            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), msg,
                    "Validare", valid ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        }
    }

    public void stopAnimation() {
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();
        animating = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grid == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int spatiu = 10;
        double aw = getWidth() - 2 * spatiu, ah = getHeight() - 2 * spatiu;
        double cs = Math.min(aw / cols, ah / rows);
        double mw = cs * cols, mh = cs * rows;
        double ox = (getWidth() - mw) / 2.0, oy = (getHeight() - mh) / 2.0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = (int) Math.round(ox + c * cs);
                int y = (int) Math.round(oy + r * cs);
                int w = (int) Math.round(ox + (c+1)*cs) - x;
                int h = (int) Math.round(oy + (r+1)*cs) - y;
                Color fill = null;

                if (animating && visited != null) {
                    if (currentCell != null && grid[r][c] == currentCell)
                        fill = COL_CURRENT;
                    else if (onStack != null && onStack[r][c])
                        fill = COL_FRONTIER;
                    else if (visited[r][c])
                        fill = COL_VISITED;
                } else if (solutionPath != null) {
                    if (r == 0 && c == 0)               fill = COL_START;
                    else if (r == rows-1 && c == cols-1) fill = COL_END;
                    else {
                        for (Cell sc : solutionPath)
                            if (sc.getRow() == r && sc.getCol() == c) { fill = COL_PATH; break; }
                    }
                }
                if (activeGame != null) {
                    for (org.example.maze.game.Robot robot : activeGame.getRobots()) {
                        if (robot.getRow() == r && robot.getCol() == c) {
                            fill = COL_ROBOT;
                            break;
                        }
                    }
                    org.example.maze.game.Bunny bunny = activeGame.getBunny();
                    if (bunny != null && bunny.getRow() == r && bunny.getCol() == c) {
                        fill = COL_BUNNY;
                    }
                }

                if (fill != null) {
                    g2d.setColor(fill);
                    g2d.fillRect(x + 1, y + 1, w - 1, h - 1);
                }
            }
        }

        g2d.setColor(COL_WALL);
        g2d.setStroke(new BasicStroke(2));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                int x  = (int) Math.round(ox + c       * cs);
                int y  = (int) Math.round(oy + r       * cs);
                int nx = (int) Math.round(ox + (c + 1) * cs);
                int ny = (int) Math.round(oy + (r + 1) * cs);
                if (cell.hasWallTop())    g2d.drawLine(x,  y,  nx, y);
                if (cell.hasWallBottom()) g2d.drawLine(x,  ny, nx, ny);
                if (cell.hasWallLeft())   g2d.drawLine(x,  y,  x,  ny);
                if (cell.hasWallRight())  g2d.drawLine(nx, y,  nx, ny);
            }
        }
    }

    public void resetMaze() {
        stopAnimation();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                grid[r][c].setWallTop(true);    grid[r][c].setWallBottom(true);
                grid[r][c].setWallLeft(true);   grid[r][c].setWallRight(true);
            }
        solutionPath = null;
        visited = null; onStack = null; currentCell = null;
        repaint();
    }

    public void generateRandomMaze() {
        stopAnimation();
        resetMaze();
        Random rnd = new Random();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                if (r < rows-1 && rnd.nextDouble() < 0.4) { grid[r][c].setWallBottom(false); grid[r+1][c].setWallTop(false); }
                if (c < cols-1 && rnd.nextDouble() < 0.4) { grid[r][c].setWallRight(false);  grid[r][c+1].setWallLeft(false); }
            }
        repaint();
    }

    public boolean validateMaze() {
        solutionPath = null;
        Cell start = grid[0][0], end = grid[rows-1][cols-1];
        boolean[][] vis = new boolean[rows][cols];
        Cell[][] par    = new Cell[rows][cols];
        Queue<Cell> q   = new LinkedList<>();
        q.add(start); vis[0][0] = true;
        boolean found = false;
        while (!q.isEmpty()) {
            Cell cur = q.poll();
            int r = cur.getRow(), c = cur.getCol();
            if (cur == end) { found = true; break; }
            if (!cur.hasWallTop()    && r > 0      && !vis[r-1][c]) { vis[r-1][c]=true; par[r-1][c]=cur; q.add(grid[r-1][c]); }
            if (!cur.hasWallBottom() && r < rows-1 && !vis[r+1][c]) { vis[r+1][c]=true; par[r+1][c]=cur; q.add(grid[r+1][c]); }
            if (!cur.hasWallLeft()   && c > 0      && !vis[r][c-1]) { vis[r][c-1]=true; par[r][c-1]=cur; q.add(grid[r][c-1]); }
            if (!cur.hasWallRight()  && c < cols-1 && !vis[r][c+1]) { vis[r][c+1]=true; par[r][c+1]=cur; q.add(grid[r][c+1]); }
        }
        if (found) {
            solutionPath = new ArrayList<>();
            for (Cell n = end; n != null; n = par[n.getRow()][n.getCol()])
                solutionPath.add(0, n);
        }
        repaint();
        return found;
    }

    public void saveMaze(java.io.File file) throws java.io.IOException {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file))) {
            oos.writeObject(grid); oos.writeInt(rows); oos.writeInt(cols);
        }
    }

    public void loadMaze(java.io.File file) throws java.io.IOException, ClassNotFoundException {
        stopAnimation();
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(file))) {
            grid = (Cell[][]) ois.readObject(); rows = ois.readInt(); cols = ois.readInt();
            solutionPath = null; repaint();
        }
    }
    public void setActiveGame(org.example.maze.game.Game game) {
        this.activeGame = game;
        if (gameRenderTimer != null && gameRenderTimer.isRunning()) {
            gameRenderTimer.stop();
        }
        gameRenderTimer = new javax.swing.Timer(30, e -> {
            if (activeGame != null) {
                repaint();
                if (!activeGame.isGameRunning()) {
                    ((javax.swing.Timer) e.getSource()).stop();
                }
            }
        });
        gameRenderTimer.start();
    }


    public Cell[][] getGrid() { return grid; }

}