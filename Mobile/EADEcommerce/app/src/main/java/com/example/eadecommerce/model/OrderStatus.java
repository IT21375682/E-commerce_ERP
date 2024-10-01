package com.example.eadecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class OrderStatus implements Parcelable {
    @SerializedName("pending")
    private boolean pending;

    @SerializedName("pendingDate")
    private String pendingDate;

    @SerializedName("processing")
    private boolean processing;

    @SerializedName("processingDate")
    private String processingDate;

    @SerializedName("dispatched")
    private boolean dispatched;

    @SerializedName("dispatchedDate")
    private String dispatchedDate;

    @SerializedName("partially_Delivered")
    private boolean partiallyDelivered;

    @SerializedName("partially_Delivered_Date")
    private String partiallyDeliveredDate;

    @SerializedName("delivered")
    private boolean delivered;

    @SerializedName("deliveredDate")
    private String deliveredDate;

    @SerializedName("canceled")
    private boolean canceled;

    @SerializedName("canceledDate")
    private String canceledDate;

    @SerializedName("stockReduced")
    private boolean stockReduced;

    // Constructor
    public OrderStatus(boolean pending, String pendingDate, boolean processing, String processingDate,
                       boolean dispatched, String dispatchedDate, boolean partiallyDelivered,
                       String partiallyDeliveredDate, boolean delivered, String deliveredDate,
                       boolean canceled, String canceledDate, boolean stockReduced) {
        this.pending = pending;
        this.pendingDate = pendingDate;
        this.processing = processing;
        this.processingDate = processingDate;
        this.dispatched = dispatched;
        this.dispatchedDate = dispatchedDate;
        this.partiallyDelivered = partiallyDelivered;
        this.partiallyDeliveredDate = partiallyDeliveredDate;
        this.delivered = delivered;
        this.deliveredDate = deliveredDate;
        this.canceled = canceled;
        this.canceledDate = canceledDate;
        this.stockReduced = stockReduced;
    }

    // Getter methods
    public boolean isPending() {
        return pending;
    }

    // Include other status fields and their respective getters here...

    // Parcelable implementation
    protected OrderStatus(Parcel in) {
        pending = in.readByte() != 0;
        pendingDate = in.readString();
        processing = in.readByte() != 0;
        processingDate = in.readString();
        dispatched = in.readByte() != 0;
        dispatchedDate = in.readString();
        partiallyDelivered = in.readByte() != 0;
        partiallyDeliveredDate = in.readString();
        delivered = in.readByte() != 0;
        deliveredDate = in.readString();
        canceled = in.readByte() != 0;
        canceledDate = in.readString();
        stockReduced = in.readByte() != 0;
    }

    public static final Creator<OrderStatus> CREATOR = new Creator<OrderStatus>() {
        @Override
        public OrderStatus createFromParcel(Parcel in) {
            return new OrderStatus(in);
        }

        @Override
        public OrderStatus[] newArray(int size) {
            return new OrderStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (pending ? 1 : 0));
        dest.writeString(pendingDate);
        dest.writeByte((byte) (processing ? 1 : 0));
        dest.writeString(processingDate);
        dest.writeByte((byte) (dispatched ? 1 : 0));
        dest.writeString(dispatchedDate);
        dest.writeByte((byte) (partiallyDelivered ? 1 : 0));
        dest.writeString(partiallyDeliveredDate);
        dest.writeByte((byte) (delivered ? 1 : 0));
        dest.writeString(deliveredDate);
        dest.writeByte((byte) (canceled ? 1 : 0));
        dest.writeString(canceledDate);
        dest.writeByte((byte) (stockReduced ? 1 : 0));
    }

    public boolean isDelivered() {
        return delivered;
    }

    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "pending=" + pending +
                ", pendingDate='" + pendingDate + '\'' +
                ", processing=" + processing +
                ", processingDate='" + processingDate + '\'' +
                ", dispatched=" + dispatched +
                ", dispatchedDate='" + dispatchedDate + '\'' +
                ", partiallyDelivered=" + partiallyDelivered +
                ", partiallyDeliveredDate='" + partiallyDeliveredDate + '\'' +
                ", delivered=" + delivered +
                ", deliveredDate='" + deliveredDate + '\'' +
                ", canceled=" + canceled +
                ", canceledDate='" + canceledDate + '\'' +
                ", stockReduced=" + stockReduced +
                '}';
    }


}
