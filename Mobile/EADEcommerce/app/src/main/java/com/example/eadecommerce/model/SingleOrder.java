package com.example.eadecommerce.model;

import java.util.List;

/**
 * The SingleOrder class represents an order in the e-commerce system.
 * It includes fields for order details such as the order ID, user ID, date,
 * total amount, product items, order status, and any cancellation note.
 */
public class SingleOrder {
    // Fields representing the details of an order
    private String id;
    private String userId;
    private String date;
    private double total;
    private List<SingleOrderProductItem> productItems;
    private SingleOrderStatus status;
    private String cancellationNote;

    // Getter and setter methods
    /**
     * Gets the unique identifier for the order.
     * @return The order ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the order.
     * @param id The order ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the ID of the user who placed the order.
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user who placed the order.
     * @param userId The user ID to set.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the date the order was placed.
     * @return The order date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date the order was placed.
     * @param date The order date to set.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the total amount for the order.
     * @return The total amount.
     */
    public double getTotal() {
        return total;
    }

    /**
     * Sets the total amount for the order.
     * @param total The total amount to set.
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Gets the list of product items in the order.
     * @return The list of product items.
     */
    public List<SingleOrderProductItem> getProductItems() {
        return productItems;
    }

    /**
     * Sets the list of product items in the order.
     * @param productItems The list of product items to set.
     */
    public void setProductItems(List<SingleOrderProductItem> productItems) {
        this.productItems = productItems;
    }

    /**
     * Gets the status of the order.
     * @return The order status.
     */
    public SingleOrderStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the order.
     * @param status The order status to set.
     */
    public void setStatus(SingleOrderStatus status) {
        this.status = status;
    }

    /**
     * Gets the cancellation note for the order.
     * @return The cancellation note.
     */
    public String getCancellationNote() {
        return cancellationNote;
    }

    /**
     * Sets the cancellation note for the order.
     * @param cancellationNote The cancellation note to set.
     */
    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }
}