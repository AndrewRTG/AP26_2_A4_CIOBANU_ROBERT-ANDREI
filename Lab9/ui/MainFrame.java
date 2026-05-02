package org.example.maze.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ConfigPanel configPanel;
    private ControlPanel controlPanel;
    private MazePanel mazePanel;

    public MainFrame() {
        super("Maze Generator & Editor");
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        mazePanel = new MazePanel();
        configPanel = new ConfigPanel(this);
        controlPanel = new ControlPanel(this);

        add(configPanel, BorderLayout.NORTH);
        add(mazePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    public MazePanel getMazePanel() {
        return mazePanel;
    }
}