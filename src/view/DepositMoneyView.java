package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class DepositMoneyView extends JFrame {
    private JTextField amountField;
    private JButton depositButton;
    private JButton backButton;
    private JLabel currentBalanceLabel;

    public DepositMoneyView(User currentUser) {
        setTitle("Deposit Money");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        // Display current balance
        currentBalanceLabel = new JLabel("Current Balance: $" + currentUser.getBudget(), SwingConstants.CENTER);
        add(currentBalanceLabel);

        add(new JLabel("Enter amount to deposit:", SwingConstants.CENTER));
        amountField = new JTextField();
        add(amountField);

        JPanel buttonPanel = new JPanel();
        depositButton = new JButton("Deposit");
        backButton = new JButton("Back");
        buttonPanel.add(depositButton);
        buttonPanel.add(backButton);

        add(buttonPanel);
    }

    public JTextField getAmountField() {
        return amountField;
    }

    public JButton getDepositButton() {
        return depositButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JLabel getCurrentBalanceLabel() {
        return currentBalanceLabel;
    }
}
