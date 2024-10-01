package com.example.eadecommerce.model;

public class CartProductResponse {
    private String productId;
    private String productName;
    private String productImage;
    private double price;
    private int count;

    // Getters and setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImage() { return productImage; }
    public void setProductImage(String productName) { this.productImage = productImage; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
