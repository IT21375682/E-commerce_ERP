package com.example.eadecommerce.model;

/**
 * The UserUpdate class represents the data structure for updating a user's details,
 * containing fields for the user's name, phone number, and address.
 * It includes a constructor to initialize the fields and getter/setter methods for each field.
 */
public class UserUpdate {
    // Fields representing the details of a user update
    private String name;
    private String phone;
    private Address address;

    /**
     * Constructor to initialize the user update fields.
     * @param updatedName The updated name for the user.
     * @param updatedPhone The updated phone number for the user.
     * @param updatedAddress The updated address for the user.
     */
    public UserUpdate(String updatedName, String updatedPhone, Address updatedAddress) {
        this.name = updatedName;
        this.phone = updatedPhone;
        this.address = updatedAddress;
    }

    // Getter for name
    /**
     * Gets the updated name of the user.
     * @return The updated name of the user.
     */
    public String getName() {
        return name;
    }

    // Getter for phone
    /**
     * Gets the updated phone number of the user.
     * @return The updated phone number of the user.
     */
    public String getPhone() {
        return phone;
    }

    // Getter for address
    /**
     * Gets the updated address of the user.
     * @return The updated address of the user.
     */
    public Address getAddress() {
        return address;
    }

    // Setter for name
    /**
     * Sets the updated name of the user.
     * @param name The updated name to set for the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    // Setter for phone
    /**
     * Sets the updated phone number of the user.
     * @param phone The updated phone number to set for the user.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Setter for address
    /**
     * Sets the updated address of the user.
     * @param address The updated address to set for the user.
     */
    public void setAddress(Address address) {
        this.address = address;
    }
}
