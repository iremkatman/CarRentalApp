package controller;

import database.SingletonConnection;
import model.User;
import view.DepositMoneyView;
import view.WelcomeView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepositMoneyController {
    private DepositMoneyView depositMoneyView;
    private User currentUser;
    private WelcomeView welcomeView;
    private Connection connection;

    public DepositMoneyController(DepositMoneyView depositMoneyView, User currentUser, WelcomeView welcomeView) {
        this.depositMoneyView = depositMoneyView;
        this.currentUser = currentUser;
        this.welcomeView = welcomeView;
        this.connection =SingletonConnection.getInstance();

        loadUserBudget();
        depositMoneyView.getDepositButton().addActionListener(e -> depositMoney());
        depositMoneyView.getBackButton().addActionListener(e -> goBackToWelcome());
    }

    private void loadUserBudget() {
        try {
            String query = "SELECT budget FROM users WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double budget = rs.getDouble("budget");
                currentUser.setBudget(budget);
                depositMoneyView.getCurrentBalanceLabel().setText("Current Balance: $" + budget);
            } else {
                JOptionPane.showMessageDialog(depositMoneyView, "User not found in the database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(depositMoneyView, "Failed to load user budget from database.");
        }
    }

    private void depositMoney() {
        String amountText = depositMoneyView.getAmountField().getText();
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(depositMoneyView, "Amount must be positive!");
                return;
            }

            try {
                String updateBudgetQuery = "UPDATE users SET budget = budget + ? WHERE id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateBudgetQuery);
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, currentUser.getId());
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected > 0) {
                    currentUser.setBudget(currentUser.getBudget() + amount);
                    JOptionPane.showMessageDialog(depositMoneyView, "Deposit successful!");
                } else {
                    JOptionPane.showMessageDialog(depositMoneyView, "Failed to update user budget in the database.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(depositMoneyView, "Database update failed! Error: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(depositMoneyView, "Invalid amount entered!");
        }
    }


    private void goBackToWelcome() {
        welcomeView.setVisible(true);
        depositMoneyView.dispose();
    }
}
