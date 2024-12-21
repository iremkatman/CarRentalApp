package model;

public class FullFuelDecorator extends CarDecorator {
    public FullFuelDecorator(Car decoratedCar) {
        super(decoratedCar);
    }

    @Override
    public String getModel() {
        return super.getModel() + " (with Full Tank Fuel)";
    }

    @Override
    public double getBasePrice() {
        return decoratedCar.getBasePrice() + 50.0; // Ekstra yakıt maliyeti
    }

    @Override
    public double getCost() {
        return 50.0; // Yakıt için ekstra ücret
    }
}
