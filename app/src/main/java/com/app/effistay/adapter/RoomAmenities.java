package com.app.effistay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.R;
import com.app.effistay.response.HotelDetail;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoomAmenities extends RecyclerView.Adapter<RoomAmenities.ViewHolder> {

    private Activity activity;
    private String type;
    private List<HotelDetail.Facily> roomAmenitiesLists;

    public RoomAmenities(Activity activity, String type, List<HotelDetail.Facily> roomAmenitiesLists) {
        this.activity = activity;
        this.type = type;
        this.roomAmenitiesLists = roomAmenitiesLists;
    }

    @NotNull
    @Override
    public RoomAmenities.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.room_amenities_adapter, parent, false);

        return new RoomAmenities.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomAmenities.ViewHolder holder, final int position) {
        holder.textViewAmenities.setText(roomAmenitiesLists.get(position).getName().trim());
    }

    @Override
    public int getItemCount() {
        return roomAmenitiesLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAmenities;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAmenities = itemView.findViewById(R.id.textView_name_room_amenities_adapter);

        }
    }
}