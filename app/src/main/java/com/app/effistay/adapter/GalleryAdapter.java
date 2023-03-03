package com.app.effistay.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.item.GalleryList;
import com.app.effistay.interfaces.OnClick;
import com.app.effistay.R;
import com.app.effistay.util.Method;
import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private String type;
    private int columnWidth;
    private Method method;
    private List<GalleryList> galleryLists;

    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;

    public GalleryAdapter(Activity activity, String type, List<GalleryList> galleryLists, OnClick onClick) {
        this.activity = activity;
        this.galleryLists = galleryLists;
        this.type = type;
        method = new Method(activity, onClick);
        Resources r = activity.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
        columnWidth = (int) ((method.getScreenWidth() - ((4 + 2) * padding)) / 2);
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.gallery_adapter, parent, false);
            return new GalleryAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(activity).inflate(R.layout.layout_loading_item, parent, false);
            return new ProgressViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.imageView.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, columnWidth));
            viewHolder.view.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, columnWidth));

            Glide.with(activity).load(galleryLists.get(position).getCategory_image_thumb())
                    .placeholder(R.drawable.placeholder_portable)
                    .into(viewHolder.imageView);

            viewHolder.textView.setText(galleryLists.get(position).getCategory_name());

            viewHolder.constraintLayout.setOnClickListener(v -> method.onClickAd(position, type, galleryLists.get(position).getCid(), galleryLists.get(position).getCategory_name()));

        }

    }

    @Override
    public int getItemCount() {
        return galleryLists.size() + 1;
    }

    public void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    private boolean isHeader(int position) {
        return position == galleryLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView imageView;
        private MaterialTextView textView;
        private ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.view_gallery_adapter);
            imageView = itemView.findViewById(R.id.imageView_gallery_adapter);
            constraintLayout = itemView.findViewById(R.id.con_gallery_adapter);
            textView = itemView.findViewById(R.id.textView_gallery_adapter);

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

