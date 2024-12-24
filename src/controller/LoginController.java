package controller;

import model.User;
import view.AdminView;
import view.LoginView;
import view.RegisterView;
import view.WelcomeView;

import javax.swing.*;

public class LoginController {
    private LoginView loginView;
    private UserController userController;

    public LoginController(LoginView loginView, UserController userController) {
        this.loginView = loginView;
        this.userController = userController;

        loginView.getLoginButton().addActionListener(e -> login());
        loginView.getRegisterButton().addActionListener(e -> openSignupView());
    }

    private void login() {
        String username = loginView.getUsernameField().getText().trim();
        String password = new String(loginView.getPasswordField().getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Username and password cannot be empty.");
            return;
        }

        // Admin giriş kontrolü
        if (username.equals("admin") && password.equals("admin123")) {
            JOptionPane.showMessageDialog(loginView, "Login Successful! Welcome, Admin.");
            AdminView adminView = new AdminView(); // AdminView, admin için GUI'yi temsil eder
            new AdminController(adminView); // AdminController, admin işlemlerini yönetir
            adminView.setVisible(true);
            loginView.dispose();
            return;
        }

        // Kullanıcı giriş kontrolü
        User user = userController.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(loginView, "Login Successful! Welcome, " + user.getFirstName());
            WelcomeView welcomeView = new WelcomeView(user);
            new WelcomeController(welcomeView, user);
            welcomeView.setVisible(true);
            loginView.dispose();
        } else {
            JOptionPane.showMessageDialog(loginView, "Invalid username or password.");
        }
    }

    private void openSignupView() {
        RegisterView signupView = new RegisterView();
        new RegisterController(signupView, userController);
        signupView.setVisible(true);
        loginView.dispose();
    }
}
