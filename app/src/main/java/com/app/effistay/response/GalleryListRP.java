package com.app.effistay.response;

import com.app.effistay.item.GalleryDetailList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GalleryListRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("SINGLE_HOTEL_APP")
    private List<GalleryDetailList> galleryDetailLists;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GalleryDetailList> getGalleryDetailLists() {
        return galleryDetailLists;
    }
}
