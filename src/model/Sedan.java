package model;

public class Sedan implements Car {
    private int id;

    private String model;
    private boolean available;
    private double basePrice;

    public Sedan(int id,String model, boolean available, double basePrice) {
        this.model = model;
        this.available = available;
        this.basePrice = basePrice;
        this.id=id;
    }

    @Override
    public int getId() {
        return id;
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
        return basePrice;
    }
}
