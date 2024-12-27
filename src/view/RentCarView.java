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
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Rent A Car", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(169, 169, 169));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        currentBalanceLabel = new JLabel("Current Balance: $" + currentUser.getBudget(), SwingConstants.RIGHT);
        currentBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        currentBalanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(currentBalanceLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Model", "Type", "Base Price"};
        Object[][] data = {};
        carTable = new JTable(data, columnNames);
        carTable.setFillsViewportHeight(true);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel pricingLabel = new JLabel("Select a pricing model:", SwingConstants.LEFT);
        pricingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pricingLabel.setForeground(new Color(70, 130, 180));

        pricingModelComboBox = new JComboBox<>(new String[]{
                "Hourly - No Discount",
                "Daily - 10% Discount",
                "Weekly - 25% Discount"
        });
        pricingModelComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        fullFuelCheckBox = new JCheckBox("Full Tank Fuel (+$50)");
        fullFuelCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        fullFuelCheckBox.setBackground(new Color(240, 248, 255));

        insuranceCheckBox = new JCheckBox("Daily Temporary Insurance (+$30)");
        insuranceCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        insuranceCheckBox.setBackground(new Color(240, 248, 255));

        JLabel noteLabel = new JLabel("Select a pricing model to see the discount applied.", SwingConstants.LEFT);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        noteLabel.setForeground(Color.BLUE);

        totalCostLabel = new JLabel("Total Cost: $0.0", SwingConstants.LEFT);
        totalCostLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalCostLabel.setForeground(new Color(220, 20, 60));

        rightPanel.add(pricingLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(pricingModelComboBox);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(fullFuelCheckBox);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(insuranceCheckBox);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(noteLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(totalCostLabel);

        add(rightPanel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rentButton = new JButton("Rent Selected Car");
        rentButton.setFont(new Font("Arial", Font.BOLD, 14));
        rentButton.setBackground(new Color(169, 169, 169));
        rentButton.setForeground(Color.WHITE);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(220, 20, 60));
        backButton.setForeground(Color.WHITE);

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
