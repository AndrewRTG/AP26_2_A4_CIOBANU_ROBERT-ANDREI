package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionRunner {

    public void runClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);

            System.out.println("Loaded class: " + clazz.getName());

            Method runMethod = findRunMethod(clazz);

            if (runMethod == null) {
                System.out.println("The class does not contain a run method with no arguments.");
                return;
            }

            System.out.println("Found method: " + runMethod.getName());

            runMethod.setAccessible(true);

            if (Modifier.isStatic(runMethod.getModifiers())) {
                runMethod.invoke(null);
            } else {
                Object instance = createInstance(clazz);
                runMethod.invoke(instance);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        } catch (Exception e) {
            System.out.println("Error while running class: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Method findRunMethod(Class<?> clazz) {
        try {
            return clazz.getDeclaredMethod("run");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}