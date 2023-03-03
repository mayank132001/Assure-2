package com.app.effistay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateOrder {
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("ref_no")
    @Expose
    private String refNo;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("payment_time")
    @Expose
    private String paymentTime;
    @SerializedName("payment_status")
    @Expose
    private Integer paymentStatus;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
