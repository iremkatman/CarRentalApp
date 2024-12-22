package command;

import database.SingletonConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CancelRentalCommand implements Command {
    private int reservationId;
    private int carId;
    private int userId;
    private String startDate;
    private String endDate;
    private Connection connection;

    public CancelRentalCommand(int reservationId, int carId) {
        this.reservationId = reservationId;
        this.carId = carId;
        this.connection = SingletonConnection.getInstance();
    }

    @Override
    public void execute() {
        try {
            connection.setAutoCommit(false);

            // Rezervasyon bilgilerini kaydet
            String fetchQuery = "SELECT user_id, reservation_date_start, reservation_date_end FROM reservations WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(fetchQuery)) {
                stmt.setInt(1, reservationId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("user_id");
                        startDate = rs.getString("reservation_date_start");
                        endDate = rs.getString("reservation_date_end");
                    }
                }
            }

            // Rezervasyonu sil
            String deleteQuery = "DELETE FROM reservations WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
                stmt.setInt(1, reservationId);
                stmt.executeUpdate();
            }

            // Arabayı tekrar kullanılabilir yap
            String updateCarQuery = "UPDATE cars SET availability = TRUE WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateCarQuery)) {
                stmt.setInt(1, carId);
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        try {
            connection.setAutoCommit(false);

            // Rezervasyonu geri ekle
            String insertQuery = "INSERT INTO reservations (id, car_id, user_id, reservation_date_start, reservation_date_end) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                stmt.setInt(1, reservationId);
                stmt.setInt(2, carId);
                stmt.setInt(3, userId);
                stmt.setString(4, startDate);
                stmt.setString(5, endDate);
                stmt.executeUpdate();
            }

            // Arabayı tekrar rezerve edilemez yap
            String updateCarQuery = "UPDATE cars SET availability = FALSE WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateCarQuery)) {
                stmt.setInt(1, carId);
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
