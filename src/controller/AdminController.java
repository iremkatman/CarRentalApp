package controller;

import database.SingletonConnection;
import view.AdminView;
import view.LoginView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminController {
    private AdminView adminView;
    private Connection connection;

    public AdminController(AdminView adminView) {
        this.adminView = adminView;
        this.connection = SingletonConnection.getInstance();

        populateCarTable();

        adminView.getAddCarButton().addActionListener(e -> addCar());
        adminView.getModifyCarButton().addActionListener(e -> modifyCar());
        adminView.getRemoveCarButton().addActionListener(e -> removeCar());
        adminView.getExitButton().addActionListener(e -> exitToLogin());
    }

    private void populateCarTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Model", "Type", "Available", "Price per Day"}, 0);
        String query = "SELECT * FROM cars";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getBoolean("availability"),
                        rs.getDouble("price_per_day")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adminView.getCarTable().setModel(model);
    }

    private void addCar() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField modelField = new JTextField();
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"SUV", "Hatchback", "Sedan", "Sports", "Luxury"});
        JCheckBox availabilityCheckBox = new JCheckBox("Available", true);
        JTextField priceField = new JTextField();
        JButton uploadButton = new JButton("Upload Image");

        final String[] imagePath = new String[1];

        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePath[0] = selectedFile.getAbsolutePath();
            }
        });

        panel.add(new JLabel("Model:"));
        panel.add(modelField);
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);
        panel.add(new JLabel("Available:"));
        panel.add(availabilityCheckBox);
        panel.add(new JLabel("Price per Day:"));
        panel.add(priceField);
        panel.add(uploadButton);

        int result = JOptionPane.showConfirmDialog(adminView, panel, "Add Car", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String model = modelField.getText().trim();
            String type = (String) typeComboBox.getSelectedItem();
            boolean availability = availabilityCheckBox.isSelected();
            double price;

            try {
                price = Double.parseDouble(priceField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(adminView, "Invalid price value.");
                return;
            }

            if (model.isEmpty() || imagePath[0] == null) {
                JOptionPane.showMessageDialog(adminView, "Model and image are required.");
                return;
            }

            try {
                // Copy image to resources folder
                String destinationPath = "src/resources/" + model.replaceAll(" ", "_").toLowerCase() + ".jpeg";
                Files.copy(new File(imagePath[0]).toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

                String insertQuery = "INSERT INTO cars (model, type, availability, price_per_day) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                    stmt.setString(1, model);
                    stmt.setString(2, type);
                    stmt.setBoolean(3, availability);
                    stmt.setDouble(4, price);
                    stmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(adminView, "Car added successfully.");
                populateCarTable();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(adminView, "Failed to add car.");
            }
        }
    }

    private void modifyCar() {
        int selectedRow = adminView.getCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(adminView, "Please select a car to modify.");
            return;
        }

        int carId = (int) adminView.getCarTable().getValueAt(selectedRow, 0);
        String existingModel = (String) adminView.getCarTable().getValueAt(selectedRow, 1);
        String existingType = (String) adminView.getCarTable().getValueAt(selectedRow, 2);
        boolean existingAvailability = (boolean) adminView.getCarTable().getValueAt(selectedRow, 3);
        double existingPrice = (double) adminView.getCarTable().getValueAt(selectedRow, 4);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField modelField = new JTextField(existingModel);
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"SUV", "Hatchback", "Sedan", "Sports", "Luxury"});
        typeComboBox.setSelectedItem(existingType);
        JCheckBox availabilityCheckBox = new JCheckBox("Available", existingAvailability);
        JTextField priceField = new JTextField(String.valueOf(existingPrice));

        panel.add(new JLabel("Model:"));
        panel.add(modelField);
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);
        panel.add(new JLabel("Available:"));
        panel.add(availabilityCheckBox);
        panel.add(new JLabel("Price per Day:"));
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(adminView, panel, "Modify Car", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String model = modelField.getText().trim();
            String type = (String) typeComboBox.getSelectedItem();
            boolean availability = availabilityCheckBox.isSelected();
            double price;

            try {
                price = Double.parseDouble(priceField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(adminView, "Invalid price value.");
                return;
            }

            try {
                String updateQuery = "UPDATE cars SET model = ?, type = ?, availability = ?, price_per_day = ? WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                    stmt.setString(1, model);
                    stmt.setString(2, type);
                    stmt.setBoolean(3, availability);
                    stmt.setDouble(4, price);
                    stmt.setInt(5, carId);
                    stmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(adminView, "Car modified successfully.");
                populateCarTable();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(adminView, "Failed to modify car.");
            }
        }
    }

    private void removeCar() {
        int selectedRow = adminView.getCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(adminView, "Please select a car to remove.");
            return;
        }

        int carId = (int) adminView.getCarTable().getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(adminView, "Are you sure you want to remove this car?", "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                String deleteQuery = "DELETE FROM cars WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
                    stmt.setInt(1, carId);
                    stmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(adminView, "Car removed successfully.");
                populateCarTable();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(adminView, "Failed to remove car.");
            }
        }
    }

    private void exitToLogin() {
        LoginView loginView = new LoginView();
        UserController userController = new UserController();
        new LoginController(loginView, userController);
        loginView.setVisible(true);
        adminView.dispose();
    }
}
