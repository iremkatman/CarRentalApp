package service;

import database.SingletonConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationService {

    private final Connection connection = SingletonConnection.getInstance();

    public void createReservation(int carId, int userId, LocalDateTime startDate, LocalDateTime endDate) {
        String query = "INSERT INTO reservations (car_id, user_id, reservation_date_start, reservation_date_end) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            stmt.setInt(1, carId);
            stmt.setInt(2, userId);
            stmt.setString(3, startDate.format(formatter));
            stmt.setString(4, endDate.format(formatter));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}