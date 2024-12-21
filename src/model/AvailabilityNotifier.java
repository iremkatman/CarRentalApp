package model;

public class AvailabilityNotifier extends Subject {
    private static AvailabilityNotifier instance;

    private AvailabilityNotifier() {}

    public static AvailabilityNotifier getInstance() {
        if (instance == null) {
            instance = new AvailabilityNotifier();
        }
        return instance;
    }
}
