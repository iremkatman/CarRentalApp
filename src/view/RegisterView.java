package view;

import javax.swing.*;
import java.awt.*;

public class SignupView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton signupButton;

    public SignupView() {
        setTitle("Car Rental System - Signup");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"customer", "admin"});
        add(roleComboBox);

        signupButton = new JButton("Create Account");
        add(signupButton);
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JComboBox<String> getRoleComboBox() {
        return roleComboBox;
    }

    public JButton getSignupButton() {
        return signupButton;
    }
}
