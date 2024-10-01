package com.example.eadecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class OrderProductItem implements Parcelable {
    @SerializedName("productId")
    private String productId;

    @SerializedName("count")
    private int count;

    @SerializedName("delivered")
    private boolean delivered;

    // Constructor
    public OrderProductItem(String productId, int count, boolean delivered) {
        this.productId = productId;
        this.count = count;
        this.delivered = delivered;
    }

    // Getter methods
    public String getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }

    public boolean isDelivered() {
        return delivered;
    }

    // Parcelable implementation
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
