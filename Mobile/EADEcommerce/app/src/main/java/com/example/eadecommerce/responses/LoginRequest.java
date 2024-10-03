package com.example.eadecommerce.responses;

/**
 * The LoginRequest class represents the data structure for a login request,
 * containing fields for the user's email and password.
 * It includes a constructor to initialize the fields.
 */
public class LoginRequest {
    // Fields representing the details of a login request
    private String email;
    private String password;

    /**
     * Constructor to initialize the login request fields.
     * @param email The email address for the login request.
     * @param password The password for the login request.
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter for email
    /**
     * Gets the email address for the login request.
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    // Getter for password
    /**
     * Gets the password for the login request.
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }
}
