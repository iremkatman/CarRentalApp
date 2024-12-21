package model;

public class CarFactory {
    public static Car createCar(String type, int id, String model, boolean available, double basePrice) {
        switch (type) {
            case "SUV":
                return new SUV(id, model, available, basePrice);
            case "Sports":
                return new Sports(id, model, available, basePrice);
            case "Hatchback":
                return new Hatchback(id, model, available, basePrice);
            case "Luxury":
                return new Luxury(id, model, available, basePrice);
            case "Sedan":
                return new Sedan(id, model, available, basePrice);
            default:
                throw new IllegalArgumentException("Unknown car type: " + type);
        }
    }


}
