package org.example.plugins;

public class RunMe {
    @MyAnnotation
    public void hello() {
        System.out.println("Hello method was invoked.");
    }

    @MyAnnotation
    public void printNumber(int number) {
        System.out.println("Number is: " + number);
    }

    @MyAnnotation
    public void invalidMethod(String text) {
        System.out.println(text);
    }

    public void normalMethod() {
        System.out.println("This method is not annotated.");
    }
}