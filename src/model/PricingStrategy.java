package model;

public interface PricingStrategy {
    double calculatePrice(double basePrice, int duration);
}
