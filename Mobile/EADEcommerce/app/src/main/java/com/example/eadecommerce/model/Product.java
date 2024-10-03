package com.example.eadecommerce.model;

/**
 * The Product class represents a product in the e-commerce system with fields for product ID, name,
 * image, category, description, price, available stock, active status, vendor ID, creation date,
 * stock update date, product category name, vendor name, and average rating.
 * It includes a constructor to initialize the product and getter/setter methods for each field.
 */
public class Product {
    // Fields representing the details of a product
    private String productId;
    private String name;
    private String productImage;
    private String categoryId;
    private String description;
    private double price;
    private int availableStock;
    private boolean isActive;
    private String vendorId;
    private String createdAt;
    private String stockLastUpdated;
    private String productCategoryName;
    private String vendorName;
    private double averageRating ;

    /**
     * Constructor to initialize the product fields.
     * @param productId The unique identifier for the product.
     * @param name The name of the product.
     * @param productImage The URL of the product image.
     * @param categoryId The ID of the product category.
     * @param description The description of the product.
     * @param price The price of the product.
     * @param availableStock The number of items available in stock.
     * @param isActive The status indicating if the product is active.
     * @param vendorId The ID of the vendor.
     * @param createdAt The creation date of the product.
     * @param stockLastUpdated The last update date of the stock.
     * @param productCategoryName The name of the product category.
     * @param vendorName The name of the vendor.
     * @param averageRating The average rating of the product.
     */
    public Product(String productId, String name, String productImage, String categoryId,
                   String description, double price, int availableStock, boolean isActive,
                   String vendorId, String createdAt, String stockLastUpdated,
                   String productCategoryName, String vendorName, double averageRating) {
        this.productId = productId;
        this.name = name;
        this.productImage = productImage;
        this.categoryId = categoryId;
        this.description = description;
        this.price = price;
        this.availableStock = availableStock;
        this.isActive = isActive;
        this.vendorId = vendorId;
        this.createdAt = createdAt;
        this.stockLastUpdated = stockLastUpdated;
        this.productCategoryName = productCategoryName;
        this.vendorName = vendorName;
        this.averageRating = averageRating;
    }

    // Getter methods for each field
    public String getId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public String getVendor() {
        return vendorId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getStockLastUpdated() {
        return stockLastUpdated;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public double getRating() {
        return averageRating;
    }

    // Setter methods for each field
    public void setId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setStockLastUpdated(String stockLastUpdated) {
        this.stockLastUpdated = stockLastUpdated;
    }
}
