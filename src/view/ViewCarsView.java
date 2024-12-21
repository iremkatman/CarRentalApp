package view;

import javax.swing.*;
import java.awt.*;

public class ViewCarsView extends JFrame {
    private JTable carTable;
    private JButton backButton;

    public ViewCarsView() {
        setTitle("View Available Cars");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Available Cars", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.NORTH);

        carTable = new JTable(); // Populate this table with car data
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        backButton = new JButton("Back");
        add(backButton, BorderLayout.SOUTH);
    }

    public JTable getCarTable() {
        return carTable;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
