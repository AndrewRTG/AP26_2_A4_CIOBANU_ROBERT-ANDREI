package org.example.maze.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedMemory {

    private final List<String> clues = new ArrayList<>();

    public synchronized void addInfo(String info) {
        clues.add(info);
        System.out.println("New info added to memory: " + info);
    }

    public synchronized List<String> getInfo() {
        return new ArrayList<>(clues);
    }

    private final Set<String> exploredCells = new HashSet<>();


    public synchronized boolean claimCell(int row, int col) { // il facem synchronised sa evitam mutual exclusion
        String key = row + "," + col;
        if (exploredCells.contains(key)) {
            return false; //este deja explorat de altcineva
        }
        exploredCells.add(key);
        return true;
    }


    public synchronized boolean isExplored(int row, int col) {
        return exploredCells.contains(row + "," + col);
    }


    public synchronized int exploredCount() {
        return exploredCells.size();
    }
    private int totalRobots = 0;
    private final AtomicInteger finishedRobots = new AtomicInteger(0);
    private Runnable onAllRobotsFinished; // callback → Game.stopGame()

    public void setTotalRobots(int total)              { this.totalRobots = total; }
    public void setOnAllRobotsFinished(Runnable callback) { this.onAllRobotsFinished = callback; }

    public void robotFinished(String robotName) {
        int done = finishedRobots.incrementAndGet();
        System.out.println("🤖 " + robotName + " a terminat explorarea. ("
                + done + "/" + totalRobots + " roboți gata)");
        if (done >= totalRobots && onAllRobotsFinished != null) {
            System.out.println("🔍 Toți roboții au terminat explorarea — iepurele nu a fost prins.");
            onAllRobotsFinished.run();
        }
    }
}