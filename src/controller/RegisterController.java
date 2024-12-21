package controller;

import model.User;
import view.RegisterView;
import view.LoginView;

import javax.swing.*;
import java.util.regex.Pattern;

public class RegisterController {
    private RegisterView registerView;
    private UserController userController;

    public RegisterController(RegisterView registerView, UserController userController) {
        this.registerView = registerView;
        this.userController = userController;

        registerView.getRegisterButton().addActionListener(e -> register());
        registerView.getGoToLoginButton().addActionListener(e -> openLoginView());
    }

    private void register() {
        // Kullanıcı girişlerini al
        String firstName = registerView.getFirstNameField().getText().trim();
        String lastName = registerView.getLastNameField().getText().trim();
        String username = registerView.getUsernameField().getText().trim();
        String password = new String(registerView.getPasswordField().getPassword()).trim();

        // Boş alan kontrolü
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(registerView, "All fields are required!");
            return;
        }

        // Parola uzunluk kontrolü
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(registerView, "Password must be at least 6 characters long!");
            return;
        }

        // Kullanıcı adı format kontrolü
        if (!isValidUsername(username)) {
            JOptionPane.showMessageDialog(registerView, "Username can only contain letters, numbers, and underscores.");
            return;
        }

        // Yeni kullanıcı oluştur
        User newUser = new User(0, username, hashPassword(password), firstName, lastName, 0.0); // 0 ID otomatik atanacak
        boolean success = userController.registerUser(newUser);

        if (success) {
            JOptionPane.showMessageDialog(registerView, "Registration Successful!");
            openLoginView();
        } else {
            JOptionPane.showMessageDialog(registerView, "Registration Failed! Username may already exist.");
        }
    }

    private void openLoginView() {
        LoginView loginView = new LoginView();
        new LoginController(loginView, userController);
        loginView.setVisible(true);
        registerView.dispose();
    }

    // Kullanıcı adı format kontrolü
    private boolean isValidUsername(String username) {
        return Pattern.matches("^[a-zA-Z0-9_]+$", username);
    }

    // Parola hash fonksiyonu (örnek olarak basit bir şifreleme; üretim için güvenli bir yöntem kullanılmalı)
    private String hashPassword(String password) {
        // Bu örnek, hash yerine düz metin döndürüyor. BCrypt veya SHA-256 gibi bir hash kütüphanesi kullanmanız önerilir.
        return password; // Değiştirin: BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
