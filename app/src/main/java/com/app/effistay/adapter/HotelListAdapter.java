package com.app.effistay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.interfaces.OnClick;
import com.app.effistay.util.Method;
import com.bumptech.glide.Glide;
import com.app.effistay.R;
import com.app.effistay.response.Hotels;

import java.util.List;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.ViewHolder> {

    private Activity activity;
    private int lastPosition = -1;
    private Method method;
    private String type;
    List<Hotels.ResponseDatum> responseData;
    public HotelListAdapter(Activity activity, OnClick onClick, List<Hotels.ResponseDatum> responseData) {
        this.activity = activity;
        this.responseData = responseData;
        method = new Method(activity, onClick);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_hotel_item, parent, false);
        return new HotelListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(activity).load(responseData.get(position).getImage())
                .placeholder(R.drawable.placeholder_portable)
                .into(holder.ivHotel);
        holder.tvName.setText(responseData.get(position).getName());
        holder.tvAddress.setText(responseData.get(position).getDescription());
        if (responseData.get(position).getPrice()!=null){
            holder.tv3H.setText("3 hours\nRs."+responseData.get(position).getPrice().getHotel3hrsDiscountedCost());
            holder.tv6H.setText("6 hours\nRs."+responseData.get(position).getPrice().getHotel6hrsDiscountedCost());
            holder.tv12H.setText("12/24 hours\nRs."+responseData.get(position).getPrice().getHotel9hrsDiscountedCost());
        }
        holder.layoutMain.setOnClickListener(v -> method.onClickAd(position, type, responseData.get(position).getId().toString(), responseData.get(position).getName()));
        holder.tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.tvAddress.getMaxLines()==1){
                    holder.tvAddress.setMaxLines(Integer.MAX_VALUE);
                    holder.tvReadMore.setText("Read Less");
                }else {
                    holder.tvAddress.setMaxLines(1);
                    holder.tvReadMore.setText("Read More");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return responseData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivHotel;
        private CardView layoutMain;
        TextView tvName,tvAddress,tvReadMore,tv3H,tv6H,tv12H;
        public ViewHolder(View itemView) {
            super(itemView);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            ivHotel = itemView.findViewById(R.id.ivHotel);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvReadMore = itemView.findViewById(R.id.tvReadMore);
            tv3H = itemView.findViewById(R.id.tv3H);
            tv6H = itemView.findViewById(R.id.tv6H);
            tv12H = itemView.findViewById(R.id.tv12H);

        }
    }
}
