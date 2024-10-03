package com.example.eadecommerce.model;

/**
 * The UserResponse class represents the response structure for user data,
 * containing fields for the user's ID, name, email, phone, and address.
 * It includes getter and setter methods for each field.
 */
public class UserResponse {
    // Fields representing the details of a user response
    private String id;
    private String name;
    private String email;
    private String phone;
    private Address address;

    // Getter for id
    /**
     * Gets the unique identifier for the user.
     * @return The user's ID.
     */
    public String getId() {
        return id;
    }

    // Setter for id
    /**
     * Sets the unique identifier for the user.
     * @param id The ID to set for the user.
     */
    public void setId(String id) {
        this.id = id;
    }

    // Getter for name
    /**
     * Gets the name of the user.
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    // Setter for name
    /**
     * Sets the name of the user.
     * @param name The name to set for the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    // Getter for email
    /**
     * Gets the email address of the user.
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    // Setter for email
    /**
     * Sets the email address of the user.
     * @param email The email address to set for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for phone
    /**
     * Gets the phone number of the user.
     * @return The phone number of the user.
     */
    public String getPhone() {
        return phone;
    }

    // Setter for phone
    /**
     * Sets the phone number of the user.
     * @param phone The phone number to set for the user.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter for address
    /**
     * Gets the address of the user.
     * @return The address of the user.
     */
    public Address getAddress() {
        return address;
    }

    // Setter for address
    /**
     * Sets the address of the user.
     * @param address The address to set for the user.
     */
    public void setAddress(Address address) {
        this.address = address;
    }
}
