// AdminView.java
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminView extends JFrame {
    private JTable carsTable;
    private JButton addCarButton;
    private JButton modifyCarButton;
    private JButton removeCarButton;
    private JButton exitButton;

    public AdminView() {
        setTitle("Admin Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome Admin", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        carsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(carsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        addCarButton = new JButton("Add Car");
        modifyCarButton = new JButton("Modify Car");
        removeCarButton = new JButton("Remove Car");
        exitButton = new JButton("Exit");

        buttonPanel.add(addCarButton);
        buttonPanel.add(modifyCarButton);
        buttonPanel.add(removeCarButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public JTable getCarTable() {
        return carsTable;
    }

    public JButton getAddCarButton() {
        return addCarButton;
    }

    public JButton getModifyCarButton() {
        return modifyCarButton;
    }

    public JButton getRemoveCarButton() {
        return removeCarButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
