package model;

public class WeeklyPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice, int duration) {
        return basePrice * duration * 0.75; // Haftalık indirimli fiyat (örneğin %25 indirim)
    }
}
