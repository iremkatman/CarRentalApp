package model;

public class Hatchback implements Car {
    private String model;
    private boolean available;
    private final double basePrice = 100.0; // Örnek fiyat

    public Hatchback(String model, boolean available) {
        this.model = model;
        this.available = available;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getType() {
        return "Hatchback";
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

