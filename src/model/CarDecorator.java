package model;

public abstract class CarDecorator implements Car {
    protected Car decoratedCar; // Orijinal Car nesnesi

    public CarDecorator(Car decoratedCar) {
        this.decoratedCar = decoratedCar;
    }

    @Override
    public String getModel() {
        return decoratedCar.getModel();
    }

    @Override
    public String getType() {
        return decoratedCar.getType();
    }

    @Override
    public boolean isAvailable() {
        return decoratedCar.isAvailable();
    }

    @Override
    public void setAvailable(boolean available) {
        decoratedCar.setAvailable(available);
    }


    public abstract double getCost(int duration);
}
