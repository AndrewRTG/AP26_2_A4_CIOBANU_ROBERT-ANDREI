package org.example.maze.ui;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class ControlPanel extends JPanel {
    private final MainFrame frame;
    private JButton btnCreate;
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
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        setBackground(Color.DARK_GRAY);

        btnCreate = new JButton("Randomize");
        btnReset = new JButton("Reset");
        btnExit = new JButton("Exit");
        btnValidate = new JButton( ("Validate"));
        btnExport = new JButton("Export");
        btnSave = new JButton("Save");
        btnLoad = new JButton("Load");
        btnStartGame = new JButton("Start Game");


        btnCreate.addActionListener(e -> {
            if (frame.getMazePanel().isMazeInitialized()) {
                frame.getMazePanel().generateRandomMaze();
            } else {
                JOptionPane.showMessageDialog(frame, "Please create a grid first from the top panel");
            }
        });

        btnReset.addActionListener(e -> {
            if (frame.getMazePanel().isMazeInitialized()) {
                frame.getMazePanel().resetMaze();
            }
        });
        btnValidate.addActionListener(e -> {
            if (frame.getMazePanel().isMazeInitialized()) {
                boolean isValid=frame.getMazePanel().validateMaze();
                if (isValid) {
                    JOptionPane.showMessageDialog(frame, "The maze has been validated");
                }
                else{
                    JOptionPane.showMessageDialog(frame, "The maze is not valid");
                }
            }
            else  {
                JOptionPane.showMessageDialog(frame, "Please create the maze first");
            }
        });
        btnExport.addActionListener(e -> {
            MazePanel mazePanel = frame.getMazePanel();
            if (mazePanel.isMazeInitialized()) {
                exportMazeToPNG(mazePanel);
            } else {
                JOptionPane.showMessageDialog(frame, "Please create a maze first!");
            }
        });
        btnSave.addActionListener(e -> {
            if (!frame.getMazePanel().isMazeInitialized()) {
                JOptionPane.showMessageDialog(frame, "Please create a maze first!");
                return;
            }
            File defaultDir = new File(System.getProperty("user.dir"), "exports");
            if (!defaultDir.exists()) defaultDir.mkdirs();

            JFileChooser fileChooser = new JFileChooser(defaultDir);
            fileChooser.setDialogTitle("Save Maze State");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Maze Data Files (*.maze)", "maze"));

            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".maze")) {
                    fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".maze");
                }
                try {
                    frame.getMazePanel().saveMaze(fileToSave);
                    JOptionPane.showMessageDialog(frame, "Maze state saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving maze: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnLoad.addActionListener(_ -> {
            File defaultDir = new File(System.getProperty("user.dir"), "exports");
            JFileChooser fileChooser = new JFileChooser(defaultDir);
            fileChooser.setDialogTitle("Load Maze State");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Maze Data Files (*.maze)", "maze"));

            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                try {
                    frame.getMazePanel().loadMaze(fileToLoad);
                    JOptionPane.showMessageDialog(frame, "Maze loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading maze: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnExit.addActionListener(_ -> System.exit(0));

        btnStartGame.addActionListener(e -> {
            MazePanel mazePanel = frame.getMazePanel();
            if (mazePanel.isMazeInitialized()) {
                org.example.maze.game.Maze concurrentMaze = new org.example.maze.game.Maze(mazePanel.getGrid());
                org.example.maze.game.Game game = new org.example.maze.game.Game(concurrentMaze);

                game.setBunny(new org.example.maze.game.Bunny("Bugs Bunny"));
                game.addRobot(new org.example.maze.game.Robot("Wall-E"));
                game.addRobot(new org.example.maze.game.Robot("R2D2"));
                game.addRobot(new org.example.maze.game.Robot("Optimus Prime"));

                System.out.println("Pornim simularea...");
                game.start();

            } else {
                JOptionPane.showMessageDialog(frame, "Creează un labirint mai întâi!");
            }
        });

        add(btnCreate);
        add(btnReset);
        add(btnExit);
        add(btnValidate);
        add(btnExport);
        add(btnSave);
        add(btnLoad);
        add(btnStartGame);
    }
    private void exportMazeToPNG(MazePanel mazePanel) {

        File defaultDir = new File(System.getProperty("user.dir"), "exports");

        if (!defaultDir.exists()) {
            defaultDir.mkdirs();
        }
        JFileChooser fileChooser = new JFileChooser(defaultDir);
        fileChooser.setDialogTitle("Save Maze as PNG");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".png");
            }

            try {
                BufferedImage image = new BufferedImage(
                        mazePanel.getWidth(),
                        mazePanel.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );

                Graphics2D g2d = image.createGraphics();
                mazePanel.paint(g2d);
                g2d.dispose();

                ImageIO.write(image, "png", fileToSave);

                JOptionPane.showMessageDialog(frame, "Image saved successfully at:\n" + fileToSave.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}