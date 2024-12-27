package model;

public class HourlyPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice, int duration) {
        return basePrice * duration/24;
    }
}
