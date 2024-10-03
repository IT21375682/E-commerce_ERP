package com.example.eadecommerce.model;

/**
 * The SingleOrderProductItem class represents a product item within a single order in the e-commerce system.
 * It includes fields for the product ID, name, image, price, quantity, and delivery status.
 * It provides getter and setter methods for each field.
 */
public class SingleOrderProductItem {
    // Fields representing the details of an order product item
    private String productId;
    private String productName;
    private String productImg;
    private double productPrice;
    private int count;
    private boolean delivered;

    // Getter and setter methods

    /**
     * Gets the unique identifier for the product.
     * @return The product ID.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Sets the unique identifier for the product.
     * @param productId The product ID to set.
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Gets the name of the product.
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product.
     * @param productName The product name to set.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the image URL of the product.
     * @return The product image URL.
     */
    public String getProductImg() {
        return productImg;
    }

    /**
     * Sets the image URL of the product.
     * @param productImg The product image URL to set.
     */
    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    /**
     * Gets the price of the product.
     * @return The product price.
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * Sets the price of the product.
     * @param productPrice The product price to set.
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Gets the quantity of the product ordered.
     * @return The quantity ordered.
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the quantity of the product ordered.
     * @param count The quantity to set.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Checks if the product has been delivered.
     * @return True if delivered, false otherwise.
     */
    public boolean isDelivered() {
        return delivered;
    }

    /**
     * Sets the delivery status of the product.
     * @param delivered The delivery status to set.
     */
    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}
