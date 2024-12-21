package model;

public class Sedan implements Car {
    private String model;
    private boolean available;
    private final double basePrice = 100.0; // Örnek fiyat


    public Sedan(String model, boolean available) {
        this.model = model;
        this.available = available;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getType() {
        return "Sedan";
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public double getBasePrice() {
        return 0;
    }
}
