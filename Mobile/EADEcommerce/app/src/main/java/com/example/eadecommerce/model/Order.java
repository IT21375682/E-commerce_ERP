package com.example.eadecommerce.model;

// Order.java
public class Order {
    private String orderId;
    private String date;
    private double total;

    public Order(String orderId, String date, double total) {
        this.orderId = orderId;
        this.date = date;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }
}
