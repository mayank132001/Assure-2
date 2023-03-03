package com.app.effistay.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.activity.GalleryDetail;
import com.app.effistay.activity.MainActivity;
import com.app.effistay.adapter.GalleryAdapter;
import com.app.effistay.interfaces.OnClick;
import com.app.effistay.item.GalleryList;
import com.app.effistay.R;
import com.app.effistay.util.API;
import com.app.effistay.util.EndlessRecyclerViewScrollListener;
import com.app.effistay.util.Method;
import com.app.effistay.response.GalleryCatRP;
import com.app.effistay.rest.ApiClient;
import com.app.effistay.rest.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {

    private Method method;
    private OnClick onClick;
    private ProgressBar progressBar;
    private List<GalleryList> galleryLists;
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private ConstraintLayout conNoData;
    private Boolean isOver = false;
    private int paginationIndex = 1;

    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.gallery_fragment, container, false);

        if (MainActivity.toolbar != null) {
            MainActivity.toolbar.setTitle(getResources().getString(R.string.gallery));
        }

        galleryLists = new ArrayList<>();

        onClick = (position, type, id, title) -> startActivity(new Intent(getActivity(), GalleryDetail.class)
                .putExtra("id", id)
                .putExtra("title", title)
                .putExtra("position", position));
        method = new Method(getActivity(), onClick);

        conNoData = view.findViewById(R.id.con_noDataFound);
        progressBar = view.findViewById(R.id.progressBar_gallery_fragment);
        recyclerView = view.findViewById(R.id.recyclerView_gallery_fragment);

        conNoData.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (galleryAdapter.getItemViewType(position) == 0) {
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(() -> {
                        paginationIndex++;
                        callData();
                    }, 1000);
                } else {
                    galleryAdapter.hideHeader();
                }
            }
        });

        callData();

        return view;
    }

    private void callData() {
        if (method.isNetworkAvailable()) {
            catGallery();
        } else {
            progressBar.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
    }

    private void catGallery() {

        if (getActivity() != null) {

            if (galleryAdapter == null) {
                galleryLists.clear();
                progressBar.setVisibility(View.VISIBLE);
            }

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("page", paginationIndex);
            jsObj.addProperty("method_name", "get_category");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<GalleryCatRP> call = apiService.getGallery(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<GalleryCatRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<GalleryCatRP> call, @NotNull Response<GalleryCatRP> response) {
                    int statusCode = response.code();

                    if (getActivity() != null) {

                        try {

                            GalleryCatRP galleryCatRP = response.body();

                            assert galleryCatRP != null;
                            if (galleryCatRP.getStatus().equals("1")) {

                                if (galleryCatRP.getGalleryLists().size() == 0) {
                                    if (galleryAdapter != null) {
                                        galleryAdapter.hideHeader();
                                        isOver = true;
                                    }
                                } else {
                                    galleryLists.addAll(galleryCatRP.getGalleryLists());
                                }

                                if (galleryAdapter == null) {
                                    if (galleryLists.size() != 0) {
                                        galleryAdapter = new GalleryAdapter(getActivity(), "gallery", galleryLists, onClick);
                                        recyclerView.setAdapter(galleryAdapter);
                                    } else {
                                        conNoData.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    galleryAdapter.notifyDataSetChanged();
                                }


                            } else {
                                conNoData.setVisibility(View.VISIBLE);
                                method.alertBox(galleryCatRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<GalleryCatRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("error_fail", t.toString());
                    progressBar.setVisibility(View.VISIBLE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

}
