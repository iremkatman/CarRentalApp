package facade;

import model.PricingStrategy;
import model.*;
import service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.util.List;

public class CarRentalFacade {

    private final CarService carService;
    private final ReservationService reservationService;
    private final UserService userService;

    public CarRentalFacade() {
        this.carService = new CarService();
        this.reservationService = new ReservationService();
        this.userService = new UserService();
    }

    public List<Car> fetchAvailableCars() {
        return carService.fetchAvailableCars();
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

    public double calculateTotalCost(Car car, PricingStrategy pricingStrategy, int duration, List<CarDecorator> decorators) {
        double baseCost = pricingStrategy.calculatePrice(car.getBasePrice(), duration);
        for (CarDecorator decorator : decorators) {
            baseCost += decorator.getCost(duration);
        }
        return baseCost;
    }

    public void processReservation(Car selectedCar, User currentUser, String pricingModel, int duration, double totalCost, LocalDateTime startDateTime) {
        LocalDateTime returnDate = calculateReturnDate(pricingModel, startDateTime, duration);

        reservationService.createReservation(
                selectedCar.getId(), currentUser.getId(), startDateTime, returnDate);
        carService.updateCarAvailability(selectedCar.getId(), false);
        userService.updateUserBudget(currentUser.getId(), currentUser.getBudget() - totalCost);

        currentUser.setBudget(currentUser.getBudget() - totalCost);

        JOptionPane.showMessageDialog(null,
                "Car rented successfully: " + selectedCar.getModel() +
                        "\nTotal Cost: $" + totalCost +
                        "\nReturn Date: " + returnDate);
    }

    private LocalDateTime calculateReturnDate(String pricingModel, LocalDateTime startDateTime, int duration) {
        switch (pricingModel) {
            case "Hourly - No Discount":
                return startDateTime.plusHours(duration);
            case "Daily - 10% Discount":
                return startDateTime.plusDays(duration);
            case "Weekly - 25% Discount":
                return startDateTime.plusWeeks(duration);
            default:
                throw new IllegalArgumentException("Invalid pricing model.");
        }
    }
}
