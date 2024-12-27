package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class RentedCarsView extends JFrame {
    private JTable rentedCarTable;
    private JButton backButton;
    private JButton cancelButton;
    private JButton returnButton;
    private JButton undoButton;

    public RentedCarsView(User currentUser) {
        setTitle("Rented Cars");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Rented Cars for " + currentUser.getFirstName() + " " + currentUser.getLastName(), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(label, BorderLayout.NORTH);

        rentedCarTable = new JTable();
        rentedCarTable.setRowHeight(30);
        rentedCarTable.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(rentedCarTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        backButton = new JButton("Back");
        cancelButton = new JButton("Cancel Rental");
        returnButton = new JButton("Return Car");
        undoButton = new JButton("Undo");
        buttonPanel.add(backButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(undoButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JTable getRentedCarTable() {
        return rentedCarTable;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getReturnButton() {
        return returnButton;
    }

    public JButton getUndoButton() {
        return undoButton;
    }
}
