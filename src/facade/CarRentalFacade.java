package facade;

import model.PricingStrategy;
import database.SingletonConnection;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CarRentalFacade {

    private Connection connection = SingletonConnection.getInstance();

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

    public void populateCarTable(List<Car> carList, JTable carTable, JLabel currentBalanceLabel, User currentUser) {
        String[] columnNames = {"ID", "Model", "Type", "Base Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Car car : carList) {
            model.addRow(new Object[]{car.getId(), car.getModel(), car.getType(), car.getBasePrice()});
        }

        carTable.setModel(model);
        carTable.getColumnModel().getColumn(0).setMinWidth(0);
        carTable.getColumnModel().getColumn(0).setMaxWidth(0);
        carTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        currentBalanceLabel.setText(String.format("Current Balance: $%.2f", currentUser.getBudget()));
    }

    public double calculateTotalCost(Car decoratedCar, PricingStrategy pricingStrategy, int duration) {
        double baseCost = pricingStrategy.calculatePrice(decoratedCar.getBasePrice(), duration);
        double decoratorCost = 0;

        if (decoratedCar instanceof FullFuelDecorator) {
            decoratorCost += ((FullFuelDecorator) decoratedCar).getCost();
        }
        if (decoratedCar instanceof InsuranceDecorator) {
            decoratorCost += ((InsuranceDecorator) decoratedCar).getCost() * duration;
        }

        return baseCost + decoratorCost;
    }

    public void processReservation(Car selectedCar, User currentUser, String pricingModel, int duration, double totalCost, LocalDateTime startDateTime) {
        LocalDateTime now = startDateTime;
        LocalDateTime returnDate;

        switch (pricingModel) {
            case "Hourly - No Discount":
                returnDate = now.plusHours(duration);
                break;
            case "Daily - 10% Discount":
                returnDate = now.plusDays(duration);
                break;
            case "Weekly - 25% Discount":
                returnDate = now.plusWeeks(duration);
                break;
            default:
                returnDate = now;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedStartDate = now.format(formatter);
        String formattedEndDate = returnDate.format(formatter);

        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE cars SET availability = FALSE WHERE id = ?")) {
            stmt.setInt(1, selectedCar.getId());
            stmt.executeUpdate();

            String reservationQuery = "INSERT INTO reservations (car_id, user_id, reservation_date_start, reservation_date_end) VALUES (?, ?, ?, ?)";
            try (PreparedStatement resStmt = connection.prepareStatement(reservationQuery)) {
                resStmt.setInt(1, selectedCar.getId());
                resStmt.setInt(2, currentUser.getId());
                resStmt.setString(3, formattedStartDate);
                resStmt.setString(4, formattedEndDate);
                resStmt.executeUpdate();
            }

            String updateBudgetQuery = "UPDATE users SET budget = ? WHERE id = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateBudgetQuery)) {
                updateStmt.setDouble(1, currentUser.getBudget() - totalCost);
                updateStmt.setInt(2, currentUser.getId());
                updateStmt.executeUpdate();
            }

            currentUser.setBudget(currentUser.getBudget() - totalCost);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null,
                "Car rented successfully: " + selectedCar.getModel() +
                        "\nTotal Cost: $" + totalCost +
                        "\nReturn Date: " + formattedEndDate);
    }
}