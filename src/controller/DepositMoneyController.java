package controller;

import model.User;
import view.DepositMoneyView;
import view.WelcomeView;

import javax.swing.*;

public class DepositMoneyController {
    private DepositMoneyView depositMoneyView;
    private User currentUser;
    private WelcomeView welcomeView;

    public DepositMoneyController(DepositMoneyView depositMoneyView, User currentUser, WelcomeView welcomeView) {
        this.depositMoneyView = depositMoneyView;
        this.currentUser = currentUser;
        this.welcomeView = welcomeView;

        depositMoneyView.getDepositButton().addActionListener(e -> depositMoney());
        depositMoneyView.getBackButton().addActionListener(e -> goBackToWelcome());
    }

    private void depositMoney() {
        String amountText = depositMoneyView.getAmountField().getText();
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(depositMoneyView, "Amount must be positive!");
                return;
            }

            // Kullanıcı bütçesini güncelle
            currentUser.setBudget(currentUser.getBudget() + amount);

            depositMoneyView.getCurrentBalanceLabel().setText("Current Balance: $" + currentUser.getBudget());
            JOptionPane.showMessageDialog(depositMoneyView, "Deposit successful!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(depositMoneyView, "Invalid amount entered!");
        }
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true); // Re-show the WelcomeView
        depositMoneyView.dispose();  // Dispose the DepositMoneyView
    }
}
