package com.app.effistay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataRP implements Serializable {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseText")
    @Expose
    private String responseText;

    @SerializedName("responseData")
    @Expose
    private ResponseData responseData;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("booking_id")
    @Expose
    private String bookingId;

    @SerializedName("order_token")
    @Expose
    private OrderToken orderToken;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public OrderToken getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(OrderToken orderToken) {
        this.orderToken = orderToken;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public class ResponseData {

        @SerializedName("order_amount")
        @Expose
        private String orderAmount;
        @SerializedName("coupon_discount_amount")
        @Expose
        private String couponDiscountAmount;
        @SerializedName("final_amount")
        @Expose
        private String finalAmount;

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getCouponDiscountAmount() {
            return couponDiscountAmount;
        }

        public void setCouponDiscountAmount(String couponDiscountAmount) {
            this.couponDiscountAmount = couponDiscountAmount;
        }

        public String getFinalAmount() {
            return finalAmount;
        }

        public void setFinalAmount(String finalAmount) {
            this.finalAmount = finalAmount;
        }
    }

    public class OrderToken {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("cftoken")
        @Expose
        private String cftoken;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCftoken() {
            return cftoken;
        }

        public void setCftoken(String cftoken) {
            this.cftoken = cftoken;
        }
    }

}
