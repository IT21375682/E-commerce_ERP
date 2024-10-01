package com.example.eadecommerce.model;

public class UserUpdate {

    private String name;
    private String phone;
    private Address address;

    public UserUpdate(String updatedName, String updatedPhone, Address updatedAddress) {
        this.name = updatedName;
        this.phone = updatedPhone;
        this.address = updatedAddress;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
