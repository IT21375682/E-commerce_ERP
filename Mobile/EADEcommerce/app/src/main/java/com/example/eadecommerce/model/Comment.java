package com.example.eadecommerce.model;

/**
 * The Comment class represents a user's comment on a product.
 * It includes the comment ID, user ID, vendor ID, product ID, rating, and the comment text.
 */
public class Comment {
    // Fields representing the components of a comment
    private String id;
    private String userId;
    private String vendorId;
    private String productId;
    private int rating;
    private String commentText;

    // Constructor to initialize all fields
    public Comment(String id, String userId, String vendorId, String productId, int rating, String commentText) {
        this.id = id;
        this.userId = userId;
        this.vendorId = vendorId;
        this.productId = productId;
        this.rating = rating;
        this.commentText = commentText;
    }

    // Default constructor
    public Comment() {
    }

    // Getter methods for each field

    /**
     * Gets the comment ID.
     * @return The unique identifier for the comment.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the user ID of the commenter.
     * @return The unique identifier for the user.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the vendor ID associated with the product.
     * @return The unique identifier for the vendor.
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * Gets the product ID being commented on.
     * @return The unique identifier for the product.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Gets the rating given by the user.
     * @return The rating value.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Gets the text of the comment.
     * @return The content of the comment.
     */
    public String getCommentText() {
        return commentText;
    }

    // Setter methods for each field

    /**
     * Sets the comment ID.
     * @param id The new comment ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the user ID of the commenter.
     * @param userId The new user ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Sets the vendor ID associated with the product.
     * @param vendorId The new vendor ID.
     */
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * Sets the product ID being commented on.
     * @param productId The new product ID.
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Sets the rating given by the user.
     * @param rating The new rating value.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Sets the text of the comment.
     * @param commentText The new content of the comment.
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
