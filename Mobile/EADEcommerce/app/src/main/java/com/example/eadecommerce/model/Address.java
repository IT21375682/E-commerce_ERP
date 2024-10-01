package com.example.eadecommerce.model;

public class Address {
    private String street;
    private String city;
    private String postalCode;
    private String country;

    public Address(String updatedStreet, String updatedCity, String updatedPostalCode, String updatedCountry) {
        this.street = updatedStreet;
        this.city = updatedCity;
        this.postalCode = updatedPostalCode;
        this.country = updatedCountry;
    }

    // Getter for street
    public String getStreet() {
        return street;
    }

    // Setter for street
    public void setStreet(String street) {
        this.street = street;
    }

    // Getter for city
    public String getCity() {
        return city;
    }

    // Setter for city
    public void setCity(String city) {
        this.city = city;
    }

    // Getter for postalCode
    public String getPostalCode() {
        return postalCode;
    }

    // Setter for postalCode
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    // Getter for country
    public String getCountry() {
        return country;
    }

    // Setter for country
    public void setCountry(String country) {
        this.country = country;
    }
}
