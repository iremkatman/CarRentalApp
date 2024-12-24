package view;

import model.Observer;
import model.User;

import javax.swing.*;
import java.awt.*;

public class WelcomeView extends JFrame implements Observer {
    private JLabel budgetLabel;
    private JButton rentCarButton, viewCarsButton, rentedCarsButton, depositMoneyButton, exitButton;

    public WelcomeView(User currentUser) {
        setTitle("Welcome");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Observer olarak kendini ekle
        currentUser.addObserver(this);

        // Bütçe Label
        budgetLabel = new JLabel("Current Balance: $" + currentUser.getBudget(), SwingConstants.CENTER);
        budgetLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(budgetLabel, BorderLayout.NORTH);

        // Welcome Label
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName());
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(welcomeLabel, BorderLayout.NORTH);

        JLabel descriptionLabel = new JLabel("Welcome to RentCar - your one-stop application for all car rental needs. Effortlessly rent, manage, and return vehicles with just a few clicks. Explore our seamless features and enjoy hassle-free service.", SwingConstants.CENTER);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(descriptionLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        rentCarButton = new JButton("Rent a Car");
        viewCarsButton = new JButton("View Available Cars");
        rentedCarsButton = new JButton("View Rented Cars");
        depositMoneyButton = new JButton("Deposit Money");
        exitButton = new JButton("Exit");

        rentCarButton.setBackground(new Color(60, 179, 113));
        rentCarButton.setForeground(Color.WHITE);
        viewCarsButton.setBackground(new Color(255, 165, 0));
        viewCarsButton.setForeground(Color.WHITE);
        rentedCarsButton.setBackground(new Color(218, 112, 214));
        rentedCarsButton.setForeground(Color.WHITE);
        depositMoneyButton.setBackground(new Color(70, 130, 180));
        depositMoneyButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);

        buttonPanel.add(rentCarButton);
        buttonPanel.add(viewCarsButton);
        buttonPanel.add(rentedCarsButton);
        buttonPanel.add(depositMoneyButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.WEST);

        // Right Panel with Image
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        JLabel pictureLabel = new JLabel();
        pictureLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pictureLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        ImageIcon carImage = new ImageIcon("src/resources/main.jpeg");
        Image scaledImage = carImage.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        pictureLabel.setIcon(new ImageIcon(scaledImage));

        rightPanel.add(pictureLabel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Set visible
        setVisible(true);
    }

    // Observer'dan gelen güncellemeleri işlemek için metot
    @Override
    public void update(double newBudget) {
        budgetLabel.setText("Current Balance: $" + newBudget);
    }

    public JButton getRentCarButton() {
        return rentCarButton;
    }

    public JButton getViewCarsButton() {
        return viewCarsButton;
    }

    public JButton getRentedCarsButton() {
        return rentedCarsButton;
    }

    public JButton getDepositMoneyButton() {
        return depositMoneyButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
