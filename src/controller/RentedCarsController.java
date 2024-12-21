package controller;

import database.SingletonConnection;
import model.User;
import view.RentedCarsView;
import view.WelcomeView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentedCarsController {
    private RentedCarsView rentedCarsView;
    private User currentUser;
    private WelcomeView welcomeView;
    private Connection connection;

    public RentedCarsController(RentedCarsView rentedCarsView, User currentUser, WelcomeView welcomeView) {
        this.rentedCarsView = rentedCarsView;
        this.currentUser = currentUser;
        this.welcomeView = welcomeView;
        this.connection = SingletonConnection.getInstance();

        populateRentedCars();
        rentedCarsView.getBackButton().addActionListener(e -> goBackToWelcome());
        rentedCarsView.getCancelButton().addActionListener(e -> cancelSelectedRental());
        rentedCarsView.getReturnButton().addActionListener(e -> returnSelectedCar());
    }

    private void populateRentedCars() {
        String query = """
                SELECT 
                    r.id AS reservation_id, 
                    c.id AS car_id,
                    c.model AS car_model, 
                    r.reservation_date_start AS start_date, 
                    r.reservation_date_end AS end_date 
                FROM 
                    reservations r 
                JOIN 
                    cars c ON r.car_id = c.id 
                WHERE 
                    r.user_id = ?;
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"Reservation ID", "Car ID", "Car Model", "Start Date", "End Date"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("reservation_id"),
                        rs.getInt("car_id"),
                        rs.getString("car_model"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                });
            }

            rentedCarsView.getRentedCarTable().setModel(model);

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(rentedCarsView, "No rented cars found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelSelectedRental() {
        int selectedRow = rentedCarsView.getRentedCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(rentedCarsView, "Please select a rental to cancel.");
            return;
        }

        int reservationId = (int) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 0);
        int carId = (int) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 1);
        String startDateStr = (String) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 3);

        // Eğer kiralama başlamışsa iptal edilemez
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!LocalDate.now().isBefore(startDate)) {
            JOptionPane.showMessageDialog(rentedCarsView, "The rental has already started and cannot be cancelled.");
            return;
        }

        try {
            String deleteQuery = "DELETE FROM reservations WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
                stmt.setInt(1, reservationId);
                stmt.executeUpdate();
            }

            String updateCarQuery = "UPDATE cars SET availability = TRUE WHERE id = ?";
            try (PreparedStatement carStmt = connection.prepareStatement(updateCarQuery)) {
                carStmt.setInt(1, carId);
                carStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(rentedCarsView, "Rental cancelled successfully.");
            populateRentedCars();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returnSelectedCar() {
        int selectedRow = rentedCarsView.getRentedCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(rentedCarsView, "Please select a car to return.");
            return;
        }

        int reservationId = (int) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 0);
        int carId = (int) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 1);
        String endDateStr = (String) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 4);

        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (LocalDate.now().isBefore(endDate)) {
            JOptionPane.showMessageDialog(rentedCarsView, "The car cannot be returned before the end date.");
            return;
        }

        try {
            String deleteQuery = "DELETE FROM reservations WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
                stmt.setInt(1, reservationId);
                stmt.executeUpdate();
            }

            String updateCarQuery = "UPDATE cars SET availability = TRUE WHERE id = ?";
            try (PreparedStatement carStmt = connection.prepareStatement(updateCarQuery)) {
                carStmt.setInt(1, carId);
                carStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(rentedCarsView, "Car returned successfully.");
            populateRentedCars();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true);
        rentedCarsView.dispose();
    }
}
