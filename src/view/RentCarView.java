package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class RentCarView extends JFrame {
    private JTable carTable;
    private JButton rentButton, backButton;
    private JComboBox<String> pricingModelComboBox;
    private JCheckBox fullFuelCheckBox, insuranceCheckBox;
    private JLabel currentBalanceLabel, totalCostLabel;

    public RentCarView(User currentUser) {
        setTitle("Rent a Car");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel with Balance
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Available Cars", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        currentBalanceLabel = new JLabel("Current Balance: $" + currentUser.getBudget(), SwingConstants.RIGHT);
        currentBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        currentBalanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(currentBalanceLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Car Table with Base Price
        String[] columnNames = {"Model", "Type", "Base Price"};
        Object[][] data = {}; // Data will be populated by the controller
        carTable = new JTable(data, columnNames);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // Right Panel with options
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fullFuelCheckBox = new JCheckBox("Full Tank Fuel (+$50)");
        insuranceCheckBox = new JCheckBox("Daily Temporary Insurance (+$30)");
        pricingModelComboBox = new JComboBox<>(new String[]{
                "Hourly - No Discount",
                "Daily - 10% Discount",
                "Weekly - 25% Discount"
        });

        JLabel pricingLabel = new JLabel("Select a pricing model:", SwingConstants.LEFT);
        JLabel noteLabel = new JLabel("Select a pricing model to see the discount applied.", SwingConstants.LEFT);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        noteLabel.setForeground(Color.BLUE);

        totalCostLabel = new JLabel("Total Cost: $0.0", SwingConstants.LEFT);
        totalCostLabel.setFont(new Font("Arial", Font.BOLD, 14));

        rightPanel.add(pricingLabel);
        rightPanel.add(pricingModelComboBox);
        rightPanel.add(fullFuelCheckBox);
        rightPanel.add(insuranceCheckBox);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(noteLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(totalCostLabel);

        add(rightPanel, BorderLayout.EAST);

        // Bottom Panel with buttons
        JPanel bottomPanel = new JPanel();
        rentButton = new JButton("Rent Selected Car");
        backButton = new JButton("Back");

        bottomPanel.add(rentButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JTable getCarTable() {
        return carTable;
    }

    public JButton getRentButton() {
        return rentButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JComboBox<String> getPricingModelComboBox() {
        return pricingModelComboBox;
    }

    public JCheckBox getFullFuelCheckBox() {
        return fullFuelCheckBox;
    }

    public JCheckBox getInsuranceCheckBox() {
        return insuranceCheckBox;
    }

    public JLabel getCurrentBalanceLabel() {
        return currentBalanceLabel;
    }

    public JLabel getTotalCostLabel() {
        return totalCostLabel;
    }
}
