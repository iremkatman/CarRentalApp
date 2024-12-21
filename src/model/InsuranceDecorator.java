package model;

public class InsuranceDecorator extends CarDecorator {
    private static final double INSURANCE_COST = 30.0; // Sigorta maliyeti sabit bir değer

    public InsuranceDecorator(Car decoratedCar) {
        super(decoratedCar);
    }

    @Override
    public int getId() {
        // Orijinal aracın ID'sini döndür
        return decoratedCar.getId();
    }

    @Override
    public String getModel() {
        // Model bilgisine sigorta detayını ekle
        return decoratedCar.getModel() + " (with Temporary Insurance)";
    }

    @Override
    public double getBasePrice() {
        // Sigorta maliyetini temel fiyata ekle
        return decoratedCar.getBasePrice() + INSURANCE_COST;
    }

    @Override
    public double getCost() {
        // Sigorta maliyetini döndür
        return INSURANCE_COST;
    }
}
