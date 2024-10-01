package com.example.eadecommerce.model;

import java.util.List;

public class SingleOrder {
    private String id;
    private String userId;
    private String date;
    private double total;
    private List<SingleOrderProductItem> productItems;
    private SingleOrderStatus status;
    private String cancellationNote;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<SingleOrderProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<SingleOrderProductItem> productItems) {
        this.productItems = productItems;
    }

    public SingleOrderStatus getStatus() {
        return status;
    }

    public void setStatus(SingleOrderStatus status) {
        this.status = status;
    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }
}
