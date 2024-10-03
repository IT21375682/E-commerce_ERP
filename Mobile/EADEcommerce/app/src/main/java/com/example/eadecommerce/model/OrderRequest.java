package com.example.eadecommerce.model;

import java.util.List;

/**
 * The OrderRequest class represents a request to create an order.
 * It contains details such as the user ID, total amount, and the list of product items.
 */
public class OrderRequest {
    // Fields representing the components of an order request
    private String userId;
    private double total;
    private List<CartProduct> productItems;

    // Getter for userId
    /**
     * Gets the user ID of the person placing the order.
     * @return The unique identifier for the user.
     */
    public String getUserId() {
        return userId;
    }

    // Setter for userId
    /**
     * Sets the user ID of the person placing the order.
     * @param userId The unique identifier for the user.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter for total
    /**
     * Gets the total amount of the order.
     * @return The total cost of the order.
     */
    public double getTotal() {
        return total;
    }

    // Setter for total
    /**
     * Sets the total amount of the order.
     * @param total The total cost of the order.
     */
    public void setTotal(double total) {
        this.total = total;
    }

    // Getter for productItems
    /**
     * Gets the list of product items in the order.
     * @return The list of CartProduct items.
     */
    public List<CartProduct> getProductItems() {
        return productItems;
    }

    // Setter for productItems
    /**
     * Sets the list of product items in the order.
     * @param productItems The list of CartProduct items.
     */
    public void setProductItems(List<CartProduct> productItems) {
        this.productItems = productItems;
    }
}
