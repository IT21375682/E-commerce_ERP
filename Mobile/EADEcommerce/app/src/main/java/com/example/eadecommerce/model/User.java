package com.example.eadecommerce.model;

/**
 * The User class represents a user in the e-commerce system with fields for the user's name, email,
 * password, role, and active status. It includes a constructor to initialize the user details and
 * getter and setter methods for each field.
 */
public class User {
    // Fields representing the details of a user
    private String name;
    private String email;
    private String password;
    private String role;
    private boolean isActive;

    /**
     * Constructor to initialize the user fields.
     * @param name The name of the user.
     * @param email The email address of the user.
     * @param password The password for the user's account.
     * @param role The role of the user (e.g., admin, customer).
     * @param isActive The active status of the user.
     */
    public User(String name, String email, String password, String role, boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }

    // Getter and setter methods

    /**
     * Gets the name of the user.
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     * @param name The name to set for the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * @param email The email address to set for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     * @param password The password to set for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user.
     * @return The role of the user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     * @param role The role to set for the user.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Checks if the user account is active.
     * @return True if the user account is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the user account.
     * @param isActive The active status to set for the user account.
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
