package org.example.maze.ui;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private final MainFrame frame;
    private JButton btnCreate;
    private JButton btnReset;
    private JButton btnExit;

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

        btnExit.addActionListener(e -> System.exit(0));

        add(btnCreate);
        add(btnReset);
        add(btnExit);
    }
}