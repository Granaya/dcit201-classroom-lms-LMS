package com.vehicle.rental;

import java.util.*;

// Abstract Class: Vehicle
public abstract class Vehicle {
    private final String vehicleId;
    private final String model;
    private final double baseRentalRate;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        this.vehicleId = Objects.requireNonNull(vehicleId, "Vehicle ID cannot be null");
        this.model = Objects.requireNonNull(model, "Model cannot be null");
        if (baseRentalRate <= 0) throw new IllegalArgumentException("Base rental rate must be positive");
        this.baseRentalRate = baseRentalRate;
        this.isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getBaseRentalRate() {
        return baseRentalRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public abstract double calculateRentalCost(int days);

    public abstract boolean isAvailableForRental();

    @Override
    public String toString() {
        return String.format("Vehicle ID: %s, Model: %s, Base Rate: %.2f, Available: %b", 
                              vehicleId, model, baseRentalRate, isAvailable);
    }
}

// Rentable Interface
interface Rentable {
    void rent(Customer customer, int days);
    void returnVehicle();
}

// Concrete Class: Car
class Car extends Vehicle implements Rentable {
    private final boolean hasGPS;

    public Car(String vehicleId, String model, double baseRentalRate, boolean hasGPS) {
        super(vehicleId, model, baseRentalRate);
        this.hasGPS = hasGPS;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        if (hasGPS) cost += 50;
        return cost;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }

    @Override
    public void rent(Customer customer, int days) {
        setAvailability(false);
        customer.addRental(getVehicleId());
        System.out.println("Car rented: " + this);
    }

    @Override
    public void returnVehicle() {
        setAvailability(true);
        System.out.println("Car returned: " + this);
    }
}

// Concrete Class: Motorcycle
class Motorcycle extends Vehicle implements Rentable {
    private final boolean hasHelmet;

    public Motorcycle(String vehicleId, String model, double baseRentalRate, boolean hasHelmet) {
        super(vehicleId, model, baseRentalRate);
        this.hasHelmet = hasHelmet;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        if (hasHelmet) cost += 20;
        return cost;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }

    @Override
    public void rent(Customer customer, int days) {
        setAvailability(false);
        customer.addRental(getVehicleId());
        System.out.println("Motorcycle rented: " + this);
    }

    @Override
    public void returnVehicle() {
        setAvailability(true);
        System.out.println("Motorcycle returned: " + this);
    }
}

// Concrete Class: Truck
class Truck extends Vehicle implements Rentable {
    private final double loadCapacity;

    public Truck(String vehicleId, String model, double baseRentalRate, double loadCapacity) {
        super(vehicleId, model, baseRentalRate);
        if (loadCapacity <= 0) throw new IllegalArgumentException("Load capacity must be positive");
        this.loadCapacity = loadCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days + loadCapacity * 10;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }

    @Override
    public void rent(Customer customer, int days) {
        setAvailability(false);
        customer.addRental(getVehicleId());
        System.out.println("Truck rented: " + this);
    }

    @Override
    public void returnVehicle() {
        setAvailability(true);
        System.out.println("Truck returned: " + this);
    }
}

// Customer Class
class Customer {
    private final String name;
    private final String customerId;
    private final List<String> rentalHistory;
    private int loyaltyPoints;

    public Customer(String name, String customerId) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.rentalHistory = new ArrayList<>();
        this.loyaltyPoints = 0;
    }

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<String> getRentalHistory() {
        return Collections.unmodifiableList(rentalHistory);
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void addRental(String vehicleId) {
        rentalHistory.add(vehicleId);
        loyaltyPoints += 10; // Add points per rental
    }

    public boolean isEligibleForRental() {
        return loyaltyPoints >= 50; // Example eligibility rule
    }

    @Override
    public String toString() {
        return String.format("Customer ID: %s, Name: %s, Loyalty Points: %d", customerId, name, loyaltyPoints);
    }
}

// RentalTransaction Class
class RentalTransaction {
    private final String transactionId;
    private final Customer customer;
    private final Vehicle vehicle;
    private final int rentalDays;
    private final double totalCost;

    public RentalTransaction(String transactionId, Customer customer, Vehicle vehicle, int rentalDays) {
        this.transactionId = transactionId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.rentalDays = rentalDays;
        this.totalCost = vehicle.calculateRentalCost(rentalDays);
    }

    @Override
    public String toString() {
        return String.format("Transaction ID: %s, Customer: %s, Vehicle: %s, Days: %d, Total Cost: %.2f",
                              transactionId, customer, vehicle, rentalDays, totalCost);
    }
}

// RentalAgency Class
class RentalAgency {
    private final List<Vehicle> fleet;
    private final List<RentalTransaction> transactions;

    public RentalAgency() {
        fleet = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public void processRental(String vehicleId, Customer customer, int days) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getVehicleId().equals(vehicleId) && vehicle.isAvailable()) {
                vehicle.setAvailability(false);
                RentalTransaction transaction = new RentalTransaction(UUID.randomUUID().toString(), customer, vehicle, days);
                transactions.add(transaction);
                customer.addRental(vehicleId);
                System.out.println("Rental processed: " + transaction);
                return;
            }
        }
        System.out.println("Vehicle not available for rental.");
    }

    public void returnVehicle(String vehicleId) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                vehicle.setAvailability(true);
                System.out.println("Vehicle returned: " + vehicle);
                return;
            }
        }
        System.out.println("Vehicle not found in fleet.");
    }

    public void generateReport() {
        System.out.println("Rental Transactions Report:");
        for (RentalTransaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}
