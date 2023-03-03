package com.app.effistay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Hotels implements Serializable {

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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("max_room")
        @Expose
        private String maxRoom;
        @SerializedName("total_room")
        @Expose
        private String totalRoom;
        @SerializedName("price")
        @Expose
        private Price price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMaxRoom() {
            return maxRoom;
        }

        public void setMaxRoom(String maxRoom) {
            this.maxRoom = maxRoom;
        }

        public String getTotalRoom() {
            return totalRoom;
        }

        public void setTotalRoom(String totalRoom) {
            this.totalRoom = totalRoom;
        }

        public Price getPrice() {
            return price;
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        public class Price {

            @SerializedName("hotel_3hrs_cost")
            @Expose
            private String hotel3hrsCost;
            @SerializedName("hotel_6hrs_cost")
            @Expose
            private String hotel6hrsCost;
            @SerializedName("hotel_9hrs_cost")
            @Expose
            private String hotel9hrsCost;
            @SerializedName("hotel_3hrs_discounted_cost")
            @Expose
            private String hotel3hrsDiscountedCost;
            @SerializedName("hotel_6hrs_discounted_cost")
            @Expose
            private String hotel6hrsDiscountedCost;
            @SerializedName("hotel_9hrs_discounted_cost")
            @Expose
            private String hotel9hrsDiscountedCost;

            public String getHotel3hrsCost() {
                return hotel3hrsCost;
            }

            public void setHotel3hrsCost(String hotel3hrsCost) {
                this.hotel3hrsCost = hotel3hrsCost;
            }

            public String getHotel6hrsCost() {
                return hotel6hrsCost;
            }

            public void setHotel6hrsCost(String hotel6hrsCost) {
                this.hotel6hrsCost = hotel6hrsCost;
            }

            public String getHotel9hrsCost() {
                return hotel9hrsCost;
            }

            public void setHotel9hrsCost(String hotel9hrsCost) {
                this.hotel9hrsCost = hotel9hrsCost;
            }

            public String getHotel3hrsDiscountedCost() {
                return hotel3hrsDiscountedCost;
            }

            public void setHotel3hrsDiscountedCost(String hotel3hrsDiscountedCost) {
                this.hotel3hrsDiscountedCost = hotel3hrsDiscountedCost;
            }

            public String getHotel6hrsDiscountedCost() {
                return hotel6hrsDiscountedCost;
            }

            public void setHotel6hrsDiscountedCost(String hotel6hrsDiscountedCost) {
                this.hotel6hrsDiscountedCost = hotel6hrsDiscountedCost;
            }

            public String getHotel9hrsDiscountedCost() {
                return hotel9hrsDiscountedCost;
            }

            public void setHotel9hrsDiscountedCost(String hotel9hrsDiscountedCost) {
                this.hotel9hrsDiscountedCost = hotel9hrsDiscountedCost;
            }

        }
    }

}
