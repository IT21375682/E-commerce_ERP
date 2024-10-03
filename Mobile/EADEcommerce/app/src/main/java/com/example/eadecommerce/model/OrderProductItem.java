package com.example.eadecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * The OrderProductItem class represents an individual product item in an order.
 * It contains details such as the product ID, quantity, and delivery status.
 * It implements Parcelable for easy passing between activities.
 */
public class OrderProductItem implements Parcelable {
    // Fields representing the components of an order product item
    @SerializedName("productId")
    private String productId;

    @SerializedName("count")
    private int count;

    @SerializedName("delivered")
    private boolean delivered;

    // Constructor to initialize all fields
    public OrderProductItem(String productId, int count, boolean delivered) {
        this.productId = productId;
        this.count = count;
        this.delivered = delivered;
    }

    // Getter methods for each field

    /**
     * Gets the product ID.
     * @return The unique identifier for the product.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Gets the count of the product in the order.
     * @return The quantity of the product ordered.
     */
    public int getCount() {
        return count;
    }

    /**
     * Checks if the product has been delivered.
     * @return True if the product has been delivered, false otherwise.
     */
    public boolean isDelivered() {
        return delivered;
    }

    // Parcelable implementation

    /**
     * Constructor to create an OrderProductItem from a Parcel.
     * @param in The Parcel containing the product item data.
     */
    protected OrderProductItem(Parcel in) {
        productId = in.readString();
        count = in.readInt();
        delivered = in.readByte() != 0;
    }

    public static final Creator<OrderProductItem> CREATOR = new Creator<OrderProductItem>() {
        @Override
        public OrderProductItem createFromParcel(Parcel in) {
            return new OrderProductItem(in);
        }

        @Override
        public OrderProductItem[] newArray(int size) {
            return new OrderProductItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeInt(count);
        dest.writeByte((byte) (delivered ? 1 : 0));
    }

    @Override
    public String toString() {
        return "OrderProductItem{" +
                "productId='" + productId + '\'' +
                ", count=" + count +
                ", delivered=" + delivered +
                '}';
    }
}
