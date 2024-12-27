package controller;

import command.CancelRentalCommand;
import command.CommandInvoker;
import command.ReturnCarCommand;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RentedCarsController {
    private RentedCarsView rentedCarsView;
    private User currentUser;
    private WelcomeView welcomeView;
    private Connection connection;
    private CommandInvoker commandInvoker;

    public RentedCarsController(RentedCarsView rentedCarsView, User currentUser, WelcomeView welcomeView) {
        this.rentedCarsView = rentedCarsView;
        this.currentUser = currentUser;
        this.welcomeView = welcomeView;
        this.connection = SingletonConnection.getInstance();
        this.commandInvoker = new CommandInvoker();

        populateRentedCars();
        rentedCarsView.getBackButton().addActionListener(e -> goBackToWelcome());
        rentedCarsView.getCancelButton().addActionListener(e -> cancelSelectedRental());
        rentedCarsView.getReturnButton().addActionListener(e -> returnSelectedCar());
        rentedCarsView.getUndoButton().addActionListener(e -> undoLastAction());

    }

    boolean populateRentedCars() {
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
            rentedCarsView.getRentedCarTable().getColumnModel().getColumn(0).setMinWidth(0);
            rentedCarsView.getRentedCarTable().getColumnModel().getColumn(0).setMaxWidth(0);
            rentedCarsView.getRentedCarTable().getColumnModel().getColumn(1).setMinWidth(0);
            rentedCarsView.getRentedCarTable().getColumnModel().getColumn(1).setMaxWidth(0);
            if (model.getRowCount() == 0) {
                return false;

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }
    private void undoLastAction() {
        if (commandInvoker.canUndo()) {
            commandInvoker.undoLastCommand(); // Undo işlemini tetikleyin
            populateRentedCars(); // Tabloyu güncelleyin
            JOptionPane.showMessageDialog(rentedCarsView, "Last action undone.");
        } else {
            JOptionPane.showMessageDialog(rentedCarsView, "Nothing to undo.");
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

        CancelRentalCommand cancelRentalCommand = new CancelRentalCommand(reservationId, carId);
        commandInvoker.executeCommand(cancelRentalCommand);

        populateRentedCars();
    }

    private void returnSelectedCar() {
        int selectedRow = rentedCarsView.getRentedCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(rentedCarsView, "Please select a car to return.");
            return;
        }

        String startDateStr = (String) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 3);
        String endDateStr = (String) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 4);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            LocalDate startDate = LocalDateTime.parse(startDateStr, formatter).toLocalDate();
            LocalDate endDate = LocalDateTime.parse(endDateStr, formatter).toLocalDate();

            if (LocalDate.now().isBefore(startDate)) {
                JOptionPane.showMessageDialog(rentedCarsView,
                        "You cannot return the car before the start date, but you can cancel the reservation.");
                return;
            }

            if (LocalDate.now().isBefore(endDate)) {
                JOptionPane.showMessageDialog(rentedCarsView, "You cannot return the car before the end date.");
                return;
            }

            int reservationId = (int) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 0);
            int carId = (int) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 1);

            ReturnCarCommand returnCarCommand = new ReturnCarCommand(reservationId, carId);
            commandInvoker.executeCommand(returnCarCommand);

            JOptionPane.showMessageDialog(rentedCarsView, "Car successfully returned.");
            populateRentedCars();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rentedCarsView, "Error processing dates. Please contact support.");
            e.printStackTrace();
        }
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true);
        rentedCarsView.dispose();
    }
}


