package com.example.eadecommerce.model;

public class Comment {
    private String id;
    private String userId;
    private String vendorId;
    private String productId;
    private int rating;
    private String commentText;

    // Constructor
    public Comment(String id, String userId, String vendorId, String productId, int rating, String commentText) {
        this.id = id;
        this.userId = userId;
        this.vendorId = vendorId;
        this.productId = productId;
        this.rating = rating;
        this.commentText = commentText;
    }

    public Comment() {

    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getProductId() {
        return productId;
    }

    public int getRating() {
        return rating;
    }

    public String getCommentText() {
        return commentText;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}

