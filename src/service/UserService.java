package service;
import database.SingletonConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class UserService {

    private final Connection connection = SingletonConnection.getInstance();

    public void updateUserBudget(int userId, double newBudget) {
        String query = "UPDATE users SET budget = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, newBudget);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}