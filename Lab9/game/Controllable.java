package org.example.maze.game;


public interface Controllable {
    String getName();
    void setStepDelay(int delayMs);
    void pause();
    void resume();
    int getStepDelay();
    boolean isPaused();
}