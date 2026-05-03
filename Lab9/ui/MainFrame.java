package org.example.maze.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private ConfigPanel    configPanel;
    private ControlPanel   controlPanel;
    private MazePanel      mazePanel;
    private CommandConsole commandConsole;

    public MainFrame() {
        super("Maze Generator & Editor");
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 680);
        setLayout(new BorderLayout());

        mazePanel      = new MazePanel();
        configPanel    = new ConfigPanel(this);
        controlPanel   = new ControlPanel(this);
        commandConsole = new CommandConsole();

        add(configPanel,    BorderLayout.NORTH);
        add(mazePanel,      BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(controlPanel,   BorderLayout.NORTH);
        southPanel.add(commandConsole, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    public MazePanel      getMazePanel()      { return mazePanel; }
    public CommandConsole getCommandConsole() { return commandConsole; }
}