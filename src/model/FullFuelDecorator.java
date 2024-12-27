package model;

public class FullFuelDecorator extends CarDecorator {
    private static final double FULLFUEL_COST = 50.0;

    public FullFuelDecorator(Car decoratedCar) {
        super(decoratedCar);
    }

    @Override
    public int getId() {
        return decoratedCar.getId();
    }

    @Override
    public String getModel() {
        return decoratedCar.getModel() + " (with FullFuel)";
    }


    @Override
    public double getBasePrice() {
        return decoratedCar.getBasePrice();
    }

    @Override
    public double getCost(int duration) {
        return FULLFUEL_COST;
    }
}
