package controller;

import database.SingletonConnection;
import model.*;
import view.RentCarView;
import view.WelcomeView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Date; // Tarih işlemleri için
import javax.swing.SpinnerDateModel; // Tarih modelleme için
import javax.swing.JSpinner; // Tarih seçimi için JSpinner bileşeni
import java.text.SimpleDateFormat; // Tarih biçimlendirme için

public class RentCarController {
    private RentCarView rentCarView;
    private User currentUser;
    private WelcomeView welcomeView;
    private List<Car> carList;
    private Connection connection = SingletonConnection.getInstance();
    private int selectedDuration;
    private String selectedPricingModel;
    LocalDateTime startDateTime;

    public RentCarController(RentCarView rentCarView, User currentUser, WelcomeView welcomeView) {
        this.rentCarView = rentCarView;
        this.currentUser = currentUser;
        this.welcomeView = welcomeView;

        refreshTableAndBudget();

        rentCarView.getPricingModelComboBox().addActionListener(e -> handlePricingModelSelection());
        rentCarView.getRentButton().addActionListener(e -> rentCar());
        rentCarView.getBackButton().addActionListener(e -> goBackToWelcome());
    }

    private List<Car> fetchAvailableCars() {
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

    private void populateCarTable() {
        String[] columnNames = {"ID", "Model", "Type", "Base Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Car car : carList) {
            model.addRow(new Object[]{car.getId(), car.getModel(), car.getType(), car.getBasePrice()});
        }

        rentCarView.getCarTable().setModel(model);
        rentCarView.getCurrentBalanceLabel().setText(String.format("Current Balance: $%.2f", currentUser.getBudget()));
    }

    public void refreshTableAndBudget() {
        carList = fetchAvailableCars();
        populateCarTable();
    }

    private void handlePricingModelSelection() {
        selectedPricingModel = (String) rentCarView.getPricingModelComboBox().getSelectedItem();

        if (selectedPricingModel != null) {
            if (!selectedPricingModel.equals("Hourly - No Discount") &&
                    !selectedPricingModel.equals("Daily - 10% Discount") &&
                    !selectedPricingModel.equals("Weekly - 25% Discount")) {
                rentCarView.getTotalCostLabel().setText("Invalid pricing model.");
                return;
            }

            // JSpinner ile tarih seçimi
            SpinnerDateModel dateModel = new SpinnerDateModel();
            dateModel.setStart(new Date()); // Minimum tarih olarak bugünü ayarla
            dateModel.setValue(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // Varsayılan tarih yarın

            JSpinner dateSpinner = new JSpinner(dateModel);
            JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
            dateSpinner.setEditor(editor);

            JPanel panel = new JPanel();
            panel.add(new JLabel("Select Start Date:"));
            panel.add(dateSpinner);

            int result = JOptionPane.showConfirmDialog(rentCarView, panel, "Select Start Date", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Date selectedStartDate = (Date) dateSpinner.getValue();
                 startDateTime = LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd").format(selectedStartDate) + "T12:00:00");

                String durationStr = JOptionPane.showInputDialog(rentCarView, "Enter rental duration:");
                if (durationStr != null) {
                    try {
                        selectedDuration = Integer.parseInt(durationStr);
                        if (selectedDuration > 0) {
                            updateEstimatedCost(selectedPricingModel, selectedDuration, startDateTime);
                        } else {
                            JOptionPane.showMessageDialog(rentCarView, "Duration must be positive!");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(rentCarView, "Invalid duration entered!");
                    }
                }
            }
        }
    }

    private void updateEstimatedCost(String pricingModel, int duration, LocalDateTime startDateTime) {
        int selectedRow = rentCarView.getCarTable().getSelectedRow();
        if (selectedRow == -1) {
            rentCarView.getTotalCostLabel().setText("Total Cost: $0.0");
            return;
        }

        Car selectedCar = carList.get(selectedRow);
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
                rentCarView.getTotalCostLabel().setText("Invalid pricing model.");
                return;
        }

        double estimatedCost = pricingStrategy.calculatePrice(selectedCar.getBasePrice(), duration);

        if (rentCarView.getFullFuelCheckBox().isSelected()) estimatedCost += 50;
        if (rentCarView.getInsuranceCheckBox().isSelected()) estimatedCost += 30;

        // Return date hesaplama
        LocalDateTime returnDate;
        switch (pricingModel) {
            case "Hourly - No Discount":
                returnDate = startDateTime.plusHours(duration);
                break;
            case "Daily - 10% Discount":
                returnDate = startDateTime.plusDays(duration);
                break;
            case "Weekly - 25% Discount":
                returnDate = startDateTime.plusWeeks(duration);
                break;
            default:
                returnDate = startDateTime;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedReturnDate = returnDate.format(formatter);

        rentCarView.getTotalCostLabel().setText(String.format("Estimated Cost: $%.2f\nReturn Date: %s", estimatedCost, formattedReturnDate));
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

        if (selectedPricingModel == null || selectedDuration <= 0) {
            JOptionPane.showMessageDialog(rentCarView, "Please select a pricing model and duration first.");
            return;
        }

        double totalCost = calculateTotalCost(selectedCar, selectedPricingModel, selectedDuration);
        if (currentUser.getBudget() < totalCost) {
            JOptionPane.showMessageDialog(rentCarView, "Insufficient funds. Please deposit more money.");
            return;
        }

        processReservation(selectedCar, selectedPricingModel, selectedDuration, totalCost);
        refreshTableAndBudget();
    }

    private double calculateTotalCost(Car selectedCar, String pricingModel, int duration) {
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
                return 0.0;
        }

        double totalCost = pricingStrategy.calculatePrice(selectedCar.getBasePrice(), duration);
        if (rentCarView.getFullFuelCheckBox().isSelected()) totalCost += 50;
        if (rentCarView.getInsuranceCheckBox().isSelected()) totalCost += 30;

        return totalCost;
    }

    private void processReservation(Car selectedCar, String pricingModel, int duration, double totalCost) {
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

        JOptionPane.showMessageDialog(rentCarView,
                "Car rented successfully: " + selectedCar.getModel() +
                        "\nTotal Cost: $" + totalCost +
                        "\nReturn Date: " + formattedEndDate);
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true);
        rentCarView.dispose();
    }
}
