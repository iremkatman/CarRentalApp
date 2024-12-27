package model;

public class WeeklyPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice, int duration) {
        int totalDays = duration * 7;
        double weeklyCost = basePrice * 7 * 0.75;
        int weeks = totalDays / 7;
        return weeks * weeklyCost;
    }
}
