package com.example.eadecommerce.model;

public class SingleOrderStatus {
    private boolean pending;
    private String pendingDate;
    private boolean processing;
    private String processingDate;
    private boolean dispatched;
    private String dispatchedDate;
    private boolean partiallyDelivered;
    private String partiallyDeliveredDate;
    private boolean delivered;
    private String deliveredDate;
    private boolean canceled;
    private String canceledDate;
    private boolean stockReduced;

    // Getters and Setters
    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getPendingDate() {
        return pendingDate;
    }

    public void setPendingDate(String pendingDate) {
        this.pendingDate = pendingDate;
    }

    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }

    public boolean isDispatched() {
        return dispatched;
    }

    public void setDispatched(boolean dispatched) {
        this.dispatched = dispatched;
    }

    public String getDispatchedDate() {
        return dispatchedDate;
    }

    public void setDispatchedDate(String dispatchedDate) {
        this.dispatchedDate = dispatchedDate;
    }

    public boolean isPartiallyDelivered() {
        return partiallyDelivered;
    }

    public void setPartiallyDelivered(boolean partiallyDelivered) {
        this.partiallyDelivered = partiallyDelivered;
    }

    public String getPartiallyDeliveredDate() {
        return partiallyDeliveredDate;
    }

    public void setPartiallyDeliveredDate(String partiallyDeliveredDate) {
        this.partiallyDeliveredDate = partiallyDeliveredDate;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public String getCanceledDate() {
        return canceledDate;
    }

    public void setCanceledDate(String canceledDate) {
        this.canceledDate = canceledDate;
    }

    public boolean isStockReduced() {
        return stockReduced;
    }

    public void setStockReduced(boolean stockReduced) {
        this.stockReduced = stockReduced;
    }
}

