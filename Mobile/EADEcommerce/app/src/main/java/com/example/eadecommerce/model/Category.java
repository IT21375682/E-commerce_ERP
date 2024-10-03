package com.example.eadecommerce.model;

/**
 * The Category class represents a product category in the e-commerce system.
 * It contains the category ID and the category name.
 */
public class Category {
    // Fields representing the components of a category
    private String id;
    private String categoryName;

    /**
     * Constructor to initialize the Category fields.
     * @param id The unique identifier for the category.
     * @param name The name of the category.
     */
    public Category(String id, String name) {
        this.id = id;
        this.categoryName = name;
    }

    // Getter methods for category fields

    /**
     * Gets the category name.
     * @return The name of the category.
     */
    public String getName() {
        return categoryName;
    }

    /**
     * Gets the category ID.
     * @return The unique identifier for the category.
     */
    public String getId() {
        return id;
    }
}
