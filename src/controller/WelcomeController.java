package controller;

import model.User;
import view.*;

import javax.swing.*;

public class WelcomeController {
    private WelcomeView welcomeView;
    private User currentUser;

    public WelcomeController(WelcomeView welcomeView, User currentUser) {
        this.welcomeView = welcomeView;
        this.currentUser = currentUser;

        welcomeView.getRentCarButton().addActionListener(e -> openRentCarView());
        welcomeView.getViewCarsButton().addActionListener(e -> openViewCarsView());
        welcomeView.getRentedCarsButton().addActionListener(e -> openRentedCarsView());
        welcomeView.getDepositMoneyButton().addActionListener(e -> openDepositMoneyView());
        welcomeView.getExitButton().addActionListener(e -> exitToLogin());
    }

    private void openRentCarView() {
        RentCarView rentCarView = new RentCarView(currentUser);
        new RentCarController(rentCarView, currentUser, welcomeView);
        rentCarView.setVisible(true);
        welcomeView.setVisible(false);
    }

    private void openViewCarsView() {
        ViewCarsView viewCarsView = new ViewCarsView();
        new ViewCarsController(viewCarsView, welcomeView);
        viewCarsView.setVisible(true);
        welcomeView.setVisible(false);
    }

    private void openRentedCarsView() {
        RentedCarsView rentedCarsView = new RentedCarsView(currentUser);
        RentedCarsController rentedCarsController = new RentedCarsController(rentedCarsView, currentUser, welcomeView);

        if (!rentedCarsController.populateRentedCars()) {
            JOptionPane.showMessageDialog(welcomeView, "No rented cars found.");
            return;
        }

        rentedCarsView.setVisible(true);
        welcomeView.setVisible(false);
    }

    private void openDepositMoneyView() {
        DepositMoneyView depositMoneyView = new DepositMoneyView(currentUser);
        new DepositMoneyController(depositMoneyView, currentUser, welcomeView);
        depositMoneyView.setVisible(true);
        welcomeView.setVisible(false);
    }

    private void exitToLogin() {
        welcomeView.setVisible(false);
        LoginView loginView = new LoginView();
        new LoginController(loginView, new UserController());
        loginView.setVisible(true);
    }
}
