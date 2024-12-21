package model;

public class SUV implements Car {
    private int id;
    private String model;
    private boolean available;
    private double basePrice;

    public SUV(int id, String model, boolean available, double basePrice) {
        this.id = id;
        this.model = model;
        this.available = available;
        this.basePrice = basePrice;
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
        return "SUV";
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
