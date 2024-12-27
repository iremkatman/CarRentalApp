package model;

import java.util.ArrayList;
import java.util.List;

public class User implements Subject{
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private double budget;

    private List<Observer> observers = new ArrayList<>();

    public User(int id, String username, String password, String firstName, String lastName, double budget) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.budget = budget;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(budget);
        }
    }
    public void setBudget(double budget) {
        this.budget = budget;
        notifyObservers();
    }

    public double getBudget() {
        return budget;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
