package model;

public class DailyPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice, int duration) {
        return basePrice * duration * 0.9;
    }
}
