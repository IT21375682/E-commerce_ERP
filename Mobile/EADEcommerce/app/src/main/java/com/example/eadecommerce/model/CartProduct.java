package com.example.eadecommerce.model;

public class CartProduct {
    private String productId;
    private int count;

    // Constructor
    public CartProduct(String productId, int count) {
        this.productId = productId;
        this.count = count;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
