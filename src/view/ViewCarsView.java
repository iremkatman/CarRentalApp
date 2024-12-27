package view;

import javax.swing.*;
import java.awt.*;

public class ViewCarsView extends JFrame {
    private JTable carTable;
    private JTextField modelFilterField;
    private JComboBox<String> typeFilterComboBox;
    private JButton filterButton;
    private JButton backButton;
    private JLabel carImageLabel;

    public ViewCarsView() {
        setTitle("View Cars");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        modelFilterField = new JTextField();
        typeFilterComboBox = new JComboBox<>(new String[]{"All", "SUV", "Sedan", "Hatchback", "Sports", "Luxury"});
        filterButton = new JButton("Filter");
        topPanel.add(new JLabel("Model:"));
        topPanel.add(modelFilterField);
        topPanel.add(new JLabel("Type:"));
        topPanel.add(typeFilterComboBox);
        topPanel.add(filterButton);
        add(topPanel, BorderLayout.NORTH);

        carTable = new JTable();
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        carImageLabel = new JLabel("Select a car to view its image", SwingConstants.CENTER);
        carImageLabel.setPreferredSize(new Dimension(400, 300));
        carImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        carImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        bottomPanel.add(carImageLabel, BorderLayout.CENTER);

        backButton = new JButton("Back");
        bottomPanel.add(backButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public JTable getCarTable() {
        return carTable;
    }

    public JTextField getModelFilterField() {
        return modelFilterField;
    }

    public JComboBox<String> getTypeFilterComboBox() {
        return typeFilterComboBox;
    }

    public JButton getFilterButton() {
        return filterButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JLabel getCarImageLabel() {
        return carImageLabel;
    }
}
