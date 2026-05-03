package org.example.maze.ui;

import org.example.maze.game.Controllable;
import org.example.maze.game.GameController;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class CommandConsole extends JPanel {

    private final JTextArea outputArea;
    private final JTextField inputField;
    private GameController controller;

    private final List<String> history = new ArrayList<>();
    private int historyIndex = -1;

    public CommandConsole() {
        setLayout(new BorderLayout(4, 4));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(80, 80, 80)));

        outputArea = new JTextArea(5, 60);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(20, 20, 20));
        outputArea.setForeground(new Color(180, 255, 180));
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        JPanel inputRow = new JPanel(new BorderLayout(4, 0));
        inputRow.setBackground(new Color(30, 30, 30));
        inputRow.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));

        JLabel prompt = new JLabel("▶");
        prompt.setForeground(new Color(100, 220, 100));
        prompt.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        prompt.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 6));

        inputField = new JTextField();
        inputField.setBackground(new Color(40, 40, 40));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        inputField.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));

        JButton sendBtn = new JButton("Send");
        styleButton(sendBtn, new Color(60, 130, 60));

        JButton helpBtn = new JButton("Help");
        styleButton(helpBtn, new Color(60, 90, 140));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        btnPanel.setBackground(new Color(30, 30, 30));
        btnPanel.add(helpBtn);
        btnPanel.add(sendBtn);

        inputRow.add(prompt,    BorderLayout.WEST);
        inputRow.add(inputField, BorderLayout.CENTER);
        inputRow.add(btnPanel,  BorderLayout.EAST);
        add(inputRow, BorderLayout.SOUTH);

        sendBtn.addActionListener(e -> submitCommand());
        helpBtn.addActionListener(e -> submitCommand("help"));

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitCommand();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    navigateHistory(-1);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    navigateHistory(+1);
                }
            }
        });

        appendOutput("Console de comenzi — scrie 'help' pentru lista de comenzi.\n");
        setVisible(false);
    }


    public void showConsole(GameController newController) {
        this.controller = newController;
        outputArea.setText("");
        appendOutput("Joc pornit! Scrie 'help' pentru comenzi disponibile.\n");
        setVisible(true);
        inputField.requestFocusInWindow();
    }

    public void hideConsole() {
        this.controller = null;
        setVisible(false);
    }

    public void appendOutput(String text) {
        SwingUtilities.invokeLater(() -> outputArea.append(text + "\n"));
    }


    private void submitCommand() {
        submitCommand(inputField.getText());
    }

    private void submitCommand(String cmd) {
        String text = cmd.trim();
        if (text.isEmpty()) return;

        appendOutput("▶ " + text);

        if (history.isEmpty() || !history.get(history.size() - 1).equals(text)) {
            history.add(text);
        }
        historyIndex = history.size();
        inputField.setText("");

        if (controller == null) {
            appendOutput("⚠ Nicio simulare activă. Apasă 'Start Game' mai întâi.\n");
            return;
        }
        String result = controller.execute(text);
        appendOutput(result + "\n");
    }

    private void navigateHistory(int direction) {
        if (history.isEmpty()) return;
        historyIndex = Math.max(0, Math.min(history.size() - 1, historyIndex + direction));
        inputField.setText(history.get(historyIndex));
        inputField.setCaretPosition(inputField.getText().length());
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        btn.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
    }
}