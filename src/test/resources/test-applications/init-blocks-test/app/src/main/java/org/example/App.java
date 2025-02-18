package org.example;

import java.util.List;

public class App {
    private static String staticMessage;

    static {
        try {
            staticMessage = "Static block initialized";
            System.out.println("Static initialization block executed.");
            initializeStaticFields();
        } catch (Exception e) {
            System.err.println("Error in static block: " + e.getMessage());
        }
    }

    {
        try {
            System.out.println("Instance initialization block executed.");
            initializeInstanceFields();
        } catch (Exception e) {
            System.err.println("Error in instance block: " + e.getMessage());
        }
    }

    public App() {
        System.out.println("Constructor executed.");
    }

    private static void initializeStaticFields() {
        System.out.println("Initializing static fields.");
    }

    private void initializeInstanceFields() {
        System.out.println("Initializing instance fields.");
    }

    public static void main(String[] args) {
        new App();
    }
}
