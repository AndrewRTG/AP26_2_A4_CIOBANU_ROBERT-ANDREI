package org.example.maze.ui;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel {

    private final MainFrame frame;
    private JSpinner rowsSpinner;
    private JSpinner colsSpinner;
    private JButton createBtn;
    private JSlider speedSlider;
    private JLabel speedLabel;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 16, 8));
        setBackground(new Color(45, 45, 48));


        JLabel rowsLabel = new JLabel("Rows:");
        JLabel colsLabel = new JLabel("Cols:");
        rowsLabel.setForeground(Color.LIGHT_GRAY);
        colsLabel.setForeground(Color.LIGHT_GRAY);

        rowsSpinner = new JSpinner(new SpinnerNumberModel(15, 5, 100, 1));
        colsSpinner = new JSpinner(new SpinnerNumberModel(15, 5, 100, 1));
        rowsSpinner.setPreferredSize(new Dimension(60, 26));
        colsSpinner.setPreferredSize(new Dimension(60, 26));

        createBtn = new JButton("Create Grid");
        createBtn.addActionListener(e -> {
            int rows = (Integer) rowsSpinner.getValue();
            int cols = (Integer) colsSpinner.getValue();
            frame.getMazePanel().initMaze(rows, cols);
        });

        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(2, 28));
        sep.setForeground(Color.GRAY);

        speedLabel = new JLabel("Speed: 50%");
        speedLabel.setForeground(Color.LIGHT_GRAY);
        speedLabel.setPreferredSize(new Dimension(90, 20));

        speedSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 100, 50);
        speedSlider.setPreferredSize(new Dimension(160, 26));
        speedSlider.setBackground(new Color(45, 45, 48));
        speedSlider.setFocusable(false);
        speedSlider.addChangeListener(e -> {
            int val = speedSlider.getValue();
            speedLabel.setText("Speed: " + val + "%");
            frame.getMazePanel().setSpeed(val);
        });

        frame.getMazePanel().setSpeed(50);

        add(rowsLabel);
        add(rowsSpinner);
        add(colsLabel);
        add(colsSpinner);
        add(createBtn);
        add(sep);
        add(speedLabel);
        add(speedSlider);
    }
}