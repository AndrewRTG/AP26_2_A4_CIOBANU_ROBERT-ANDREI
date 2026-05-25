package org.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {
    private final Path rootFolder;
    private final ClassLoader classLoader;

    public ClassScanner(Path rootFolder, ClassLoader classLoader) {
        this.rootFolder = rootFolder;
        this.classLoader = classLoader;
    }

    public List<Class<?>> loadClasses() throws Exception {
        List<Class<?>> classes = new ArrayList<>();

        Files.walk(rootFolder)
                .filter(path -> path.toString().endsWith(".class"))
                .forEach(path -> {
                    try {
                        String className = getClassName(path);
                        Class<?> clazz = classLoader.loadClass(className);
                        classes.add(clazz);
                    } catch (Exception e) {
                        System.out.println("Could not load class: " + path);
                    }
                });

        return classes;
    }

    private String getClassName(Path classFile) {
        Path relativePath = rootFolder.relativize(classFile);

        String className = relativePath.toString()
                .replace("\\", ".")
                .replace("/", ".");

        return className.substring(0, className.length() - ".class".length());
    }
}