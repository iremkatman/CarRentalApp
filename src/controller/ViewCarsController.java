package controller;

import database.SingletonConnection;
import view.ViewCarsView;
import view.WelcomeView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewCarsController {
    private ViewCarsView viewCarsView;
    private WelcomeView welcomeView;
    private Connection connection = SingletonConnection.getInstance();

    public ViewCarsController(ViewCarsView viewCarsView, WelcomeView welcomeView) {
        this.viewCarsView = viewCarsView;
        this.welcomeView = welcomeView;

        populateCarTable();
        setupFilters();
        setupSelectionListener();

        viewCarsView.getBackButton().addActionListener(e -> goBackToWelcome());
    }

    private void populateCarTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Model", "Type", "Price per Day"}, 0);
        String query = "SELECT * FROM cars WHERE availability = TRUE";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getDouble("price_per_day")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewCarsView.getCarTable().setModel(model);
    }

    private void setupFilters() {
        viewCarsView.getFilterButton().addActionListener(e -> {
            String selectedModel = viewCarsView.getModelFilterField().getText();
            String selectedType = (String) viewCarsView.getTypeFilterComboBox().getSelectedItem();

            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Model", "Type", "Price per Day"}, 0);
            StringBuilder query = new StringBuilder("SELECT * FROM cars WHERE availability = TRUE");

            if (!selectedModel.isEmpty()) {
                query.append(" AND model LIKE ?");
            }
            if (!"All".equals(selectedType)) {
                query.append(" AND type = ?");
            }

            try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
                int index = 1;
                if (!selectedModel.isEmpty()) {
                    stmt.setString(index++, "%" + selectedModel + "%");
                }
                if (!"All".equals(selectedType)) {
                    stmt.setString(index, selectedType);
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getInt("id"),
                                rs.getString("model"),
                                rs.getString("type"),
                                rs.getDouble("price_per_day")
                        });
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            viewCarsView.getCarTable().setModel(model);
        });
    }

    private void setupSelectionListener() {
        viewCarsView.getCarTable().getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = viewCarsView.getCarTable().getSelectedRow();
            if (selectedRow >= 0) {
                String selectedModel = (String) viewCarsView.getCarTable().getValueAt(selectedRow, 1);
                displayCarImage(selectedModel);
            }
        });
    }

    private ImageIcon resizeImage(String imagePath, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image img = imageIcon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private void displayCarImage(String carModel) {
        String imagePath = "src/resources/" + carModel.replaceAll(" ", "_").toLowerCase() + ".jpeg";
        ImageIcon carImage = new ImageIcon(imagePath);
        if (carImage.getIconWidth() == -1) {
            viewCarsView.getCarImageLabel().setIcon(null);
            viewCarsView.getCarImageLabel().setText("Image not found");
        } else {
            viewCarsView.getCarImageLabel().setIcon(carImage);
            viewCarsView.getCarImageLabel().setText("");
        }
        ImageIcon resizedImage = resizeImage(imagePath, 400, 300);
        viewCarsView.getCarImageLabel().setIcon(resizedImage);
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true);
        viewCarsView.dispose();
    }
}
