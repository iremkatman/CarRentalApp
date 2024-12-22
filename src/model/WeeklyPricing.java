package model;

public class WeeklyPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice, int duration) {
        int totalDays = duration * 7; // Giriş süresi haftayı temsil ettiği için 7 ile çarpılır.
        double weeklyCost = basePrice * 7 * 0.75; // Haftalık indirimli maliyet (7 gün için %25 indirim)
        int weeks = totalDays / 7; // Haftalık süre
        return weeks * weeklyCost;
    }
}
