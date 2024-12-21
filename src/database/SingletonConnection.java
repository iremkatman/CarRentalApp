package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    //Database connection URL
    String url = "jdbc:mysql://localhost:3306/car_rental_system?useSSL=false";
    //Username
    private final String user = "root";
    //Passowrd, in my case i don t have a password
    private final String pass = "password123A";
    //Connection object
    private static Connection connection;

    //Private constructor
    private SingletonConnection() {
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //This method returns the connection instance if already created, or create an instance
    public static Connection getInstance() {
        if(connection == null)
            new SingletonConnection();
        return connection;
    }


}