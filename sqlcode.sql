

DROP DATABASE IF EXISTS car_rental_system;
CREATE DATABASE car_rental_system;

USE car_rental_system;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,               
    username VARCHAR(255) UNIQUE NOT NULL,           
    password VARCHAR(255) NOT NULL,                 
    first_name VARCHAR(255) NOT NULL,                
    last_name VARCHAR(255) NOT NULL,                 
    budget DOUBLE DEFAULT 0 CHECK (budget >= 0),     
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP    
);

CREATE TABLE cars (
    id INT AUTO_INCREMENT PRIMARY KEY,                    
    model VARCHAR(255) NOT NULL,                         
    type ENUM('SUV', 'Hatchback', 'Sedan', 'Sports', 'Luxury') NOT NULL,
    availability BOOLEAN DEFAULT TRUE,                
    price_per_day DOUBLE DEFAULT 0 CHECK (price_per_day >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP         
);

INSERT INTO cars (model, type, availability, price_per_day) VALUES
('Toyota RAV4', 'SUV', TRUE, 100.0),
('Honda Civic', 'Sedan', TRUE, 80.0),
('Ford Mustang', 'Sports', TRUE, 150.0),
('Volkswagen Golf', 'Hatchback', TRUE, 70.0),
('Mercedes-Benz S-Class', 'Luxury', TRUE, 200.0),
('BMW X5', 'SUV', TRUE, 120.0),
('Hyundai Tucson', 'SUV', TRUE, 90.0),
('Audi A4', 'Sedan', TRUE, 110.0),
('Porsche 911', 'Sports', TRUE, 250.0),
('Tesla Model S', 'Luxury', TRUE, 300.0),
('Chevrolet Spark', 'Hatchback', TRUE, 60.0),
('Mazda 3', 'Sedan', TRUE, 75.0),
('Lamborghini Huracan', 'Sports', TRUE, 400.0),
('Jaguar F-Pace', 'SUV', TRUE, 140.0),
('Rolls-Royce Phantom', 'Luxury', TRUE, 500.0);

CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,              
    car_id INT NOT NULL,                            
    user_id INT NOT NULL,
    reservation_date_start TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reservation_date_end TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE, 
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE 
);



SELECT * FROM cars;

