package com.example.eadecommerce.model;

import java.util.List;

public class CartResponse {
    private String cartId;
    private String userId;
    private List<CartProductResponse> products;

    // Getters and setters
    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartProductResponse> getProducts() { return products; }
    public void setProducts(List<CartProductResponse> products) { this.products = products; }
}
