package com.example.eadecommerce.model;

import java.util.List;

/**
 * The CartResponse class represents the response for a shopping cart,
 * including the cart ID, user ID, and a list of products in the cart.
 */
public class CartResponse {
    // Fields representing the components of a cart response
    private String cartId;
    private String userId;
    private List<CartProductResponse> products;

    // Getter and setter methods for each field

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
     * Gets the user ID associated with the cart.
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the cart.
     * @param userId The new user ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the list of products in the cart.
     * @return The list of CartProductResponse objects representing the products.
     */
    public List<CartProductResponse> getProducts() {
        return products;
    }

    /**
     * Sets the list of products in the cart.
     * @param products The new list of products.
     */
    public void setProducts(List<CartProductResponse> products) {
        this.products = products;
    }
}
