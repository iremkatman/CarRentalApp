package controller;

import model.User;
import view.*;

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
        welcomeView.setVisible(false); // Hide the current view instead of disposing it
    }

    private void openViewCarsView() {
        ViewCarsView viewCarsView = new ViewCarsView();
        new ViewCarsController(viewCarsView, welcomeView);
        viewCarsView.setVisible(true);
        welcomeView.setVisible(false); // Hide the current view instead of disposing it
    }

    private void openRentedCarsView() {
        RentedCarsView rentedCarsView = new RentedCarsView(currentUser);
        new RentedCarsController(rentedCarsView, currentUser, welcomeView);
        rentedCarsView.setVisible(true);
        welcomeView.setVisible(false); // Hide the current view instead of disposing it
    }

    private void openDepositMoneyView() {
        DepositMoneyView depositMoneyView = new DepositMoneyView(currentUser);
        new DepositMoneyController(depositMoneyView, currentUser, welcomeView); // Pass the WelcomeView
        depositMoneyView.setVisible(true);
        welcomeView.setVisible(false); // Hide the WelcomeView
    }

    private void exitToLogin() {
        welcomeView.setVisible(false);
        LoginView loginView = new LoginView();
        new LoginController(loginView, new UserController());
        loginView.setVisible(true);
    }
}
