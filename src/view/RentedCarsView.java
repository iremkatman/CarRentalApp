package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class RentedCarsView extends JFrame {
    private JTable rentedCarTable;
    private JButton backButton;

    public RentedCarsView(User currentUser) {
        setTitle("Rented Cars");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Rented Cars for " + currentUser.getFirstName() + " " + currentUser.getLastName(), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.NORTH);

        rentedCarTable = new JTable(); // Table will be populated with data
        add(new JScrollPane(rentedCarTable), BorderLayout.CENTER);

        backButton = new JButton("Back");
        add(backButton, BorderLayout.SOUTH);
    }

    public JTable getRentedCarTable() {
        return rentedCarTable;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
