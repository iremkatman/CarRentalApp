package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    String url = "jdbc:mysql://localhost:3306/car_rental_system?useSSL=false";
    private final String user = "root";
    private final String pass = "password123A";
    private static Connection connection;

    private SingletonConnection() {
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance() {
        if(connection == null)
            new SingletonConnection();
        return connection;
    }


}