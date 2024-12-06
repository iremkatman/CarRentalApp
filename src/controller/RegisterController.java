package controller;

import model.User;
import view.SignupView;

import javax.swing.*;

public class SignupController {
    private SignupView signupView;
    private UserController userController;

    public SignupController(SignupView signupView, UserController userController) {
        this.signupView = signupView;
        this.userController = userController;

        signupView.getSignupButton().addActionListener(e -> createAccount());
    }

    private void createAccount() {
        String username = signupView.getUsernameField().getText();
        String password = new String(signupView.getPasswordField().getPassword());
        String role = (String) signupView.getRoleComboBox().getSelectedItem();

        User newUser = new User(username, password, role);
        boolean success = userController.registerUser(newUser);

        if (success) {
            JOptionPane.showMessageDialog(signupView, "Account created successfully!");
            signupView.dispose();
        } else {
            JOptionPane.showMessageDialog(signupView, "Failed to create account. Username may already exist.");
        }
    }
}
