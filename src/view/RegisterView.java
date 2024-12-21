package view;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton goToLoginButton;

    public RegisterView() {
        setTitle("Register");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Left side - Car Image
        JLabel carImageLabel = new JLabel();
        carImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon carImage = new ImageIcon("src/resources/register.jpeg");
        Image scaledCarImage = carImage.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        carImageLabel.setIcon(new ImageIcon(scaledCarImage));
        add(carImageLabel, BorderLayout.WEST);

        // Right side - Register form
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel registerLabel = new JLabel("Register");
        registerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerPanel.add(registerLabel, gbc);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registerPanel.add(firstNameLabel, gbc);

        firstNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        registerPanel.add(firstNameField, gbc);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerPanel.add(lastNameLabel, gbc);

        lastNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        registerPanel.add(lastNameField, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        registerPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        registerPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        registerPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        registerPanel.add(passwordField, gbc);

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(70, 130, 180)); // Different color for Register button
        registerButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        registerPanel.add(registerButton, gbc);

        // Add "Go to Login" section
        JLabel signedInLabel = new JLabel("If you are already signed in, you can go to Login.");
        signedInLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        signedInLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        registerPanel.add(signedInLabel, gbc);

        goToLoginButton = new JButton("Go to Login");
        goToLoginButton.setBackground(new Color(60, 179, 113));
        goToLoginButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        registerPanel.add(goToLoginButton, gbc);

        add(registerPanel, BorderLayout.CENTER);

        // Set visible
        setVisible(true);
    }

    public JTextField getFirstNameField() {
        return firstNameField;
    }

    public JTextField getLastNameField() {
        return lastNameField;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JButton getGoToLoginButton() {
        return goToLoginButton;
    }


    }

