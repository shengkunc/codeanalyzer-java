package org.example;

public record CarRecord(String model, int year) {

    // Public method
    public String getCarDetails() {
        return "Car: " + model + " (Year: " + year + ")";
    }

    // Private method
    private String internalVIN() {
        return "VIN-123456";
    }

    // Package-private method
    String getInternalVIN() {
        return internalVIN();
    }
}