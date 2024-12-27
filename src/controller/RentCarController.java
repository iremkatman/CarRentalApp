package controller;

import facade.CarRentalFacade;
import model.*;
import view.RentCarView;
import view.WelcomeView;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RentCarController {
    private RentCarView rentCarView;
    private User currentUser;
    private WelcomeView welcomeView;
    private List<Car> carList;
    private CarRentalFacade carRentalFacade;
    private int selectedDuration;
    private LocalDateTime startDateTime;
    private String selectedPricingModel = "Hourly - No Discount";

    public RentCarController(RentCarView rentCarView, User currentUser, WelcomeView welcomeView) {
        this.rentCarView = rentCarView;
        this.currentUser = currentUser;
        this.welcomeView = welcomeView;
        this.carRentalFacade = new CarRentalFacade();

        refreshTableAndBudget();

        rentCarView.getPricingModelComboBox().addActionListener(e -> handlePricingModelSelection());
        rentCarView.getRentButton().addActionListener(e -> rentCar());
        rentCarView.getBackButton().addActionListener(e -> goBackToWelcome());
        rentCarView.getFullFuelCheckBox().addActionListener(e -> updateEstimatedCost());
        rentCarView.getInsuranceCheckBox().addActionListener(e -> updateEstimatedCost());
    }

    private void refreshTableAndBudget() {
        carList = carRentalFacade.fetchAvailableCars();
        carRentalFacade.populateCarTable(carList, rentCarView.getCarTable(), rentCarView.getCurrentBalanceLabel(), currentUser);
    }

    private void handlePricingModelSelection() {
        selectedPricingModel = (String) rentCarView.getPricingModelComboBox().getSelectedItem();

        if (selectedPricingModel != null) {
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
                startDateTime = LocalDateTime.ofInstant(selectedStartDate.toInstant(), ZoneId.systemDefault());

                if (startDateTime.isBefore(LocalDateTime.now())) {
                    JOptionPane.showMessageDialog(rentCarView, "Start date cannot be in the past.");
                    return;
                }

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

        List<CarDecorator> decorators = new ArrayList<>();
        if (rentCarView.getFullFuelCheckBox().isSelected()) {
            decorators.add(new FullFuelDecorator(selectedCar));
        }
        if (rentCarView.getInsuranceCheckBox().isSelected()) {
            decorators.add(new InsuranceDecorator(selectedCar));
        }

        double estimatedCost = carRentalFacade.calculateTotalCost(selectedCar, pricingStrategy, selectedDuration, decorators);
        rentCarView.getTotalCostLabel().setText(String.format("Estimated Cost: $%.2f", estimatedCost));
    }

    private PricingStrategy getPricingStrategy(String pricingModel) {
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

        List<CarDecorator> decorators = new ArrayList<>();
        if (rentCarView.getFullFuelCheckBox().isSelected()) {
            decorators.add(new FullFuelDecorator(selectedCar));
        }
        if (rentCarView.getInsuranceCheckBox().isSelected()) {
            decorators.add(new InsuranceDecorator(selectedCar));
        }

        PricingStrategy pricingStrategy = getPricingStrategy(selectedPricingModel);

        double totalCost = carRentalFacade.calculateTotalCost(selectedCar, pricingStrategy, selectedDuration, decorators);

        if (currentUser.getBudget() < totalCost) {
            JOptionPane.showMessageDialog(rentCarView,
                    String.format("Insufficient funds. You need $%.2f more.", totalCost - currentUser.getBudget()));
            return;
        }

        carRentalFacade.processReservation(selectedCar, currentUser, selectedPricingModel, selectedDuration, totalCost, startDateTime);
        refreshTableAndBudget();
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true);
        rentCarView.dispose();
    }
}