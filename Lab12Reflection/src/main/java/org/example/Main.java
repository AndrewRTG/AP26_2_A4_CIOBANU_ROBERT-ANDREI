package org.example;

public class Main {
    public static void main(String[] args) {
        ReflectionRunner runner = new ReflectionRunner();
        runner.runClass("org.example.Main");
    }

    public void run() {
        System.out.println("Metoda run din Main a fost apelata prin reflection!");
    }
}