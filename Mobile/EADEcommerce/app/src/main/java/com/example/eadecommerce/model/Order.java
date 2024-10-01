package com.example.eadecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order implements Parcelable {
    private String id;
    private String userId;
    private String date;
    private double total;

    // Product items in the order
    private List<OrderProductItem> productItems;

    // Order status
    private OrderStatus status;

    // Cancellation note
    private String cancellationNote;

    // Constructor
    public Order(String orderId, String userId, String date, double total, List<OrderProductItem> productItems, OrderStatus status, String cancellationNote) {
        this.id = orderId;
        this.userId = userId;
        this.date = date;
        this.total = total;
        this.productItems = productItems;
        this.status = status;
        this.cancellationNote = cancellationNote;
    }

    // Getter methods
    public String getOrderId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public List<OrderProductItem> getProductItems() {
        return productItems;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    // Parcelable implementation
    protected Order(Parcel in) {
        id = in.readString();
        userId = in.readString();
        date = in.readString();
        total = in.readDouble();
        productItems = in.createTypedArrayList(OrderProductItem.CREATOR);
        status = in.readParcelable(OrderStatus.class.getClassLoader());
        cancellationNote = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(date);
        dest.writeDouble(total);
        dest.writeTypedList(productItems);
        dest.writeParcelable(status, flags);
        dest.writeString(cancellationNote);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", date='" + date + '\'' +
                ", total=" + total +
                ", productItems=" + (productItems != null ? productItems.toString() : "[]") +
                ", status=" + (status != null ? status.toString() : "N/A") +
                ", cancellationNote='" + cancellationNote + '\'' +
                '}';
    }
}
