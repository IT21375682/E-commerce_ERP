package com.example.eadecommerce.model;

/**
 * The CartProductResponse class represents the details of a product in the cart,
 * including product ID, name, image, price, and quantity (count).
 */
public class CartProductResponse {
    // Fields representing the components of a cart product response
    private String productId;
    private String productName;
    private String productImage;
    private double price;
    private int count;

    // Getter and setter methods for each field

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
     * Gets the product name.
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the product name.
     * @param productName The new product name.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the product image URL.
     * @return The product image URL.
     */
    public String getProductImage() {
        return productImage;
    }

    /**
     * Sets the product image URL.
     * @param productImage The new product image URL.
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * Gets the price of the product.
     * @return The product price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     * @param price The new product price.
     */
    public void setPrice(double price) {
        this.price = price;
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
