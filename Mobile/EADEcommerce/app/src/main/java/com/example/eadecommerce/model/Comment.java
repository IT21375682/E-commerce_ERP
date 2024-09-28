package com.example.eadecommerce.model;

public class Comment {
    private String username;
    private String commentText;
    private String date;

    public Comment(String username, String commentText, String date) {
        this.username = username;
        this.commentText = commentText;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getDate() {
        return date;
    }
}

