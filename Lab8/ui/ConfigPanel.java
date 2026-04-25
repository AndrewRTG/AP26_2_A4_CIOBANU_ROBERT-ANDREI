package org.example.maze.ui;

import javax.swing.*;

public class ConfigPanel extends JPanel {
    private final MainFrame frame;
    private JSpinner rowsSpinner;
    private JSpinner colsSpinner;
    private JButton createBtn;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        JLabel rowsLabel = new JLabel("Rows:");
        JLabel colsLabel = new JLabel("Cols:");
        rowsSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 100, 1));
        colsSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 100, 1));
        createBtn = new JButton("Create Maze");
        createBtn.addActionListener(e -> {
            int rows = (Integer) rowsSpinner.getValue();
            int cols = (Integer) colsSpinner.getValue();
            frame.getMazePanel().initMaze(rows, cols);
        });
        add(rowsLabel);
        add(rowsSpinner);
        add(colsLabel);
        add(colsSpinner);
        add(createBtn);
    }
}