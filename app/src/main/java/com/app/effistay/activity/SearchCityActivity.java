package com.app.effistay.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.adapter.SearchCityAdapter;
import com.app.effistay.interfaces.OnClick;
import com.app.effistay.response.Cities;
import com.app.effistay.rest.ApiClient;
import com.app.effistay.rest.ApiInterface;
import com.app.effistay.util.Method;
import com.app.effistay.R;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchCityActivity extends AppCompatActivity {
    RecyclerView rvSearchCity;
    ProgressBar progressBar;
    private Method method;
    private OnClick onClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_search_city);
        toolbar.setTitle("Hotels");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressBar = findViewById(R.id.progressBar);
        rvSearchCity = findViewById(R.id.rvSearchCity);
        onClick = (position, type, id, title) -> {
            Intent intent = new Intent();
            intent.putExtra("selectCityID",id);
            intent.putExtra("selectCityName",title);
            setResult(Activity.RESULT_OK,intent);
            finish();
        };
        method = new Method(this, onClick);
        getCities();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getCities() {


        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> requestBody = new HashMap<>();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Cities> call = apiService.getCities(requestBody);
        call.enqueue(new Callback<Cities>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<Cities> call, @NotNull Response<Cities> response) {
                int statusCode = response.code();

                try {
                    Cities cities = response.body();
                    assert cities != null;
                    if (cities.getResponseCode() == 1) {
                        rvSearchCity.setAdapter(new SearchCityAdapter(SearchCityActivity.this, onClick,cities.getResponseData()));
                    } else {
                        method.suspend(cities.getResponseText());
                    }

                } catch (Exception e) {
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    Log.d("exception_error", e.toString());
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<Cities> call, @NotNull Throwable t) {
                // Log error here since request failed
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });


    }
}
