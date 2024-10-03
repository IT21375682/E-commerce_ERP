package com.example.eadecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The Order class represents an order placed by a user.
 * It contains details about the order, including the user, total amount, product items,
 * order status, and cancellation note. It implements Parcelable for easy passing between activities.
 */
public class Order implements Parcelable {
    // Fields representing the components of an order
    private String id;
    private String userId;
    private String date;
    private double total;
    private List<OrderProductItem> productItems;
    private OrderStatus status;
    private String cancellationNote;

    // Constructor to initialize all fields
    public Order(String orderId, String userId, String date, double total,
                 List<OrderProductItem> productItems, OrderStatus status, String cancellationNote) {
        this.id = orderId;
        this.userId = userId;
        this.date = date;
        this.total = total;
        this.productItems = productItems;
        this.status = status;
        this.cancellationNote = cancellationNote;
    }

    // Getter methods for each field

    /**
     * Gets the order ID.
     * @return The unique identifier for the order.
     */
    public String getOrderId() {
        return id;
    }

    /**
     * Gets the user ID of the order placer.
     * @return The unique identifier for the user.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the date of the order.
     * @return The date when the order was placed.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the total amount of the order.
     * @return The total cost of the order.
     */
    public double getTotal() {
        return total;
    }

    /**
     * Gets the product items in the order.
     * @return A list of product items associated with the order.
     */
    public List<OrderProductItem> getProductItems() {
        return productItems;
    }

    /**
     * Gets the status of the order.
     * @return The current status of the order.
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Gets the cancellation note for the order.
     * @return The note explaining the cancellation, if any.
     */
    public String getCancellationNote() {
        return cancellationNote;
    }

    // Parcelable implementation

    /**
     * Constructor to create an Order from a Parcel.
     * @param in The Parcel containing the order data.
     */
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
