package com.app.effistay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.interfaces.OnClick;
import com.app.effistay.util.Method;
import com.app.effistay.R;
import com.app.effistay.response.Cities;

import java.util.List;

public class SearchCityAdapter extends RecyclerView.Adapter<SearchCityAdapter.ViewHolder> {

    private Activity activity;
    private int lastPosition = -1;
    private Method method;
    private String type;
    List<Cities.ResponseDatum> responseData;
    public SearchCityAdapter(Activity activity, OnClick onClick, List<Cities.ResponseDatum> responseData) {
        this.activity = activity;
        this.responseData = responseData;
        method = new Method(activity, onClick);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_search_city_item, parent, false);
        return new SearchCityAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(responseData.get(position).getName());
        holder.tvName.setOnClickListener(v -> method.onClickAd(position, type, responseData.get(position).getId(), responseData.get(position).getName()));

    }

    @Override
    public int getItemCount() {
        return responseData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);

        }
    }
}
