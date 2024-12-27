package model;

public interface Car {
int getId();
    String getModel();
    String getType();
    boolean isAvailable();
    void setAvailable(boolean available);
    double getBasePrice();
}
