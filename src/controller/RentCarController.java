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
import java.util.Date;
import java.util.List;
import javax.swing.SpinnerDateModel;
import javax.swing.JSpinner;
import java.text.SimpleDateFormat;

public class RentCarController {
    private RentCarView rentCarView;
    private User currentUser;
    private WelcomeView welcomeView;
    private List<Car> carList;
    private Connection connection = SingletonConnection.getInstance();
    private int selectedDuration;
    private LocalDateTime startDateTime;
    private String selectedPricingModel = "Hourly - No Discount"; // Varsayılan değer


    public RentCarController(RentCarView rentCarView, User currentUser, WelcomeView welcomeView) {
        this.rentCarView = rentCarView;
        this.currentUser = currentUser;
        this.welcomeView = welcomeView;

        refreshTableAndBudget();

        rentCarView.getPricingModelComboBox().addActionListener(e -> handlePricingModelSelection());
        rentCarView.getRentButton().addActionListener(e -> rentCar());
        rentCarView.getBackButton().addActionListener(e -> goBackToWelcome());
        rentCarView.getFullFuelCheckBox().addActionListener(e -> updateEstimatedCost());
        rentCarView.getInsuranceCheckBox().addActionListener(e -> updateEstimatedCost());
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

            SpinnerDateModel dateModel = new SpinnerDateModel();
            dateModel.setStart(new Date());
            dateModel.setValue(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));

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
                            updateEstimatedCost();
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

    private void updateEstimatedCost() {
        int selectedRow = rentCarView.getCarTable().getSelectedRow();
        if (selectedRow == -1) {
            rentCarView.getTotalCostLabel().setText("Total Cost: $0.0");
            return;
        }

        Car selectedCar = carList.get(selectedRow);
        PricingStrategy pricingStrategy = getPricingStrategy(selectedPricingModel);

        Car decoratedCar = selectedCar;

        if (rentCarView.getFullFuelCheckBox().isSelected()) {
            decoratedCar = new FullFuelDecorator(decoratedCar);
        }
        if (rentCarView.getInsuranceCheckBox().isSelected()) {
            decoratedCar = new InsuranceDecorator(decoratedCar);
        }

        double estimatedCost = calculateTotalCost(decoratedCar, pricingStrategy, selectedDuration);
        rentCarView.getTotalCostLabel().setText(String.format("Estimated Cost: $%.2f", estimatedCost));
    }

    private PricingStrategy getPricingStrategy(String pricingModel) {
        if (pricingModel == null || pricingModel.isEmpty()) {
            throw new IllegalArgumentException("No pricing model selected.");
        }
        switch (pricingModel) {
            case "Hourly - No Discount":
                return new HourlyPricing();
            case "Daily - 10% Discount":
                return new DailyPricing();
            case "Weekly - 25% Discount":
                return new WeeklyPricing();
            default:
                throw new IllegalArgumentException("Invalid pricing model.");
        }
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

        if (selectedPricingModel == null) {
            JOptionPane.showMessageDialog(rentCarView, "Please select a pricing model.");
            return;
        }
        if (selectedDuration <= 0) {
            JOptionPane.showMessageDialog(rentCarView, "Rental duration must be greater than 0.");
            return;
        }

        Car decoratedCar = selectedCar;

        if (rentCarView.getFullFuelCheckBox().isSelected()) {
            decoratedCar = new FullFuelDecorator(decoratedCar);
        }
        if (rentCarView.getInsuranceCheckBox().isSelected()) {
            decoratedCar = new InsuranceDecorator(decoratedCar);
        }

        PricingStrategy pricingStrategy = getPricingStrategy(selectedPricingModel);

        double totalCost = calculateTotalCost(decoratedCar, pricingStrategy, selectedDuration);

        if (currentUser.getBudget() < totalCost) {
            JOptionPane.showMessageDialog(rentCarView,
                    String.format("Insufficient funds. You need $%.2f more.", totalCost - currentUser.getBudget()));
            return;
        }

        processReservation(decoratedCar, selectedPricingModel, selectedDuration, totalCost);
        refreshTableAndBudget();
    }

    private double calculateTotalCost(Car decoratedCar, PricingStrategy pricingStrategy, int duration) {
        double baseCost = pricingStrategy.calculatePrice(decoratedCar.getBasePrice(), duration);
        double decoratorCost = 0;

        if (decoratedCar instanceof FullFuelDecorator) {
            decoratorCost += ((FullFuelDecorator) decoratedCar).getCost();
        }
        if (decoratedCar instanceof InsuranceDecorator) {
            decoratorCost += ((InsuranceDecorator) decoratedCar).getCost()*duration;
        }

        return baseCost + decoratorCost;
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
