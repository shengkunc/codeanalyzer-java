/**
 * Static and Instance Initialization Blocks Example with comments.
 *
 * MIT License
 * <p>
 */
package org.example;

// Import statements
import java.util.List;

/**
 * The App class demonstrates the use of static and instance initialization blocks,
 * as well as a constructor in Java.
 */
public class App {
    // Static field
    private static String staticMessage;

    // Static initialization block
    static {
        try {
            staticMessage = "Static block initialized";
            System.out.println("Static initialization block executed.");
            initializeStaticFields(); // Call a method to initialize static fields
        } catch (Exception e) {
            // Handle any exceptions that occur during initialization
            System.err.println("Error in static block: " + e.getMessage());
            throw new RuntimeException(e); // Rethrow the exception
        }
    }

    // Instance initialization block
    {
        try {
            System.out.println("Instance initialization block executed.");
            initializeInstanceFields();
        } catch (Exception e) {
            System.err.println("Error in instance block: " + e.getMessage());
        }
    }

    /**
     * Constructor for the App class.
     * Prints a message indicating that the constructor has been executed.
     */
    public App() {
        System.out.println("Constructor executed.");
    }

    /**
     * Initializes static fields.
     * Prints a message indicating that static fields are being initialized.
     */
    private static void initializeStaticFields() {
        System.out.println("Initializing static fields.");
    }

    /**
     * Initializes instance fields.
     * Prints a message indicating that instance fields are being initialized.
     */
    private void initializeInstanceFields() {
        // This is a comment associated with the println statement below
        System.out.println("Initializing instance fields.");
    }

    /**
     * The main method is the entry point of the application.
     * Creates a new instance of the App class.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        // This is an orphaned comment

        new App(); // Create a new instance of the App class
    }
}
