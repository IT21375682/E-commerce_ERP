package com.example.eadecommerce.model;

public class CartItem {
    private String name;
    private String imageUrl;
    private double price;
    private int count;

    public CartItem(String name, String imageUrl, double price, int count) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    // Setter method for count
    public void setCount(int count) {
        this.count = count;
    }
}
