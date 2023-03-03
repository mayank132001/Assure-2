package com.app.effistay.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.R;
import com.app.effistay.adapter.HotelListAdapter;
import com.app.effistay.interfaces.OnClick;
import com.app.effistay.response.Hotels;
import com.app.effistay.rest.ApiClient;
import com.app.effistay.rest.ApiInterface;
import com.app.effistay.util.Method;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelListActivity extends AppCompatActivity {
    ProgressBar progressBar;
    RecyclerView rvHotels;
    private Method method;
    private OnClick onClick;
    private String selectCityID = "";
    private String checkInDate = "";
    private String checkInTime = "";
    private String selHours = "";

    int DAY_OF_MONTH = 0;
    int MONTH = 0;
    int YEAR = 0;
    int HOUR_OF_DAY = 0;
    int MINUTE = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_hotel_list);
        toolbar.setTitle("Hotels");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DAY_OF_MONTH =  getIntent().getIntExtra("DAY_OF_MONTH",0);
        MONTH =  getIntent().getIntExtra("MONTH",0);
        YEAR =  getIntent().getIntExtra("YEAR",0);
        HOUR_OF_DAY =  getIntent().getIntExtra("HOUR_OF_DAY",0);
        MINUTE =  getIntent().getIntExtra("MINUTE",0);
        selectCityID = getIntent().getStringExtra("selectCityID");
        checkInDate = getIntent().getStringExtra("checkInDate");
        checkInTime = getIntent().getStringExtra("checkInTime");
        selHours = getIntent().getStringExtra("selHours");
        progressBar = findViewById(R.id.progressBar);
        rvHotels = findViewById(R.id.rvHotels);
        onClick = (position, type, id, title) -> startActivity(new Intent(this, RoomDetail.class)
                .putExtra("DAY_OF_MONTH", DAY_OF_MONTH)
                .putExtra("MONTH", MONTH)
                .putExtra("YEAR", YEAR)
                .putExtra("HOUR_OF_DAY", HOUR_OF_DAY)
                .putExtra("MINUTE", MINUTE)
                .putExtra("hotel_id", id)
                .putExtra("title", title)
                .putExtra("position", position)
                .putExtra("checkInDate",checkInDate)
                .putExtra("checkInTime",checkInTime)
                .putExtra("selHours",selHours));
        method = new Method(this, onClick);
        getHotels();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getHotels() {

        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("customer_id",method.userId());
        requestBody.put("city_id",selectCityID);
        requestBody.put("checkin_date",checkInDate);
        requestBody.put("checkin_time",checkInTime);
        requestBody.put("hours",selHours);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Hotels> call = apiService.getHotelList(requestBody);
        call.enqueue(new Callback<Hotels>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<Hotels> call, @NotNull Response<Hotels> response) {
                try {
                    Hotels hotels = response.body();
                    assert hotels != null;
                    if (hotels.getResponseCode() == 1) {
                        rvHotels.setAdapter(new HotelListAdapter(HotelListActivity.this, onClick,hotels.getResponseData()));
                    } else {
                        method.suspend(hotels.getResponseText());
                    }

                } catch (Exception e) {
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    Log.d("exception_error", e.toString());
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<Hotels> call, @NotNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }
}
