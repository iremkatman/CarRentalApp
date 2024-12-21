package controller;

import database.SingletonConnection;
import model.*;
import model.AvailabilityNotifier;
import view.RentCarView;
import view.WelcomeView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RentCarController {
    private RentCarView rentCarView;
    private User currentUser;
    private WelcomeView welcomeView;
    private List<Car> carList;

    public RentCarController(RentCarView rentCarView, User currentUser, WelcomeView welcomeView) {
        this.rentCarView = rentCarView;
        this.currentUser = currentUser;
        this.welcomeView = welcomeView;

        carList = fetchAvailableCars();
        populateCarTable();

        rentCarView.getRentButton().addActionListener(e -> rentCar());
        rentCarView.getBackButton().addActionListener(e -> goBackToWelcome());
    }

    private List<Car> fetchAvailableCars() {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = SingletonConnection.getInstance()) {
            String query = "SELECT * FROM cars WHERE availability = TRUE";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cars.add(CarFactory.createCar(
                        rs.getString("type"),
                        rs.getString("model"),
                        rs.getBoolean("availability")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    private void populateCarTable() {
        String[] columnNames = {"Model", "Type", "Available"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Car car : carList) {
            model.addRow(new Object[]{car.getModel(), car.getType(), car.isAvailable() ? "Yes" : "No"});
        }

        rentCarView.getCarTable().setModel(model);
    }

    private void rentCar() {
        int selectedRow = rentCarView.getCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(rentCarView, "Please select a car to rent.");
            return;
        }

        Car selectedCar = carList.get(selectedRow);
        if (!selectedCar.isAvailable()) {
            JOptionPane.showMessageDialog(rentCarView, "Selected car is not available.");
            return;
        }

        // Kullanıcının fiyatlandırma modeli seçimini al
        String pricingModel = (String) rentCarView.getPricingModelComboBox().getSelectedItem();
        PricingStrategy pricingStrategy;

        switch (pricingModel) {
            case "Hourly - No Discount":
                pricingStrategy = new HourlyPricing();
                break;
            case "Daily - 10% Discount":
                pricingStrategy = new DailyPricing();
                break;
            case "Weekly - 25% Discount":
                pricingStrategy = new WeeklyPricing();
                break;
            default:
                JOptionPane.showMessageDialog(rentCarView, "Invalid pricing model selected!");
                return;
        }

        // Süreyi kullanıcıdan alın
        String durationStr = JOptionPane.showInputDialog(rentCarView, "Enter rental duration:");
        int duration;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration <= 0) {
                JOptionPane.showMessageDialog(rentCarView, "Duration must be positive!");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(rentCarView, "Invalid duration entered!");
            return;
        }

        // Ek özelliklerin maliyetini hesapla
        double totalCost = pricingStrategy.calculatePrice(selectedCar.getBasePrice(), duration);
        if (rentCarView.getFullFuelCheckBox().isSelected()) totalCost += 50;
        if (rentCarView.getInsuranceCheckBox().isSelected()) totalCost += 30;

        // Bütçe kontrolü
        if (currentUser.getBudget() < totalCost) {
            JOptionPane.showMessageDialog(rentCarView, "Insufficient funds. Please deposit more money.");
            return;
        }

        // Kullanıcı bütçesini güncelle
        currentUser.setBudget(currentUser.getBudget() - totalCost);
        selectedCar.setAvailable(false);
        JOptionPane.showMessageDialog(rentCarView,
                "Car rented successfully: " + selectedCar.getModel() +
                        "\nTotal Cost: $" + totalCost
        );

        // Veritabanını güncelle
        try (Connection connection = SingletonConnection.getInstance()) {
            String updateQuery = "UPDATE cars SET availability = FALSE WHERE model = ?";
            PreparedStatement stmt = connection.prepareStatement(updateQuery);
            stmt.setString(1, selectedCar.getModel());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Observer'lara bilgi gönder
        AvailabilityNotifier.getInstance().notifyObservers("Car rented: " + selectedCar.getModel());

        populateCarTable();
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true);
        rentCarView.dispose();
    }
}
