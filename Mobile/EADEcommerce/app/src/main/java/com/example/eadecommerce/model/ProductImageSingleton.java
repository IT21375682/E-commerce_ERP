package com.example.eadecommerce.model;

/**
 * The ProductImageSingleton class implements the Singleton pattern
 * to provide a global point of access to a single instance of the product image.
 * This class allows for setting and getting the product image.
 */
public class ProductImageSingleton {
    // Fields representing the details of an image
    private static ProductImageSingleton instance;
    private String productImage;

    // Private constructor to prevent instantiation
    private ProductImageSingleton() {}

    /**
     * Gets the singleton instance of ProductImageSingleton.
     * If the instance does not exist, it creates one.
     * @return The singleton instance.
     */
    public static ProductImageSingleton getInstance() {
        if (instance == null) {
            instance = new ProductImageSingleton();
        }
        return instance;
    }

    /**
     * Sets the product image.
     * @param productImage The product image to set.
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * Gets the product image.
     * @return The current product image.
     */
    public String getProductImage() {
        return productImage;
    }
}
