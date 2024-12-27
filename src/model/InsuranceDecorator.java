package model;

public class InsuranceDecorator extends CarDecorator {
    private static final double INSURANCE_COST_PER_DAY = 30.0;

    public InsuranceDecorator(Car decoratedCar) {
        super(decoratedCar);
    }

    @Override
    public int getId() {
        return decoratedCar.getId();
    }

    @Override
    public String getModel() {
        return decoratedCar.getModel() + " (with Temporary Insurance)";
    }

    @Override
    public double getBasePrice() {
        return decoratedCar.getBasePrice();
    }

    @Override
    public double getCost(int duration) {
        return INSURANCE_COST_PER_DAY * duration;
    }
}


