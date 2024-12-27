package view;

import model.Observer;
import model.User;

import javax.swing.*;
import java.awt.*;

public class DepositMoneyView extends JFrame implements Observer {
    private JTextField amountField;
    private JButton depositButton;
    private JButton backButton;
    private JLabel currentBalanceLabel;

    public DepositMoneyView(User currentUser) {
        setTitle("Deposit Money");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        currentUser.addObserver(this);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        currentBalanceLabel = new JLabel("Current Balance: $" + currentUser.getBudget(), SwingConstants.CENTER);
        currentBalanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentBalanceLabel.setForeground(new Color(34, 139, 34)); // Green color
        topPanel.add(currentBalanceLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1, 10, 10));
        JLabel amountLabel = new JLabel("Enter amount to deposit:", SwingConstants.CENTER);
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField = new JTextField();
        amountField.setHorizontalAlignment(JTextField.CENTER);
        centerPanel.add(amountLabel);
        centerPanel.add(amountField);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        depositButton = new JButton("Deposit");
        depositButton.setFont(new Font("Arial", Font.BOLD, 14));
        depositButton.setBackground(new Color(50, 205, 50)); // Green background
        depositButton.setForeground(Color.WHITE);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(220, 20, 60)); // Red background
        backButton.setForeground(Color.WHITE);

        bottomPanel.add(depositButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void update(double newBudget) {
        currentBalanceLabel.setText("Current Balance: $" + newBudget);
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
