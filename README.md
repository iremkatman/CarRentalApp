Car Rental System

This project is a Car Rental System designed to simplify the car rental process for users. It provides a user-friendly interface for renting cars, managing rentals, and viewing available vehicles. The project utilizes several design patterns and follows the MVC (Model-View-Controller) architectural pattern to ensure scalability, maintainability, and modularity.

Features

User registration and login functionality

Car rental with pricing model selection

Real-time budget management

Filtering and viewing available cars

Undo/redo functionality for rental cancellations

Admin panel for car management (add, modify, remove)

Observer pattern for real-time updates on budget and UI elements

Project Structure

The project is structured around the MVC architecture:

Model

The model layer contains the core business logic and data structures:

User: Represents user data and manages observers for budget updates.

Car: An interface representing different car types (e.g., SUV, Sedan, Hatchback).

PricingStrategy: Defines strategies for calculating rental prices (e.g., hourly, daily, weekly).

Decorators: Enhances cars with additional features like full fuel or insurance.

View

The view layer manages the graphical user interface using Swing components:

LoginView: User login interface.

RegisterView: User registration form.

WelcomeView: Main menu for users.

RentCarView: Car rental interface.

RentedCarsView: Displays user's rented cars and allows actions like cancellation or return.

AdminView: Admin interface for managing cars.

Controller

The controller layer connects the view and model layers, handling user actions and updating the view:

LoginController: Handles login and registration logic.

RentCarController: Manages the car rental process.

RentedCarsController: Handles actions on rented cars (cancel, return, undo).

AdminController: Enables admins to add, modify, or remove cars.

Design Patterns

This project implements multiple design patterns to improve its structure and maintainability:

1. Singleton Pattern

Used in: SingletonConnection

Ensures a single instance of the database connection is shared across the application.

2. Command Pattern

Used in: CancelRentalCommand, CommandInvoker

Encapsulates rental operations (cancel, undo) as commands, enabling undo/redo functionality.

3. Factory Pattern

Used in: CarFactory

Simplifies the creation of car objects based on their type (e.g., SUV, Luxury, Sedan).

4. Strategy Pattern

Used in: PricingStrategy

Defines pricing models (e.g., hourly, daily, weekly) as interchangeable strategies.

5. Decorator Pattern

Used in: CarDecorator, FullFuelDecorator, InsuranceDecorator

Dynamically adds features to cars, such as full fuel or temporary insurance.

6. Observer Pattern

Used in: User (as Subject), Views (as Observers)

Updates views in real time when the user’s budget changes.

7. Facade Pattern

Used in: CarRentalFacade

Simplifies interactions between controllers and services by providing a unified interface.

Installation and Usage

Clone the repository.

Set up the database.

Import the provided SQL schema to your MySQL database.

Update database credentials in SingletonConnection.

Build and run the project:

Open the project in your preferred IDE.

Run the Main class to start the application.

Login as:

Admin: Username: admin, Password: admin123

User: Register a new user through the registration interface.
