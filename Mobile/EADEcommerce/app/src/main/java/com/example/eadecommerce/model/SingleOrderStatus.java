package com.example.eadecommerce.model;

/**
 * The SingleOrderStatus class represents the various statuses of an order in the e-commerce system.
 * It includes fields for each status type (pending, processing, dispatched, partially delivered, delivered, canceled)
 * along with their respective dates and a flag to indicate if the stock has been reduced.
 * It provides getter and setter methods for each field.
 */
public class SingleOrderStatus {
    // Fields representing the details of an order product item status
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

    // Getter and setter methods

    /**
     * Checks if the order is pending.
     * @return True if the order is pending, false otherwise.
     */
    public boolean isPending() {
        return pending;
    }

    /**
     * Sets the pending status of the order.
     * @param pending The pending status to set.
     */
    public void setPending(boolean pending) {
        this.pending = pending;
    }

    /**
     * Gets the date when the order status was set to pending.
     * @return The pending date.
     */
    public String getPendingDate() {
        return pendingDate;
    }

    /**
     * Sets the date when the order status was set to pending.
     * @param pendingDate The pending date to set.
     */
    public void setPendingDate(String pendingDate) {
        this.pendingDate = pendingDate;
    }

    /**
     * Checks if the order is being processed.
     * @return True if the order is processing, false otherwise.
     */
    public boolean isProcessing() {
        return processing;
    }

    /**
     * Sets the processing status of the order.
     * @param processing The processing status to set.
     */
    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    /**
     * Gets the date when the order status was set to processing.
     * @return The processing date.
     */
    public String getProcessingDate() {
        return processingDate;
    }

    /**
     * Sets the date when the order status was set to processing.
     * @param processingDate The processing date to set.
     */
    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }

    /**
     * Checks if the order has been dispatched.
     * @return True if the order is dispatched, false otherwise.
     */
    public boolean isDispatched() {
        return dispatched;
    }

    /**
     * Sets the dispatched status of the order.
     * @param dispatched The dispatched status to set.
     */
    public void setDispatched(boolean dispatched) {
        this.dispatched = dispatched;
    }

    /**
     * Gets the date when the order status was set to dispatched.
     * @return The dispatched date.
     */
    public String getDispatchedDate() {
        return dispatchedDate;
    }

    /**
     * Sets the date when the order status was set to dispatched.
     * @param dispatchedDate The dispatched date to set.
     */
    public void setDispatchedDate(String dispatchedDate) {
        this.dispatchedDate = dispatchedDate;
    }

    /**
     * Checks if the order has been partially delivered.
     * @return True if the order is partially delivered, false otherwise.
     */
    public boolean isPartiallyDelivered() {
        return partiallyDelivered;
    }

    /**
     * Sets the partially delivered status of the order.
     * @param partiallyDelivered The partially delivered status to set.
     */
    public void setPartiallyDelivered(boolean partiallyDelivered) {
        this.partiallyDelivered = partiallyDelivered;
    }

    /**
     * Gets the date when the order status was set to partially delivered.
     * @return The partially delivered date.
     */
    public String getPartiallyDeliveredDate() {
        return partiallyDeliveredDate;
    }

    /**
     * Sets the date when the order status was set to partially delivered.
     * @param partiallyDeliveredDate The partially delivered date to set.
     */
    public void setPartiallyDeliveredDate(String partiallyDeliveredDate) {
        this.partiallyDeliveredDate = partiallyDeliveredDate;
    }

    /**
     * Checks if the order has been delivered.
     * @return True if the order is delivered, false otherwise.
     */
    public boolean isDelivered() {
        return delivered;
    }

    /**
     * Sets the delivered status of the order.
     * @param delivered The delivered status to set.
     */
    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    /**
     * Gets the date when the order status was set to delivered.
     * @return The delivered date.
     */
    public String getDeliveredDate() {
        return deliveredDate;
    }

    /**
     * Sets the date when the order status was set to delivered.
     * @param deliveredDate The delivered date to set.
     */
    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    /**
     * Checks if the order has been canceled.
     * @return True if the order is canceled, false otherwise.
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Sets the canceled status of the order.
     * @param canceled The canceled status to set.
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * Gets the date when the order status was set to canceled.
     * @return The canceled date.
     */
    public String getCanceledDate() {
        return canceledDate;
    }

    /**
     * Sets the date when the order status was set to canceled.
     * @param canceledDate The canceled date to set.
     */
    public void setCanceledDate(String canceledDate) {
        this.canceledDate = canceledDate;
    }

    /**
     * Checks if the stock has been reduced.
     * @return True if the stock has been reduced, false otherwise.
     */
    public boolean isStockReduced() {
        return stockReduced;
    }

    /**
     * Sets the stock reduced status of the order.
     * @param stockReduced The stock reduced status to set.
     */
    public void setStockReduced(boolean stockReduced) {
        this.stockReduced = stockReduced;
    }
}
