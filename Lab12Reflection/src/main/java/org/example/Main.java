package org.example;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        String folderPath;

        if (args.length == 0) {
            folderPath = "target/classes";
        } else {
            folderPath = args[0];
        }

        try {
            Path folder = Path.of(folderPath);

            MyClassLoader classLoader = new MyClassLoader(folder);
            ClassScanner scanner = new ClassScanner(folder, classLoader);
            ClassAnalyzer analyzer = new ClassAnalyzer();

            for (Class<?> clazz : scanner.loadClasses()) {
                analyzer.analyze(clazz);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}