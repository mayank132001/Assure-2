package com.app.effistay.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.activity.MyOrderDetailActivity;
import com.app.effistay.rest.ApiInterface;
import com.app.effistay.R;
import com.app.effistay.activity.MainActivity;
import com.app.effistay.activity.RoomDetail;
import com.app.effistay.adapter.MyBookAdapter;
import com.app.effistay.interfaces.OnClick;
import com.app.effistay.response.MyOrder;
import com.app.effistay.rest.ApiClient;
import com.app.effistay.util.Method;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingFragment extends Fragment {

    private Method method;
    private OnClick onClick;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<MyOrder.ResponseDatum> roomLists;
    private MyBookAdapter myBookAdapter;
    private ConstraintLayout conNoData;
    private Boolean isOver = false;
    private int paginationIndex = 1;

    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.room_fragment, container, false);

        if (MainActivity.toolbar != null) {
            MainActivity.toolbar.setTitle(getResources().getString(R.string.my_booking));
        }

        roomLists = new ArrayList<>();

        onClick = (position, type, id, title) -> {
            try {
                String[] array = roomLists.get(position).getCheckinTime().split(" ");
                String checkInDate = array[0];
                String checkInTime = array[1];
                startActivity(new Intent(getActivity(), MyOrderDetailActivity.class)
                        .putExtra("hotel_id", roomLists.get(position).getHotelId())
                        .putExtra("title", title)
                        .putExtra("position", position)
                        .putExtra("checkInDate", checkInDate)
                        .putExtra("checkInTime", checkInTime)
                        .putExtra("selHours", roomLists.get(position).getHours())
                        .putExtra("checkOutDetails", roomLists.get(position).getCheckoutTime()));
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        method = new Method(getActivity(), onClick);

        conNoData = view.findViewById(R.id.con_noDataFound);
        progressBar = view.findViewById(R.id.progressBar_room_fragment);
        recyclerView = view.findViewById(R.id.recyclerView_room_fragment);

        conNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                if (!isOver) {
//                    new Handler().postDelayed(() -> {
//                        paginationIndex++;
//                        callData();
//                    }, 1000);
//                } else {
//                    myBookAdapter.hideHeader();
//                }
//            }
//        });

        callData();
        myBookAdapter = new MyBookAdapter(getActivity(), "room", onClick, roomLists);
        recyclerView.setAdapter(myBookAdapter);
        return view;
    }

    private void callData() {
        if (method.isNetworkAvailable()) {
            room();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
    }

    public void room() {

        if (getActivity() != null) {

            if (myBookAdapter == null) {
                roomLists.clear();
                progressBar.setVisibility(View.VISIBLE);
            }

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("customer_id", method.userId());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MyOrder> call = apiService.getMyOrders(requestBody);
            call.enqueue(new Callback<MyOrder>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<MyOrder> call, @NotNull Response<MyOrder> response) {
                    int statusCode = response.code();

                    if (getActivity() != null) {

                        try {

                            MyOrder myOrder = response.body();

                            assert myOrder != null;
                            if (myOrder.getResponseCode() == 1) {

//                                if (myOrder.getResponseData().size() == 0) {
//                                    if (myBookAdapter != null) {
//                                        myBookAdapter.hideHeader();
//                                        isOver = true;
//                                    }
//                                } else {
//                                    roomLists.addAll(myOrder.getResponseData());
//                                }
                                roomLists.addAll(myOrder.getResponseData());
                                if (myBookAdapter == null) {
                                    if (roomLists.size() != 0) {
                                        myBookAdapter = new MyBookAdapter(getActivity(), "room", onClick,roomLists);
                                        recyclerView.setAdapter(myBookAdapter);
                                    } else {
                                        conNoData.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    myBookAdapter.notifyDataSetChanged();
                                }

                            } else {
                                conNoData.setVisibility(View.VISIBLE);
                                method.alertBox(myOrder.getResponseText());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<MyOrder> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    progressBar.setVisibility(View.VISIBLE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }
    }

}
