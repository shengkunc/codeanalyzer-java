package org.example;

public record PersonRecord(String name, int age) {

    public PersonRecord {
        // Constructor logic
        if (name == null || name.isBlank()) {
            name = "Unknown";
        }
        if (age < 18) {
            age = 18;
        }
    }
    // Private field (Not directly possible in records, but can be mimicked with private static)
    private static String secretIdentity = "Unknown";

    // Public method
    public String greet() {
        return "Hello, my name is " + name + " and I am " + age + " years old.";
    }

    // Private method (only accessible within this record)
    private String getSecretIdentity() {
        return secretIdentity;
    }

    // Protected method (Not valid in records, using package-private as an alternative)
    String internalInfo() {
        return "Internal ID: " + hashCode();
    }
}
