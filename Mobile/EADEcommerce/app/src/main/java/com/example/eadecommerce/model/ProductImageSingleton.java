package com.example.eadecommerce.model;

public class ProductImageSingleton {
    private static ProductImageSingleton instance;
    private String productImage;

    // Private constructor to prevent instantiation
    private ProductImageSingleton() {}

    // Get the singleton instance
    public static ProductImageSingleton getInstance() {
        if (instance == null) {
            instance = new ProductImageSingleton();
        }
        return instance;
    }

    // Set the product image
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    // Get the product image
    public String getProductImage() {
        return productImage;
    }
}
