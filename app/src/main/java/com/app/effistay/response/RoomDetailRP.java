package com.app.effistay.response;

import com.app.effistay.item.RoomAmenitiesList;
import com.app.effistay.item.RoomSlider;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RoomDetailRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("id")
    private String id;

    @SerializedName("room_name")
    private String room_name;

    @SerializedName("room_image")
    private String room_image;

    @SerializedName("room_image_thumb")
    private String room_image_thumb;

    @SerializedName("galley_image")
    private List<RoomSlider> roomSliders;

    @SerializedName("room_description")
    private String room_description;

    @SerializedName("room_rules")
    private String room_rules;

    @SerializedName("room_price")
    private String room_price;

    @SerializedName("total_rate")
    private String total_rate;

    @SerializedName("rate_avg")
    private String rate_avg;

    @SerializedName("room_amenities")
    private List<RoomAmenitiesList> roomAmenitiesLists;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getRoom_image() {
        return room_image;
    }

    public String getRoom_image_thumb() {
        return room_image_thumb;
    }

    public List<RoomSlider> getRoomSliders() {
        return roomSliders;
    }

    public String getRoom_description() {
        return room_description;
    }

    public String getRoom_rules() {
        return room_rules;
    }

    public String getRoom_price() {
        return room_price;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    public String getRate_avg() {
        return rate_avg;
    }

    public List<RoomAmenitiesList> getRoomAmenitiesLists() {
        return roomAmenitiesLists;
    }
}
