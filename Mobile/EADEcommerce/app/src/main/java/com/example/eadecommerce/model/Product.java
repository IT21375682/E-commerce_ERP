package com.example.eadecommerce.model;

public class Product {
    private String name;
    private double price;
    private String imageUrl;
    private String category;

    public Product(String name, double price, String imageUrl, String category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

}
