package controller;

import database.SingletonConnection;
import model.User;
import view.RentedCarsView;
import view.WelcomeView;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RentedCarsController {
    private RentedCarsView rentedCarsView;
    private User currentUser;
    private WelcomeView welcomeView;

    public RentedCarsController(RentedCarsView rentedCarsView, User currentUser, WelcomeView welcomeView) {
        this.rentedCarsView = rentedCarsView;
        this.currentUser = currentUser;
        this.welcomeView = welcomeView;

        populateRentedCars();
        rentedCarsView.getBackButton().addActionListener(e -> goBackToWelcome());
    }

    private void populateRentedCars() {
        try (Connection connection = SingletonConnection.getInstance()) {
            String query = "SELECT car_id, car_model, start_date, end_date FROM rented_cars WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"Car ID", "Car Model", "Start Date", "End Date"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("car_id"),
                        rs.getString("car_model"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                });
            }
            rentedCarsView.getRentedCarTable().setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true); // Re-show the WelcomeView
        rentedCarsView.dispose();    // Dispose the RentedCarsView
    }
}
