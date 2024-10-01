package com.example.eadecommerce.model;

import java.util.List;

public class OrderRequest {
    private String userId;
    private double total;
    private List<CartProduct> productItems;

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<CartProduct> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<CartProduct> productItems) {
        this.productItems = productItems;
    }
}
