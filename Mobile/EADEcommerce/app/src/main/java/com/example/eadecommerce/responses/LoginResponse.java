package com.example.eadecommerce.responses;

/**
 * The LoginResponse class represents the data structure for a login response,
 * containing fields for the authentication token received after a successful login.
 */
public class LoginResponse {
    // Fields representing the details of a login response
    private String token;

    /**
     * Gets the authentication token from the login response.
     * @return The token as a String.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token for the login response.
     * @param token The token to be set.
     */
    public void setToken(String token) {
        this.token = token;
    }
}
