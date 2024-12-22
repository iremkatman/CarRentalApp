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
    private void undoLastAction() {
        commandInvoker.undoLastCommand(); // Undo işlemini tetikleyin
        populateRentedCars(); // Tabloyu güncelleyin
        JOptionPane.showMessageDialog(rentedCarsView, "Last action undone.");
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

        int reservationId = (int) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 0);
        int carId = (int) rentedCarsView.getRentedCarTable().getValueAt(selectedRow, 1);

        ReturnCarCommand returnCarCommand = new ReturnCarCommand(reservationId, carId);
        commandInvoker.executeCommand(returnCarCommand);

        populateRentedCars();
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true);
        rentedCarsView.dispose();
    }
}
