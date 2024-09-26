package com.example.eadecommerce.model;

public class Product {
    private String name;
    private double price;
    private String imageUrl;
    private String category;
    private double rating;
    private String vendor;// Add a rating field

    public Product(String name, double price, String imageUrl, String category, double rating, String vendor) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.rating = rating;
        this.vendor = vendor;
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

    public double getRating() {
        return rating;
    }

    public String getVendor() {
        return vendor;
    }
}
