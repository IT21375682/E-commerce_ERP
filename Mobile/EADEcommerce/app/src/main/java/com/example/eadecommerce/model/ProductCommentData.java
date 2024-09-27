package com.example.eadecommerce.model;

public class ProductCommentData {
    private String comment;
    private String commentGmail;
    private String commentId;

    // Default constructor
    public ProductCommentData() {
        this.comment = "";
        this.commentGmail = "";
        this.commentId = "";
    }

    // Parameterized constructor
    public ProductCommentData(String comment, String commentGmail, String commentId) {
        this.comment = comment;
        this.commentGmail = commentGmail;
        this.commentId = commentId;
    }

    // Getter and Setter for comment
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getter and Setter for commentGmail
    public String getCommentGmail() {
        return commentGmail;
    }

    public void setCommentGmail(String commentGmail) {
        this.commentGmail = commentGmail;
    }

    // Getter and Setter for commentId
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
