package service;

import database.SingletonConnection;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class CarService {

    private final Connection connection = SingletonConnection.getInstance();

    public List<Car> fetchAvailableCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars WHERE availability = TRUE";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cars.add(CarFactory.createCar(
                        rs.getString("type"),
                        rs.getInt("id"),
                        rs.getString("model"),
                        rs.getBoolean("availability"),
                        rs.getDouble("price_per_day")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    public void updateCarAvailability(int carId, boolean availability) {
        String query = "UPDATE cars SET availability = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, availability);
            stmt.setInt(2, carId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}