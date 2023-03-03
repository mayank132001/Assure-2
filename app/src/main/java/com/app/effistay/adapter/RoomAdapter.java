package com.app.effistay.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.item.RoomList;
import com.bumptech.glide.Glide;
import com.app.effistay.R;
import com.app.effistay.interfaces.OnClick;
import com.app.effistay.util.Method;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private Method method;
    private String type;
    private int columnWidth;
    private List<RoomList> roomLists;

    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;

    public RoomAdapter(Activity activity, List<RoomList> roomLists, String type, OnClick onClick) {
        this.activity = activity;
        this.roomLists = roomLists;
        this.type = type;
        method = new Method(activity, onClick);
        columnWidth = method.getScreenWidth();
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.room_adapter, parent, false);
            return new RoomAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(activity).inflate(R.layout.layout_loading_item, parent, false);
            return new ProgressViewHolder(v);
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.imageViewRoom.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, columnWidth / 2));
            viewHolder.view.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, columnWidth / 2));

            Glide.with(activity).load(roomLists.get(position).getRoom_image_thumb())
                    .placeholder(R.drawable.placeholder_landscape)
                    .into(viewHolder.imageViewRoom);

            viewHolder.textViewRoomName.setText(roomLists.get(position).getRoom_name());
            viewHolder.textViewPrice.setText(roomLists.get(position).getRoom_price());
            viewHolder.textViewTotalRate.setText("(" + roomLists.get(position).getTotal_rate() + ")");
            viewHolder.ratingBar.setRating(Float.parseFloat(roomLists.get(position).getRate_avg()));

            viewHolder.textViewTotalRate.setTypeface(null);

            viewHolder.constraintLayout.setOnClickListener(v -> method.onClickAd(position, type, roomLists.get(position).getId(), roomLists.get(position).getRoom_name()));

        }

    }

    @Override
    public int getItemCount() {
        return roomLists.size() + 1;
    }

    public void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    private boolean isHeader(int position) {
        return position == roomLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private RatingView ratingBar;
        private ImageView imageViewRoom;
        private ConstraintLayout constraintLayout;
        private MaterialTextView textViewRoomName, textViewPrice, textViewTotalRate;

        public ViewHolder(View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.con_room_adapter);
            imageViewRoom = itemView.findViewById(R.id.imageView_room_adapter);
            view = itemView.findViewById(R.id.view_room_adapter);
            textViewRoomName = itemView.findViewById(R.id.textView_roomName_room_adapter);
            textViewPrice = itemView.findViewById(R.id.textView_roomPrice_room_adapter);
            textViewTotalRate = itemView.findViewById(R.id.textView_totalRate_room_adapter);
            ratingBar = itemView.findViewById(R.id.ratingBar_room_adapter);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public static ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar_loading);
        }
    }

}
