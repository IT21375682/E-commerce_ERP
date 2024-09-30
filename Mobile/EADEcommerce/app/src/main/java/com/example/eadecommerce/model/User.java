package com.example.eadecommerce.model;

public class User {
    private String name;
    private String email;
    private String password;
    private String role; // You may want to define this based on your application logic
    private boolean isActive;

    // Constructor
    public User(String name, String email, String password, String role, boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }
}

