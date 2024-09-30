package com.example.eadecommerce.model;

import java.util.Date;

public class ProductCommentData {
    private String id;              // Unique ID for the comment
    private String userId;          // ID of the user who posted the comment
    private String username;         // Username of the user who posted the comment
    private String vendorId;         // ID of the vendor being commented on
    private String vendorName;       // Name of the vendor
    private String productId;        // ID of the product being commented on
    private String productName;      // Name of the product
    private Date date;               // Date of the comment
    private int rating;              // Rating for the product/vendor
    private String commentText;      // Actual comment text

    // Getter and setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for vendorId
    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    // Getter and setter for vendorName
    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    // Getter and setter for productId
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    // Getter and setter for productName
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getter and setter for date
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Getter and setter for rating
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // Getter and setter for commentText
    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}

