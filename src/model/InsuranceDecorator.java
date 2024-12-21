package model;

public class InsuranceDecorator extends CarDecorator {
    public InsuranceDecorator(Car decoratedCar) {
        super(decoratedCar);
    }

    @Override
    public String getModel() {
        return super.getModel() + " (with Temporary Insurance)";
    }

    @Override
    public double getBasePrice() {
        return decoratedCar.getBasePrice() + 30.0; // Sigorta için ekstra maliyet
    }

    @Override
    public double getCost() {
        return 30.0; // Sigorta için ekstra ücret
    }
}
