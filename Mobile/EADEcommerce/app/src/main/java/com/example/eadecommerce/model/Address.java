package com.example.eadecommerce.model;

/**
 * The Address class represents a user's address with fields for street, city, postal code, and country.
 * It includes a constructor to initialize the address and getter/setter methods for each field.
 */
public class Address {
    // Fields representing the components of an address
    private String street;
    private String city;
    private String postalCode;
    private String country;

    /**
     * Constructor to initialize the address fields.
     * @param updatedStreet The updated street address.
     * @param updatedCity The updated city.
     * @param updatedPostalCode The updated postal code.
     * @param updatedCountry The updated country.
     */
    public Address(String updatedStreet, String updatedCity, String updatedPostalCode, String updatedCountry) {
        this.street = updatedStreet;
        this.city = updatedCity;
        this.postalCode = updatedPostalCode;
        this.country = updatedCountry;
    }

    // Getter and setter methods for each field

    /**
     * Gets the street address.
     * @return The current street address.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street address.
     * @param street The new street address.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Gets the city name.
     * @return The current city name.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city name.
     * @param city The new city name.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the postal code.
     * @return The current postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code.
     * @param postalCode The new postal code.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the country name.
     * @return The current country name.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country name.
     * @param country The new country name.
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
