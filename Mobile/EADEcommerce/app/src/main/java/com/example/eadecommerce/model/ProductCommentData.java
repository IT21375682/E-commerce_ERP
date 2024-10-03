package com.example.eadecommerce.model;

import java.util.Date;

/**
 * The ProductCommentData class represents a comment made by a user on a product or vendor.
 * It includes fields for the comment details, such as user ID, vendor ID, product ID,
 * rating, comment text, and the date the comment was made.
 */
public class ProductCommentData {
    // Fields representing the details of a product comment
    private String id;
    private String userId;
    private String username;
    private String vendorId;
    private String vendorName;
    private String productId;
    private String productName;
    private Date date;
    private int rating;
    private String commentText;

    /**
     * Gets the unique ID for the comment.
     * @return The comment ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID for the comment.
     * @param id The comment ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the ID of the user who posted the comment.
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user who posted the comment.
     * @param userId The user ID to set.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the username of the user who posted the comment.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user who posted the comment.
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the ID of the vendor being commented on.
     * @return The vendor ID.
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * Sets the ID of the vendor being commented on.
     * @param vendorId The vendor ID to set.
     */
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * Gets the name of the vendor.
     * @return The vendor name.
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * Sets the name of the vendor.
     * @param vendorName The vendor name to set.
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * Gets the ID of the product being commented on.
     * @return The product ID.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Sets the ID of the product being commented on.
     * @param productId The product ID to set.
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Gets the name of the product being commented on.
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product being commented on.
     * @param productName The product name to set.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the date of the comment.
     * @return The date of the comment.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the comment.
     * @param date The date to set for the comment.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the rating for the product or vendor.
     * @return The rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating for the product or vendor.
     * @param rating The rating to set.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the actual comment text.
     * @return The comment text.
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * Sets the actual comment text.
     * @param commentText The comment text to set.
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
