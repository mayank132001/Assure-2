package com.app.effistay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CashfreePayment {
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("referenceId")
    @Expose
    private String referenceId;
    @SerializedName("orderAmount")
    @Expose
    private String orderAmount;
    @SerializedName("txStatus")
    @Expose
    private String txStatus;
    @SerializedName("txMsg")
    @Expose
    private String txMsg;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("txTime")
    @Expose
    private String txTime;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("status")
    @Expose
    private String status;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
    }

    public String getTxMsg() {
        return txMsg;
    }

    public void setTxMsg(String txMsg) {
        this.txMsg = txMsg;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
