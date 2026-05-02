package org.example.maze.game;

import java.util.ArrayList;
import java.util.List;

public class SharedMemory {
    private final List<String> clues = new ArrayList<>();

    public synchronized void addInfo(String info) {
        clues.add(info);
        System.out.println("New info added to memory: " + info);
    }

    public synchronized List<String> getInfo() {
        return new ArrayList<>(clues);
    }
}