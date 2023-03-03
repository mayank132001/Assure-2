package com.app.effistay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrder {
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseText")
    @Expose
    private String responseText;
    @SerializedName("responseData")
    @Expose
    private List<ResponseDatum> responseData = null;

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

    public List<ResponseDatum> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ResponseDatum> responseData) {
        this.responseData = responseData;
    }
    public class ResponseDatum {

        @SerializedName("booking_no")
        @Expose
        private String bookingNo;
        @SerializedName("hotel_id")
        @Expose
        private String hotelId;
        @SerializedName("hotel_name")
        @Expose
        private String hotelName;
        @SerializedName("hotel_image")
        @Expose
        private String hotelImage;
        @SerializedName("booking_date")
        @Expose
        private String bookingDate;
        @SerializedName("checkin_time")
        @Expose
        private String checkinTime;
        @SerializedName("checkout_time")
        @Expose
        private String checkoutTime;
        @SerializedName("hours")
        @Expose
        private String hours;
        @SerializedName("hour_price")
        @Expose
        private String hourPrice;
        @SerializedName("no_of_person")
        @Expose
        private String noOfPerson;
        @SerializedName("total_room")
        @Expose
        private String totalRoom;
        @SerializedName("final_amount")
        @Expose
        private String finalAmount;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("payment_time")
        @Expose
        private String paymentTime;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("ref_no")
        @Expose
        private String refNo;
        @SerializedName("doc_image")
        @Expose
        private String docImage;

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public String getHotelId() {
            return hotelId;
        }

        public void setHotelId(String hotelId) {
            this.hotelId = hotelId;
        }

        public String getHotelName() {
            return hotelName;
        }

        public void setHotelName(String hotelName) {
            this.hotelName = hotelName;
        }

        public String getHotelImage() {
            return hotelImage;
        }

        public void setHotelImage(String hotelImage) {
            this.hotelImage = hotelImage;
        }

        public String getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }

        public String getCheckinTime() {
            return checkinTime;
        }

        public void setCheckinTime(String checkinTime) {
            this.checkinTime = checkinTime;
        }

        public String getCheckoutTime() {
            return checkoutTime;
        }

        public void setCheckoutTime(String checkoutTime) {
            this.checkoutTime = checkoutTime;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String getHourPrice() {
            return hourPrice;
        }

        public void setHourPrice(String hourPrice) {
            this.hourPrice = hourPrice;
        }

        public String getNoOfPerson() {
            return noOfPerson;
        }

        public void setNoOfPerson(String noOfPerson) {
            this.noOfPerson = noOfPerson;
        }

        public String getTotalRoom() {
            return totalRoom;
        }

        public void setTotalRoom(String totalRoom) {
            this.totalRoom = totalRoom;
        }

        public String getFinalAmount() {
            return finalAmount;
        }

        public void setFinalAmount(String finalAmount) {
            this.finalAmount = finalAmount;
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

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getDocImage() {
            return docImage;
        }

        public void setDocImage(String docImage) {
            this.docImage = docImage;
        }

    }
}
