package com.example.eadecommerce.model;

/**
 * The CartProduct class represents a product in the shopping cart.
 * It contains the productId and the quantity (count) of the product.
 */
public class CartProduct {
    // Fields representing the components of a cart product
    private String productId;
    private int count;

    /**
     * Constructor to initialize the CartProduct fields.
     * @param productId The ID of the product.
     * @param count The quantity of the product in the cart.
     */
    public CartProduct(String productId, int count) {
        this.productId = productId;
        this.count = count;
    }

    // Getter and setter methods for productId and count

    /**
     * Gets the product ID.
     * @return The product ID.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Sets the product ID.
     * @param productId The new product ID.
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Gets the quantity of the product in the cart.
     * @return The quantity of the product.
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the quantity of the product in the cart.
     * @param count The new quantity of the product.
     */
    public void setCount(int count) {
        this.count = count;
    }
}
