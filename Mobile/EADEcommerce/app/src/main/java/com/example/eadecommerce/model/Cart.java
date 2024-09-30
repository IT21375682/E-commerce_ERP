package com.example.eadecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String cartId;
    private String userId;
    private List<CartProduct> products;

    // Constructor
    public Cart() {
        this.products = new ArrayList<>();
    }

    // Constructor that accepts userId and initializes products
    public Cart(String userId) {
        this.userId = userId;
        this.products = new ArrayList<>();
    }

    // Constructor that accepts userId and products
    public Cart(String userId, List<CartProduct> products) {
        this.userId = userId;
        this.products = products != null ? products : new ArrayList<>();
    }

    // Getters and Setters
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CartProduct> products) {
        this.products = products;
    }

    // Method to add a product to the cart
    public void addProduct(CartProduct product) {
        this.products.add(product);
    }

    // Method to remove a product from the cart by productId
    public void removeProduct(String productId) {
        products.removeIf(product -> product.getProductId().equals(productId));
    }
}
