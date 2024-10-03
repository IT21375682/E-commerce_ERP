package com.example.eadecommerce.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Cart class represents a shopping cart for a user. It contains the cartId, userId, and a list of CartProduct items.
 */
public class Cart {
    // Fields representing the components of a cart
    private String cartId;
    private String userId;
    private List<CartProduct> products;

    /**
     * Default constructor that initializes the products list.
     */
    public Cart() {
        this.products = new ArrayList<>();
    }

    /**
     * Constructor that accepts a userId and initializes the products list.
     * @param userId The ID of the user who owns the cart.
     */
    public Cart(String userId) {
        this.userId = userId;
        this.products = new ArrayList<>();
    }

    /**
     * Constructor that accepts a userId and a list of products.
     * If the products list is null, it initializes an empty list.
     * @param userId The ID of the user who owns the cart.
     * @param products The list of products to be added to the cart.
     */
    public Cart(String userId, List<CartProduct> products) {
        this.userId = userId;
        this.products = products != null ? products : new ArrayList<>();
    }

    // Getter and setter methods for cartId, userId, and products

    /**
     * Gets the cart ID.
     * @return The cart ID.
     */
    public String getCartId() {
        return cartId;
    }

    /**
     * Sets the cart ID.
     * @param cartId The new cart ID.
     */
    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    /**
     * Gets the user ID.
     * @return The user ID of the cart owner.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     * @param userId The new user ID of the cart owner.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the list of products in the cart.
     * @return The list of CartProduct items.
     */
    public List<CartProduct> getProducts() {
        return products;
    }

    /**
     * Sets the list of products in the cart.
     * @param products The new list of CartProduct items.
     */
    public void setProducts(List<CartProduct> products) {
        this.products = products;
    }

    // Methods for adding and removing products from the cart

    /**
     * Adds a product to the cart.
     * @param product The CartProduct to add.
     */
    public void addProduct(CartProduct product) {
        this.products.add(product);
    }

    /**
     * Removes a product from the cart by its productId.
     * @param productId The ID of the product to be removed.
     */
    public void removeProduct(String productId) {
        products.removeIf(product -> product.getProductId().equals(productId));
    }
}
