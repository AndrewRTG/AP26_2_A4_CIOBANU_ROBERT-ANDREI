package org.example.maze.ui;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ControlPanel extends JPanel {

    private final MainFrame frame;
    private JButton btnGenerate;
    private JButton btnStop;
    private JButton btnReset;
    private JButton btnExit;
    private JButton btnValidate;
    private JButton btnExport;
    private JButton btnSave;
    private JButton btnLoad;
    private JButton btnStartGame;

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 14, 10));
        setBackground(Color.DARK_GRAY);

        btnGenerate  = new JButton("Generate Perfect Maze");
        btnStop      = new JButton("Stop");
        btnReset     = new JButton("Reset");
        btnValidate  = new JButton("Validate");
        btnExport    = new JButton("Export PNG");
        btnSave      = new JButton("Save");
        btnLoad      = new JButton("Load");
        btnExit      = new JButton("Exit");
        btnStartGame = new JButton("Start Game");

        btnStop.setBackground(new Color(180, 40, 40));
        btnStop.setForeground(Color.WHITE);
        btnStop.setEnabled(false);

        btnGenerate.addActionListener(e -> {
            if (!frame.getMazePanel().isMazeInitialized()) {
                JOptionPane.showMessageDialog(frame, "Please create a grid first from the top panel.");
                return;
            }
            setAnimationMode(true);
            frame.getMazePanel().setGenerationListener((valid) -> {
                setAnimationMode(false);
                String msg = valid ? "Labirintul perfect a fost generat" : "Eroare neașteptată la generare.";
                JOptionPane.showMessageDialog(frame, msg, "Validare labirint",
                        valid ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            });
            frame.getMazePanel().generatePerfectMazeAnimated();
        });

        btnStop.addActionListener(e -> {
            frame.getMazePanel().stopAnimation();
            setAnimationMode(false);
        });

        btnReset.addActionListener(_ -> {
            if (frame.getMazePanel().isMazeInitialized()) {
                frame.getMazePanel().resetMaze();
                setAnimationMode(false);
            }
        });

        btnValidate.addActionListener(_ -> {
            if (!frame.getMazePanel().isMazeInitialized()) {
                JOptionPane.showMessageDialog(frame, "Please create the maze first."); return;
            }
            boolean ok = frame.getMazePanel().validateMaze();
            JOptionPane.showMessageDialog(frame,
                    ok ? "✔ Labirintul este valid! Drumul este marcat cu verde."
                            : "✘ Labirintul nu este valid — nu există drum de la start la finish.",
                    "Validare", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        });

        btnExport.addActionListener(_ -> {
            MazePanel mp = frame.getMazePanel();
            if (mp.isMazeInitialized()) exportMazeToPNG(mp);
            else JOptionPane.showMessageDialog(frame, "Please create a maze first!");
        });

        btnSave.addActionListener(e -> {
            if (!frame.getMazePanel().isMazeInitialized()) {
                JOptionPane.showMessageDialog(frame, "Please create a maze first!"); return;
            }
            File dir = ensureExportsDir();
            JFileChooser fc = new JFileChooser(dir);
            fc.setDialogTitle("Save Maze State");
            fc.setFileFilter(new FileNameExtensionFilter("Maze Data Files (*.maze)", "maze"));
            if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File f = ensureExtension(fc.getSelectedFile(), ".maze");
                try {
                    frame.getMazePanel().saveMaze(f);
                    JOptionPane.showMessageDialog(frame, "Maze saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnLoad.addActionListener(_ -> {
            File dir = ensureExportsDir();
            JFileChooser fc = new JFileChooser(dir);
            fc.setDialogTitle("Load Maze State");
            fc.setFileFilter(new FileNameExtensionFilter("Maze Data Files (*.maze)", "maze"));
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    frame.getMazePanel().loadMaze(fc.getSelectedFile());
                    JOptionPane.showMessageDialog(frame, "Maze loaded!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnExit.addActionListener(_ -> System.exit(0));

        btnStartGame.addActionListener(e -> {
            MazePanel mp = frame.getMazePanel();
            if (!mp.isMazeInitialized()) {
                JOptionPane.showMessageDialog(frame, "Creează un labirint mai întâi!"); return;
            }

            org.example.maze.game.Maze concurrentMaze = new org.example.maze.game.Maze(mp.getGrid());
            org.example.maze.game.Game game = new org.example.maze.game.Game(concurrentMaze);

            game.setBunny(new org.example.maze.game.Bunny("Bugs Bunny"));
            game.addRobot(new org.example.maze.game.Robot("Wall-E"));
            game.addRobot(new org.example.maze.game.Robot("R2D2"));
            game.addRobot(new org.example.maze.game.Robot("Optimus Prime"));

            frame.getCommandConsole().showConsole(game.getController());
            mp.setActiveGame(game);

            System.out.println("Pornim simularea...");
            game.start();
        });

        add(btnGenerate);
        add(btnStop);
        add(btnReset);
        add(btnValidate);
        add(btnExport);
        add(btnSave);
        add(btnLoad);
        add(btnStartGame);
        add(btnExit);
    }


    private void setAnimationMode(boolean animating) {
        btnGenerate.setEnabled(!animating);
        btnStop.setEnabled(animating);
        btnReset.setEnabled(!animating);
        btnValidate.setEnabled(!animating);
        btnSave.setEnabled(!animating);
        btnLoad.setEnabled(!animating);
        btnStartGame.setEnabled(!animating);
    }

    private File ensureExportsDir() {
        File dir = new File(System.getProperty("user.dir"), "exports");
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    private File ensureExtension(File f, String ext) {
        return f.getName().toLowerCase().endsWith(ext)
                ? f : new File(f.getParentFile(), f.getName() + ext);
    }

    private void exportMazeToPNG(MazePanel mazePanel) {
        JFileChooser fc = new JFileChooser(ensureExportsDir());
        fc.setDialogTitle("Save Maze as PNG");
        fc.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File f = ensureExtension(fc.getSelectedFile(), ".png");
            try {
                BufferedImage img = new BufferedImage(mazePanel.getWidth(), mazePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = img.createGraphics();
                mazePanel.paint(g2);
                g2.dispose();
                ImageIO.write(img, "png", f);
                JOptionPane.showMessageDialog(frame, "Saved:\n" + f.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}