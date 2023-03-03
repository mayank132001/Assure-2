package com.app.effistay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HotelDetail implements Serializable {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseText")
    @Expose
    private String responseText;
    @SerializedName("responseData")
    @Expose
    private ResponseData responseData;

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

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public class ResponseData {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("map")
        @Expose
        private String map;
        @SerializedName("total_room")
        @Expose
        private String totalRoom;
        @SerializedName("available_rooms")
        @Expose
        private String availableRooms;
        @SerializedName("hotel_policy")
        @Expose
        private String hotelPolicy;
        @SerializedName("cancel_policy")
        @Expose
        private String cancelPolicy;
        @SerializedName("rules")
        @Expose
        private String rules;
        @SerializedName("images")
        @Expose
        private List<Image> images = null;
        @SerializedName("facilies")
        @Expose
        private List<Facily> facilies = null;
        @SerializedName("reviews")
        @Expose
        private List<Review> reviews = null;
        @SerializedName("room_price_hourly")
        @Expose
        private RoomPriceHourly roomPriceHourly;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
        }

        public String getTotalRoom() {
            return totalRoom;
        }

        public void setTotalRoom(String totalRoom) {
            this.totalRoom = totalRoom;
        }

        public String getAvailableRooms() {
            return availableRooms;
        }

        public void setAvailableRooms(String availableRooms) {
            this.availableRooms = availableRooms;
        }

        public String getHotelPolicy() {
            return hotelPolicy;
        }

        public void setHotelPolicy(String hotelPolicy) {
            this.hotelPolicy = hotelPolicy;
        }

        public String getCancelPolicy() {
            return cancelPolicy;
        }

        public void setCancelPolicy(String cancelPolicy) {
            this.cancelPolicy = cancelPolicy;
        }

        public String getRules() {
            return rules;
        }

        public void setRules(String rules) {
            this.rules = rules;
        }

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        public List<Facily> getFacilies() {
            return facilies;
        }

        public void setFacilies(List<Facily> facilies) {
            this.facilies = facilies;
        }

        public List<Review> getReviews() {
            return reviews;
        }

        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }

        public RoomPriceHourly getRoomPriceHourly() {
            return roomPriceHourly;
        }

        public void setRoomPriceHourly(RoomPriceHourly roomPriceHourly) {
            this.roomPriceHourly = roomPriceHourly;
        }
    }
    public class Image {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("image")
        @Expose
        private String image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

    public class Review {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("review")
        @Expose
        private String review;
        @SerializedName("rating")
        @Expose
        private Integer rating;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

    }

    public class RoomPriceHourly {

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

    public class Facily {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
