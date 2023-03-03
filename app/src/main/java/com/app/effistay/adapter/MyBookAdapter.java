package com.app.effistay.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.response.MyOrder;
import com.app.effistay.R;
import com.app.effistay.interfaces.OnClick;
import com.app.effistay.util.Method;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyBookAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private Method method;
    private String type;
    private int columnWidth;
    List<MyOrder.ResponseDatum> roomLists;
    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;

    public MyBookAdapter(Activity activity, String type, OnClick onClick, List<MyOrder.ResponseDatum> roomLists) {
        this.activity = activity;
        this.roomLists = roomLists;
        this.type = type;
        method = new Method(activity, onClick);
        columnWidth = method.getScreenWidth();
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.list_my_book_item, parent, false);
            return new MyBookAdapter.ViewHolder(view);
//        } else if (viewType == VIEW_TYPE_LOADING) {
//            View v = LayoutInflater.from(activity).inflate(R.layout.layout_loading_item, parent, false);
//            return new ProgressViewHolder(v);
//        }
//        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

//        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {

            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tvName.setText(roomLists.get(position).getHotelName());
            viewHolder.tvCheckin.setText("Check In: "+roomLists.get(position).getCheckinTime());
            viewHolder.tvCheckout.setText("Check Out: "+roomLists.get(position).getCheckoutTime());
            viewHolder.tvCharge.setText("Payment: "+roomLists.get(position).getFinalAmount());
            viewHolder.layoutMain.setOnClickListener(v -> method.onClickAd(position, type, "", ""));

//        }

    }

    @Override
    public int getItemCount() {
        return roomLists.size();
    }

    public void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    private boolean isHeader(int position) {
        return position == 10;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return isHeader(position) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout layoutMain;
        TextView tvName,tvCheckin,tvCheckout,tvCharge;
        public ViewHolder(View itemView) {
            super(itemView);

            layoutMain = itemView.findViewById(R.id.layoutMain);
            tvName = itemView.findViewById(R.id.tvName);
            tvCheckin = itemView.findViewById(R.id.tvCheckin);
            tvCheckout = itemView.findViewById(R.id.tvCheckout);
            tvCharge = itemView.findViewById(R.id.tvCharge);

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
