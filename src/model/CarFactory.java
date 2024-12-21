package model;

public class CarFactory {
    public static Car createCar(String type, String model, boolean available) {
        switch (type) {
            case "SUV":
                return new SUV(model, available);
            case "Sedan":
                return new Sedan(model, available);
            case "Luxury":
                return new Luxury(model, available);
            case "Hatchback":
                return new Hatchback(model, available);
            case "Sports": // Sports tipi eklendi
                return new Sports(model, available);
            default:
                throw new IllegalArgumentException("Unknown car type: " + type);
        }
    }
}
