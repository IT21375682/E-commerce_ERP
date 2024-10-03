package com.example.eadecommerce.model;

/**
 * The CartItem class represents an individual item in the shopping cart.
 * It includes the item's name, image URL, price, and quantity (count).
 */
public class CartItem {
    // Fields representing the components of a cart item
    private String name;
    private String imageUrl;
    private double price;
    private int count;

    /**
     * Constructor to initialize the CartItem fields.
     * @param name The name of the product.
     * @param imageUrl The URL of the product image.
     * @param price The price of the product.
     * @param count The quantity of the product in the cart.
     */
    public CartItem(String name, String imageUrl, double price, int count) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.count = count;
    }

    // Getter methods for each field

    /**
     * Gets the name of the product.
     * @return The product name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the URL of the product image.
     * @return The image URL of the product.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Gets the price of the product.
     * @return The product price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the quantity of the product in the cart.
     * @return The count of the product.
     */
    public int getCount() {
        return count;
    }

    // Setter method for count

    /**
     * Sets the quantity of the product in the cart.
     * @param count The new quantity of the product.
     */
    public void setCount(int count) {
        this.count = count;
    }
}
