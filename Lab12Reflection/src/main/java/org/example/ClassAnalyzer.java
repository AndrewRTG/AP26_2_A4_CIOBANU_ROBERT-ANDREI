package org.example;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassAnalyzer {
    public void analyze(Class<?> clazz) {
        System.out.println();
        System.out.println("Class: " + clazz.getName());

        if (clazz.isAnnotation()) {
            System.out.println("This class is an annotation type.");
            return;
        }

        if (!Modifier.isPublic(clazz.getModifiers())) {
            System.out.println("Class is not public.");
            return;
        }

        displayPrototype(clazz);
        invokeAnnotatedMethods(clazz);
    }

    private void displayPrototype(Class<?> clazz) {
        System.out.println("Prototype:");
        System.out.println(Modifier.toString(clazz.getModifiers()) + " class " + clazz.getSimpleName());

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            System.out.print("  ");
            System.out.print(Modifier.toString(method.getModifiers()));
            System.out.print(" ");
            System.out.print(method.getReturnType().getSimpleName());
            System.out.print(" ");
            System.out.print(method.getName());
            System.out.print("(");

            Class<?>[] parameterTypes = method.getParameterTypes();

            for (int i = 0; i < parameterTypes.length; i++) {
                System.out.print(parameterTypes[i].getSimpleName());

                if (i < parameterTypes.length - 1) {
                    System.out.print(", ");
                }
            }

            System.out.println(")");
        }
    }

    private void invokeAnnotatedMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();

            if (annotations.length == 0) {
                continue;
            }

            if (canInvoke(method)) {
                invokeMethod(clazz, method);
            }
        }
    }

    private boolean canInvoke(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length == 0) {
            return true;
        }

        if (parameterTypes.length == 1) {
            return parameterTypes[0] == int.class || parameterTypes[0] == Integer.class;
        }

        return false;
    }

    private void invokeMethod(Class<?> clazz, Method method) {
        try {
            method.setAccessible(true);

            Object instance = null;

            if (!Modifier.isStatic(method.getModifiers())) {
                instance = createInstance(clazz);
            }

            System.out.println("Invoking method: " + method.getName());

            if (method.getParameterCount() == 0) {
                method.invoke(instance);
            } else {
                method.invoke(instance, 10);
            }

        } catch (Exception e) {
            System.out.println("Could not invoke method: " + method.getName());
        }
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}