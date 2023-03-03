package com.app.effistay.activity;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.effistay.R;
import com.app.effistay.adapter.RoomAmenities;
import com.app.effistay.adapter.SliderRoomDetailAdapter;
import com.app.effistay.response.CashfreePayment;
import com.app.effistay.response.DataRP;
import com.app.effistay.response.HotelDetail;
import com.app.effistay.rest.ApiClient;
import com.app.effistay.rest.ApiInterface;
import com.app.effistay.util.EnchantedViewPager;
import com.app.effistay.util.FileUtils;
import com.app.effistay.util.Method;
import com.app.effistay.util.Utils;
import com.cashfree.pg.CFPaymentService;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetail extends AppCompatActivity {

    private Method method;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private WebView webViewDes, webViewRules;
//    private ImageView imageViewRating;
    private RatingView ratingView;
    private RecyclerView recyclerViewRa;
    private EnchantedViewPager viewPager;
    private MaterialCardView cardViewBookNow, cardViewPayAtHotel, cardViewPayAdvance, cardviewSelectedHours;
    private ConstraintLayout conMain, conNoData, conRating,layoutAvlHours,layoutSoldOut;
    private SliderRoomDetailAdapter sliderRoomDetailAdapter;
    private MaterialTextView textViewRoomName, textViewTotalRate, lblSelectedHours;
    private EditText etCoupon;
    private TextView btnCouponApply, tvCouponDetails, tvReadMore,tvReadMoreRules;

    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    final Handler handler = new Handler();
    private Runnable Update;
    private InputMethodManager imm;
    TextView tvCheckINDate, tvCheckINTime, tvCheckOutDate, tvCheckOutTime, tv3H, tv6H, tv12H, tvBookRooms, tvUploadKYC, tvShowOnMap;
    AppCompatImageView tvRoomPlus, tvRoomMin;
    private String selHours = "";
    private String bookHours = "";
    private String HoursPrice = "";
    private String checkInDate = "";
    private String checkInTime = "";
    private String checkOutDate = "";
    private String checkOutTime = "";
    private String kycImgUrl = "";
    private String COUPON_CODE = "";
    HotelDetail hotel;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    int MAX_ROOMS = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;

    int DAY_OF_MONTH = 0;
    int MONTH = 0;
    int YEAR = 0;
    int HOUR_OF_DAY = 0;
    int MINUTE = 0;


    int DAY_OF_MONTH2 = 0;
    int MONTH2 = 0;
    int YEAR2 = 0;
    int HOUR_OF_DAY2 = 0;
    int MINUTE2 = 0;

    double FINAL_AMOUNT = 0.0;
    long checkOutTimeMillis = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        final Calendar newCalendar = Calendar.getInstance();
        YEAR2 = newCalendar.get(Calendar.YEAR);
        MONTH2 = newCalendar.get(Calendar.MONTH);
        DAY_OF_MONTH2 = newCalendar.get(Calendar.DAY_OF_MONTH);

        HOUR_OF_DAY2 = newCalendar.get(Calendar.HOUR_OF_DAY);
        MINUTE2 = newCalendar.get(Calendar.MINUTE);

        Intent intent = getIntent();
        String hotel_id = intent.getStringExtra("hotel_id");
        String title = intent.getStringExtra("title");
        int position = intent.getIntExtra("position", 0);
        checkInDate = getIntent().getStringExtra("checkInDate");
        checkInTime = getIntent().getStringExtra("checkInTime");
        selHours = getIntent().getStringExtra("selHours");
        DAY_OF_MONTH = getIntent().getIntExtra("DAY_OF_MONTH", 0);
        MONTH = getIntent().getIntExtra("MONTH", 0);
        YEAR = getIntent().getIntExtra("YEAR", 0);
        HOUR_OF_DAY = getIntent().getIntExtra("HOUR_OF_DAY", 0);
        MINUTE = getIntent().getIntExtra("MINUTE", 0);
        method = new Method(RoomDetail.this);
        method.forceRTLIfSupported();

        progressDialog = new ProgressDialog(RoomDetail.this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_rd);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        conMain = findViewById(R.id.con_rd);
        progressBar = findViewById(R.id.progressBar_rd);
        conNoData = findViewById(R.id.con_noDataFound);
        layoutAvlHours = findViewById(R.id.layoutAvlHours);
        viewPager = findViewById(R.id.viewPager_rd);
        textViewRoomName = findViewById(R.id.textView_roomName_rd);
        textViewTotalRate = findViewById(R.id.textView_totalRating_rd);
        webViewDes = findViewById(R.id.webView_des_rd);
        webViewRules = findViewById(R.id.webView_rules_rd);
        ratingView = findViewById(R.id.ratingBar_rd);
        conRating = findViewById(R.id.con_rating_rd);
        recyclerViewRa = findViewById(R.id.recyclerView_roomAmenities_rd);
        cardViewBookNow = findViewById(R.id.cardView_bookNow_rd);
        cardViewPayAtHotel = findViewById(R.id.cardView_pay_at_hotel);
        cardViewPayAdvance = findViewById(R.id.cardView_pay_advance);
        cardviewSelectedHours = findViewById(R.id.cardView_selected_hours);
//        imageViewRating = findViewById(R.id.imageView_rating_rd);
        lblSelectedHours = findViewById(R.id.lblSelectedHours);
        layoutSoldOut = findViewById(R.id.layoutSoldOut);
        tv3H = findViewById(R.id.tv3H);
        tv6H = findViewById(R.id.tv6H);
        tv12H = findViewById(R.id.tv12H);
        tvCheckINDate = findViewById(R.id.tvCheckINDate);
        tvCheckINTime = findViewById(R.id.tvCheckINTime);
        tvCheckOutDate = findViewById(R.id.tvCheckOutDate);
        tvCheckOutTime = findViewById(R.id.tvCheckOutTime);
        tvRoomPlus = findViewById(R.id.tvRoomPlus);
        tvBookRooms = findViewById(R.id.tvBookRooms);
        tvRoomMin = findViewById(R.id.tvRoomMin);
        tvUploadKYC = findViewById(R.id.tvUploadKYC);
        tvShowOnMap = findViewById(R.id.tvShowOnMap);
        etCoupon = findViewById(R.id.etCoupon);
        btnCouponApply = findViewById(R.id.btnCouponApply);
        tvCouponDetails = findViewById(R.id.tvCouponDetails);
        tvReadMore = findViewById(R.id.tvReadMore);
        tvReadMoreRules = findViewById(R.id.tvReadMoreRules);

        tvCheckINDate.setText(checkInDate);
        tvCheckINTime.setText(checkInTime);

        tvShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMap(Uri.parse(hotel.getResponseData().getMap()));
            }
        });

        tvRoomPlus.setOnClickListener(v -> {
            int counter = Integer.parseInt(tvBookRooms.getText().toString());
            if (counter < MAX_ROOMS) {
                int tot = counter + 1;
                tvBookRooms.setText(tot + "");
                FINAL_AMOUNT = Double.parseDouble(tvBookRooms.getText().toString()) * Double.parseDouble(HoursPrice);
                lblSelectedHours.setText("Rs." + FINAL_AMOUNT);
            }
        });


        tvRoomMin.setOnClickListener(v -> {
            int counter = Integer.parseInt(tvBookRooms.getText().toString());
            if (counter > 1) {
                int tot = counter - 1;
                tvBookRooms.setText(tot + "");
                FINAL_AMOUNT = Double.parseDouble(tvBookRooms.getText().toString()) * Double.parseDouble(HoursPrice);
                lblSelectedHours.setText("Rs." + FINAL_AMOUNT);
            }
        });

        tv3H.setOnClickListener(v -> {
            selectHours("3");
        });

        tv6H.setOnClickListener(v -> {
            selectHours("6");
        });

        tv12H.setOnClickListener(v -> {
            selectHours("12");
        });

        tvCheckINDate.setOnClickListener(v -> {
            openDatePickerDialog();
        });

        tvCheckINTime.setOnClickListener(v -> {
            openTimePickerDialog();
        });

        tvCheckOutDate.setOnClickListener(v -> {
            openDatePicker2Dialog();
        });

        tvCheckOutTime.setOnClickListener(v -> {
            openTimePicker2Dialog();
        });

        tvUploadKYC.setOnClickListener(v -> {
            if (checkPermission()) {
                ImagePicker.with(this)
                        .start();
            } else {
                requestPermission();
            }

        });

        btnCouponApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (method.isLogin()){
                    if (!etCoupon.getText().toString().isEmpty() && !HoursPrice.isEmpty()) {
                        applyCouponAPI(etCoupon.getText().toString(), String.valueOf(FINAL_AMOUNT));
                    }
                }else {
                    Utils.showToast(RoomDetail.this, "Please login to book");
                }
            }
        });

        textViewTotalRate.setTypeface(null);

        conMain.setVisibility(View.GONE);
        conNoData.setVisibility(View.GONE);

        recyclerViewRa.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_ra = new LinearLayoutManager(RoomDetail.this);
        recyclerViewRa.setLayoutManager(layoutManager_ra);
        recyclerViewRa.setFocusable(false);

        LinearLayout linearLayout = findViewById(R.id.linearLayout_rd);
        method.bannerAd(linearLayout);

        if (method.isNetworkAvailable()) {
            getHotelDetail(hotel_id);
        } else {
            progressBar.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

        tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewRa.getHeight() == 300){
                    ViewGroup.LayoutParams params = recyclerViewRa.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    recyclerViewRa.setLayoutParams(params);
                    tvReadMore.setText("Read Less");
                }else {
                    ViewGroup.LayoutParams params = recyclerViewRa.getLayoutParams();
                    params.height = 300;
                    recyclerViewRa.setLayoutParams(params);
                    tvReadMore.setText("Read More");
                }
            }
        });

        tvReadMoreRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webViewRules.getHeight() == 300){
                    ViewGroup.LayoutParams params = webViewRules.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    webViewRules.setLayoutParams(params);
                    tvReadMoreRules.setText("Read Less");
                }else {
                    ViewGroup.LayoutParams params = webViewRules.getLayoutParams();
                    params.height = 300;
                    webViewRules.setLayoutParams(params);
                    tvReadMoreRules.setText("Read More");
                }
            }
        });

    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openTimePickerDialog() {
//        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                HOUR_OF_DAY = selectedHour;
//                MINUTE = selectedMinute;
//                tvCheckINTime.setText(selectedHour + ":" + selectedMinute);
//                checkInTime = selectedHour + ":" + selectedMinute;
//            }
//        }, HOUR_OF_DAY, MINUTE, true);//Yes 24 hour time
//        mTimePicker.setTitle("Select Time");
//        mTimePicker.show();

        int mHour, mMinute;
        final Calendar c = Calendar.getInstance();

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        HOUR_OF_DAY = hourOfDay;
                        MINUTE = minute;
                        tvCheckINTime.setText(hourOfDay + ":" + minute);
                        checkInTime = hourOfDay + ":" + minute;
                    }
                },
                HOUR_OF_DAY, MINUTE,
                true
        );
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                tpd.setThemeDark(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                tpd.setThemeDark(false);
                break;
        }
        if (Utils.checkDates(checkInDate)) {
            tpd.setMinTime(mHour, mMinute, 0); // MIN: hours, minute, secconds
        }
        tpd.show(getSupportFragmentManager(), "TimePickerDialog");
    }

    private void openDatePickerDialog() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                YEAR = year;
                MONTH = monthOfYear;
                DAY_OF_MONTH = dayOfMonth;
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvCheckINDate.setText(format.format(newDate.getTime()));
                checkInDate = format.format(newDate.getTime());
            }

        }, YEAR, MONTH, DAY_OF_MONTH);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void openTimePicker2Dialog() {
//        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                HOUR_OF_DAY2 = selectedHour;
//                MINUTE2 = selectedMinute;
//                tvCheckOutTime.setText(selectedHour + ":" + selectedMinute);
//                tvCheckOutTime.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
//                tvCheckOutTime.setTextColor(getResources().getColor(R.color.white));
//                checkOutTime = selectedHour + ":" + selectedMinute;
//                method.alertBox(getResources().getString(R.string.checkout_other_time_msg));
//            }
//        }, HOUR_OF_DAY2, MINUTE2, true);//Yes 24 hour time
//        mTimePicker.setTitle("Select Time");
//        mTimePicker.show();


        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        Utils.log("=== onTimeSet " + Utils.compareDate(checkInDate, checkOutDate) + " " + hourOfDay);
                        long elapsedDays = 0;
                        long elapsedHours = 0;
                        long elapsedMinutes = 0;
                        try {
                            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date startDate = dfDate.parse(checkInDate + " " + checkInTime);
                            Date endDate = dfDate.parse(checkOutDate + " " + hourOfDay + ":" + minute);
                            //milliseconds
                            long different = endDate.getTime() - startDate.getTime();

                            long secondsInMilli = 1000;
                            long minutesInMilli = secondsInMilli * 60;
                            long hoursInMilli = minutesInMilli * 60;
                            long daysInMilli = hoursInMilli * 24;

                            elapsedDays = different / daysInMilli;
                            different = different % daysInMilli;

                            elapsedHours = different / hoursInMilli;
                            different = different % hoursInMilli;

                            elapsedMinutes = different / minutesInMilli;
                            different = different % minutesInMilli;

                            Utils.log("==== aa " + String.format("%d days, %d hours, %d minutes%n",
                                    elapsedDays, elapsedHours, elapsedMinutes));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (bookHours.equals("6") && (elapsedDays > 0 || elapsedHours > 6 || (elapsedHours >= 6 && elapsedMinutes > 0))) {
                            selectHours("12");
                        } else if (bookHours.equals("3") && (elapsedDays > 0 || elapsedHours > 3 || (elapsedHours >= 3 && elapsedMinutes > 0))) {
                            selectHours("6");
                        } else if (!Utils.compareDate(checkInDate, checkOutDate) && hourOfDay > 11) {
                            FINAL_AMOUNT = Double.parseDouble(tvBookRooms.getText().toString()) * Double.parseDouble(HoursPrice);
                            FINAL_AMOUNT = FINAL_AMOUNT * 2;
                            lblSelectedHours.setText("Rs." + FINAL_AMOUNT);
                        } else {
                            FINAL_AMOUNT = Double.parseDouble(tvBookRooms.getText().toString()) * Double.parseDouble(HoursPrice);
                            lblSelectedHours.setText("Rs." + FINAL_AMOUNT);
                        }
                        HOUR_OF_DAY2 = hourOfDay;
                        MINUTE2 = minute;
                        tvCheckOutTime.setText(hourOfDay + ":" + minute);
                        tvCheckOutTime.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                        tvCheckOutTime.setTextColor(getResources().getColor(R.color.white));
                        checkOutTime = hourOfDay + ":" + minute;
                        method.alertBox(getResources().getString(R.string.checkout_other_time_msg));
                    }
                },
                HOUR_OF_DAY2, MINUTE2, true);
        if (Utils.checkDates(checkOutDate)) {
            tpd.setMinTime(HOUR_OF_DAY2, MINUTE2, 0); // MIN: hours, minute, secconds
        }
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                tpd.setThemeDark(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                tpd.setThemeDark(false);
                break;
        }
        tpd.show(getSupportFragmentManager(), "TimePickerDialog");
    }

    private void openDatePicker2Dialog() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerTheme,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                YEAR2 = year;
                MONTH2 = monthOfYear;
                DAY_OF_MONTH2 = dayOfMonth;
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvCheckOutDate.setText(format.format(newDate.getTime()));
                tvCheckOutDate.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                tvCheckOutDate.setTextColor(getResources().getColor(R.color.white));
                checkOutDate = format.format(newDate.getTime());

                long elapsedDays = 0;
                long elapsedHours = 0;
                long elapsedMinutes = 0;
                try {
                    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date startDate = dfDate.parse(checkInDate + " " + checkInTime);
                    Date endDate = dfDate.parse(checkOutDate + " " + HOUR_OF_DAY2 + ":" + MINUTE2);
                    //milliseconds
                    long different = endDate.getTime() - startDate.getTime();

                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    elapsedDays = different / daysInMilli;
                    different = different % daysInMilli;

                    elapsedHours = different / hoursInMilli;
                    different = different % hoursInMilli;

                    elapsedMinutes = different / minutesInMilli;
                    different = different % minutesInMilli;

                    Utils.log("==== aa " + String.format("%d days, %d hours, %d minutes%n",
                            elapsedDays, elapsedHours, elapsedMinutes));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if ((elapsedDays > 0 || elapsedHours > 6 || (elapsedHours >= 6 && elapsedMinutes > 0))) {
                    selectHours("12");
                } else if ((elapsedDays > 0 || elapsedHours > 3 || (elapsedHours >= 3 && elapsedMinutes > 0))) {
                    selectHours("6");
                } else if (!Utils.compareDate(checkInDate, checkOutDate) && HOUR_OF_DAY2 > 11) {
                    FINAL_AMOUNT = Double.parseDouble(tvBookRooms.getText().toString()) * Double.parseDouble(HoursPrice);
                    FINAL_AMOUNT = FINAL_AMOUNT * 2;
                    lblSelectedHours.setText("Rs." + FINAL_AMOUNT);
                } else {
                    FINAL_AMOUNT = Double.parseDouble(tvBookRooms.getText().toString()) * Double.parseDouble(HoursPrice);
                    lblSelectedHours.setText("Rs." + FINAL_AMOUNT);
                }
            }

        }, YEAR2, MONTH2, DAY_OF_MONTH2);
        datePickerDialog.getDatePicker().setMinDate(checkOutTimeMillis - 1000);
        long now = System.currentTimeMillis() - 1000;
        datePickerDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*1));
        datePickerDialog.show();
    }


    public void getHotelDetail(String hotel_id) {

        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("customer_id", method.userId());
        requestBody.put("hotel_id", hotel_id);
        requestBody.put("checkin_date", checkInDate);
        requestBody.put("checkin_time", checkInTime);
        requestBody.put("hours", selHours);
        Utils.log("==== getHotelDetail "+requestBody);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<HotelDetail> call = apiService.getHotelDetail(requestBody);
        call.enqueue(new Callback<HotelDetail>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<HotelDetail> call, @NotNull Response<HotelDetail> response) {
                try {
                    hotel = response.body();
                    assert hotel != null;
                    if (hotel.getResponseCode() == 1) {
                        if (hotel.getResponseData().getImages().size() != 0) {

                            int columnWidth = method.getScreenWidth();
                            viewPager.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, columnWidth / 2 + 80));

                            viewPager.useScale();
                            viewPager.removeAlpha();

                            sliderRoomDetailAdapter = new SliderRoomDetailAdapter(RoomDetail.this, "room_detail_slider", hotel.getResponseData().getImages());
                            viewPager.setAdapter(sliderRoomDetailAdapter);

                            Update = () -> {
                                if (viewPager.getCurrentItem() == (sliderRoomDetailAdapter.getCount() - 1)) {
                                    viewPager.setCurrentItem(0, true);
                                } else {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                }
                            };

                            if (sliderRoomDetailAdapter.getCount() > 1) {
                                timer = new Timer(); // This will create a new Thread
                                timer.schedule(new TimerTask() { // task to be scheduled
                                    @Override
                                    public void run() {
                                        handler.post(Update);
                                    }
                                }, DELAY_MS, PERIOD_MS);
                            }

                        }

                        textViewRoomName.setText(hotel.getResponseData().getName());
                        ViewGroup.LayoutParams paramsWeb = webViewRules.getLayoutParams();
                        paramsWeb.height = 300;
                        webViewRules.setLayoutParams(paramsWeb);
                        String mimeType = "text/html";
                        String encoding = "utf-8";
                        webViewRules.setBackgroundColor(Color.TRANSPARENT);
                        webViewRules.setFocusableInTouchMode(false);
                        webViewRules.setFocusable(false);
                        webViewRules.getSettings().setDefaultTextEncodingName("UTF-8");
                        webViewRules.getSettings().setJavaScriptEnabled(true);

                        String textRules = "<html dir=" + method.isWebViewTextRtl() + "><head>"
                                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/poppins_medium.ttf\")}body{font-family: MyFont;color: " + method.webViewText() + "line-height:1.6}"
                                + "a {color:" + method.webViewLink() + "text-decoration:none}"
                                + "</style></head>"
                                + "<body>"
                                + hotel.getResponseData().getRules()
                                + "</body></html>";

                        webViewRules.loadDataWithBaseURL(null, textRules, mimeType, encoding, null);

                        if (hotel.getResponseData().getFacilies().size() != 0) {
                            RoomAmenities roomAmenities = new RoomAmenities(RoomDetail.this, "room_amenities", hotel.getResponseData().getFacilies());
                            recyclerViewRa.setAdapter(roomAmenities);
                            ViewGroup.LayoutParams params = recyclerViewRa.getLayoutParams();
                            params.height = 300;
                            recyclerViewRa.setLayoutParams(params);
                        }
                        if (hotel.getResponseData().getRoomPriceHourly() != null) {
                            tv3H.setText("3 hours\nRs." + hotel.getResponseData().getRoomPriceHourly().getHotel3hrsDiscountedCost());
                            tv6H.setText("6 hours\nRs." + hotel.getResponseData().getRoomPriceHourly().getHotel6hrsDiscountedCost());
                            tv12H.setText("12/24 hours\nRs." + hotel.getResponseData().getRoomPriceHourly().getHotel9hrsDiscountedCost());
                        }
                        MAX_ROOMS = Integer.parseInt(hotel.getResponseData().getAvailableRooms());
                        if (MAX_ROOMS==0){
                            layoutAvlHours.setVisibility(View.GONE);
                            cardViewPayAtHotel.setVisibility(View.GONE);
                            cardViewPayAdvance.setVisibility(View.GONE);
                            cardViewBookNow.setVisibility(View.GONE);

                            layoutSoldOut.setVisibility(View.VISIBLE);
                        }else {
                            layoutAvlHours.setVisibility(View.VISIBLE);
                            cardViewPayAtHotel.setVisibility(View.VISIBLE);
                            cardViewPayAdvance.setVisibility(View.VISIBLE);
                            cardViewBookNow.setVisibility(View.VISIBLE);

                            layoutSoldOut.setVisibility(View.GONE);
                        }
                        cardViewBookNow.setOnClickListener(v -> {
                            if (method.isLogin()){
                                if (bookHours.isEmpty()) {
                                    Utils.showToast(RoomDetail.this, "Please select hours");
                                } else if (checkOutDate.isEmpty()) {
                                    Utils.showToast(RoomDetail.this, "Please select check out date");
                                } else if (checkOutTime.isEmpty()) {
                                    Utils.showToast(RoomDetail.this, "Please select check out time");
                                } /*else if (kycImgUrl.isEmpty()) {
                                Utils.showToast(RoomDetail.this, "Please upload kyc image");
                            }*/ else {
                                    bookHotelAPI(hotel_id);
                                }
                            }else {
                                Utils.showToast(RoomDetail.this, "Please login to book");
                            }

                        });

                        cardViewPayAdvance.setOnClickListener(v -> {
                            if (method.isLogin()){
                                if (bookHours.isEmpty()) {
                                    Utils.showToast(RoomDetail.this, "Please select hours");
                                } else if (checkOutDate.isEmpty()) {
                                    Utils.showToast(RoomDetail.this, "Please select check out date");
                                } else if (checkOutTime.isEmpty()) {
                                    Utils.showToast(RoomDetail.this, "Please select check out time");
                                } /*else if (kycImgUrl.isEmpty()) {
                                Utils.showToast(RoomDetail.this, "Please upload kyc image");
                            }*/ else {
                                    payAdvanceAPI(hotel_id);
                                }
                            }else {
                                Utils.showToast(RoomDetail.this, "Please login to pay advance");
                            }

                        });

                        cardViewPayAtHotel.setOnClickListener(v -> {
                            if (method.isLogin()){
                                if (bookHours.isEmpty()) {
                                    Utils.showToast(RoomDetail.this, "Please select hours");
                                } else if (checkOutDate.isEmpty()) {
                                    Utils.showToast(RoomDetail.this, "Please select check out date");
                                } else if (checkOutTime.isEmpty()) {
                                    Utils.showToast(RoomDetail.this, "Please select check out time");
                                } /*else if (kycImgUrl.isEmpty()) {
                                Utils.showToast(RoomDetail.this, "Please upload kyc image");
                            }*/ else {
                                    bookHotelWithCashAPI(hotel_id);
                                }
                            }else {
                                Utils.showToast(RoomDetail.this, "Please login to book");
                            }

                        });
                    } else {
                        method.suspend(hotel.getResponseText());
                    }
                    conMain.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    Log.d("exception_error", e.toString());
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<HotelDetail> call, @NotNull Throwable t) {
                Log.d("exception_error", "=== " + t.getMessage());
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    public void applyCouponAPI(String coupon_code, String order_amount) {

        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("customer_id", method.userId());
        requestBody.put("coupon_code", coupon_code);
        requestBody.put("order_amount", order_amount);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DataRP> call = apiService.applyCouponAPI(requestBody);
        call.enqueue(new Callback<DataRP>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {
                try {
                    DataRP dataRP = response.body();
                    assert dataRP != null;
                    if (dataRP.getResponseCode() == 1) {
                        COUPON_CODE = coupon_code;
                        etCoupon.setText("");
                        etCoupon.setVisibility(View.GONE);
                        btnCouponApply.setVisibility(View.GONE);
                        tvCouponDetails.setText("Rs." + dataRP.getResponseData().getCouponDiscountAmount() + " Discount Applied");
                        lblSelectedHours.setText("Rs." + dataRP.getResponseData().getFinalAmount());
                        FINAL_AMOUNT = Double.parseDouble(dataRP.getResponseData().getFinalAmount());
                    } else {
                        method.suspend(hotel.getResponseText());
                    }
                    conMain.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    Log.d("exception_error", e.toString());
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                Log.d("exception_error", "=== " + t.getMessage());
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    public void bookHotelAPI(String hotel_id) {
        Date date = Calendar.getInstance().getTime();
        String currentDate = format.format(date);
        progressBar.setVisibility(View.VISIBLE);

        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), method.userId());
        RequestBody hotelid = RequestBody.create(MediaType.parse("text/plain"), hotel_id);
        RequestBody bookingdate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
        RequestBody checkin_time = RequestBody.create(MediaType.parse("text/plain"), checkInDate + " " + checkInTime);
        RequestBody checkout_time = RequestBody.create(MediaType.parse("text/plain"), checkOutDate + " " + checkOutTime);
        RequestBody hour_price = RequestBody.create(MediaType.parse("text/plain"), HoursPrice);
        int noOfPerson = Integer.parseInt(tvBookRooms.getText().toString()) * 2;
        RequestBody no_of_person = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(noOfPerson));
        RequestBody total_room = RequestBody.create(MediaType.parse("text/plain"), tvBookRooms.getText().toString());
        Utils.log("=== FINAL_AMOUNT " + FINAL_AMOUNT + " " + noOfPerson);
        RequestBody final_amount = RequestBody.create(MediaType.parse("text/plain"), HoursPrice);
        RequestBody payment_type = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody payment_ref_no = RequestBody.create(MediaType.parse("text/plain"), "REF787867867868");
        RequestBody coupon_code = RequestBody.create(MediaType.parse("text/plain"), COUPON_CODE);
        MultipartBody.Part fileBody;
        if (kycImgUrl.isEmpty()) {
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            fileBody = MultipartBody.Part.createFormData(
                    "image", "",
                    fileRequestBody
            );
        } else {
            File file = new File(kycImgUrl);
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileBody = MultipartBody.Part.createFormData(
                    "image", file.getName(),
                    fileRequestBody
            );
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DataRP> call = apiService.bookHotel(customer_id, hotelid, bookingdate, checkin_time, checkout_time, hour_price,
                no_of_person, total_room, final_amount, payment_type, payment_ref_no, coupon_code, fileBody);
        call.enqueue(new Callback<DataRP>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {
                try {
                    DataRP result = response.body();
                    assert result != null;
                    Utils.log("=== result " + result.getResponseCode() + " " + result.getResponseText() + " " + result.getOrderToken().getStatus() + " " + result.getOrderToken().getMessage());
                    if (result.getResponseCode() == 1) {
//                        Utils.showToast(RoomDetail.this,result.getResponseText()+" "+result.getOrderToken());
                        if (result.getOrderToken() != null && result.getOrderToken().getCftoken() != null && !result.getOrderToken().getCftoken().isEmpty()) {
                            openPaymentMethods(result.getOrderToken().getCftoken(), result.getBookingId(), HoursPrice);
                        } else {
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    } else {
                        method.suspend(result.getResponseText());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    Log.d("exception_error", e.toString());
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                Log.d("exception_error", "=== " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    public void payAdvanceAPI(String hotel_id) {
        Date date = Calendar.getInstance().getTime();
        String currentDate = format.format(date);
        progressBar.setVisibility(View.VISIBLE);

        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), method.userId());
        RequestBody hotelid = RequestBody.create(MediaType.parse("text/plain"), hotel_id);
        RequestBody bookingdate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
        RequestBody checkin_time = RequestBody.create(MediaType.parse("text/plain"), checkInDate + " " + checkInTime);
        RequestBody checkout_time = RequestBody.create(MediaType.parse("text/plain"), checkOutDate + " " + checkOutTime);
        RequestBody hour_price = RequestBody.create(MediaType.parse("text/plain"), HoursPrice);
        int noOfPerson = Integer.parseInt(tvBookRooms.getText().toString()) * 2;
        RequestBody no_of_person = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(noOfPerson));
        RequestBody total_room = RequestBody.create(MediaType.parse("text/plain"), tvBookRooms.getText().toString());
        Utils.log("=== FINAL_AMOUNT " + FINAL_AMOUNT + " " + noOfPerson);
        RequestBody final_amount = RequestBody.create(MediaType.parse("text/plain"), HoursPrice);
        RequestBody payment_type = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody payment_ref_no = RequestBody.create(MediaType.parse("text/plain"), "REF787867867868");
        RequestBody coupon_code = RequestBody.create(MediaType.parse("text/plain"), COUPON_CODE);
        MultipartBody.Part fileBody;
        if (kycImgUrl.isEmpty()) {
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            fileBody = MultipartBody.Part.createFormData(
                    "image", "",
                    fileRequestBody
            );
        } else {
            File file = new File(kycImgUrl);
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileBody = MultipartBody.Part.createFormData(
                    "image", file.getName(),
                    fileRequestBody
            );
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DataRP> call = apiService.bookHotel(customer_id, hotelid, bookingdate, checkin_time, checkout_time, hour_price,
                no_of_person, total_room, final_amount, payment_type, payment_ref_no, coupon_code, fileBody);
        call.enqueue(new Callback<DataRP>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {
                try {
                    DataRP result = response.body();
                    assert result != null;
                    Utils.log("=== result " + result.getResponseCode() + " " + result.getResponseText() + " " + result.getOrderToken().getStatus() + " " + result.getOrderToken().getMessage());
                    if (result.getResponseCode() == 1) {
//                        Utils.showToast(RoomDetail.this,result.getResponseText()+" "+result.getOrderToken());
                        if (result.getOrderToken() != null && result.getOrderToken().getCftoken() != null && !result.getOrderToken().getCftoken().isEmpty()) {
                            openPaymentMethods(result.getOrderToken().getCftoken(), result.getBookingId(), HoursPrice);
                        } else {
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    } else {
                        method.suspend(result.getResponseText());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    Log.d("exception_error", e.toString());
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                Log.d("exception_error", "=== " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    public void bookHotelWithCashAPI(String hotel_id) {
        Date date = Calendar.getInstance().getTime();
        String currentDate = format.format(date);
        progressBar.setVisibility(View.VISIBLE);

        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), method.userId());
        RequestBody hotelid = RequestBody.create(MediaType.parse("text/plain"), hotel_id);
        RequestBody bookingdate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
        RequestBody checkin_time = RequestBody.create(MediaType.parse("text/plain"), checkInDate + " " + checkInTime);
        RequestBody checkout_time = RequestBody.create(MediaType.parse("text/plain"), checkOutDate + " " + checkOutTime);
        RequestBody hour_price = RequestBody.create(MediaType.parse("text/plain"), HoursPrice);
        int noOfPerson = Integer.parseInt(tvBookRooms.getText().toString()) * 2;
        RequestBody no_of_person = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(noOfPerson));
        RequestBody total_room = RequestBody.create(MediaType.parse("text/plain"), tvBookRooms.getText().toString());
        RequestBody final_amount = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(FINAL_AMOUNT));
        RequestBody coupon_code = RequestBody.create(MediaType.parse("text/plain"), COUPON_CODE);
        MultipartBody.Part fileBody;
        if (kycImgUrl.isEmpty()) {
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            fileBody = MultipartBody.Part.createFormData(
                    "image", "",
                    fileRequestBody
            );
        } else {
            File file = new File(kycImgUrl);
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileBody = MultipartBody.Part.createFormData(
                    "image", file.getName(),
                    fileRequestBody
            );
        }
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DataRP> call = apiService.bookHotelWithCash(customer_id, hotelid, bookingdate, checkin_time, checkout_time, hour_price,
                no_of_person, total_room, final_amount, coupon_code, fileBody);
        call.enqueue(new Callback<DataRP>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {
                try {
                    DataRP result = response.body();
                    assert hotel != null;
//                    if (result.getResponseCode() == 1) {
////                        Utils.showToast(RoomDetail.this,result.getResponseText());
//                        if (result.getOrderToken() != null && result.getOrderToken().getCftoken() != null && !result.getOrderToken().getCftoken().isEmpty()) {
//                            openPaymentMethods(result.getOrderToken().getCftoken(), result.getBookingId(), HoursPrice);
//                        } else {
//                            method.alertBox(result.getResponseText());
//                        }
//
//                    } else {
//                        method.suspend(hotel.getResponseText());
//                    }

                    method.alertBox(result.getResponseText());
                } catch (Exception e) {
                    e.printStackTrace();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    Log.d("exception_error", e.toString());
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                Log.d("exception_error", "=== " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void openPaymentMethods(String cftoken, String orderId, String orderAmount) {
        Utils.log("=== cftoken " + cftoken);
        String stage = "PROD";
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
//        cfPaymentService.upiPayment(RoomDetail.this, getInputParams(orderId,orderAmount), cftoken, stage);
        cfPaymentService.doPayment(RoomDetail.this, getInputParams(orderId, orderAmount), cftoken, stage, "#784BD2", "#FFFFFF", false);
    }

    private Map<String, String> getInputParams(String orderId, String orderAmount) {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = getResources().getString(R.string.cashfree_app_id);
        String orderNote = "Order";
        String customerName = method.getUserName();
        String customerPhone = method.getUserPhone();
        String customerEmail = method.getUserEmail();

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void selectHours(String hours) {
        try {
            bookHours = hours;
            switch (bookHours) {
                case "3":
                    HoursPrice = hotel.getResponseData().getRoomPriceHourly().getHotel3hrsDiscountedCost();
                    FINAL_AMOUNT = Double.parseDouble(tvBookRooms.getText().toString()) * Double.parseDouble(HoursPrice);
                    lblSelectedHours.setText("Rs." + FINAL_AMOUNT);
                    COUPON_CODE = "";
                    etCoupon.setVisibility(View.VISIBLE);
                    btnCouponApply.setVisibility(View.VISIBLE);
                    tv3H.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                    tv3H.setTextColor(getResources().getColor(R.color.white));

                    tv6H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                    tv6H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                    tv12H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                    tv12H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                    try {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date d = df.parse(checkInDate + " " + checkInTime);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        cal.add(Calendar.HOUR, 3);

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(cal.getTime());
                        cal2.set(Calendar.HOUR_OF_DAY, 11);
                        cal2.set(Calendar.MINUTE, 00);
                        cal2.set(Calendar.SECOND, 00);
                        Utils.log("==== checkout date " + df.format(cal2.getTime()) + " " + Utils.compareDate(df.format(d), df.format(cal2.getTime())) + "" + Utils.isDateNextDay(df.format(cal.getTime()), df.format(cal2.getTime())));

                        checkOutTimeMillis = cal.getTimeInMillis();
                        String newTime = df.format(cal.getTime());
                        DAY_OF_MONTH2 = Integer.parseInt((String) DateFormat.format("dd", cal.getTime()));
                        MONTH2 = Integer.parseInt((String) DateFormat.format("MM", cal.getTime())) - 1;
                        YEAR2 = Integer.parseInt((String) DateFormat.format("yyyy", cal.getTime()));
                        MINUTE2 = Integer.parseInt((String) DateFormat.format("mm", cal.getTime()));
                        HOUR_OF_DAY2 = Integer.parseInt((String) DateFormat.format("HH", cal.getTime()));
                        checkOutDate = format.format(cal.getTime());
                        tvCheckOutDate.setText(checkOutDate);
                        checkOutTime = newTime.split(" ")[1];
                        tvCheckOutTime.setText(checkOutTime);
                        tvCheckOutDate.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                        tvCheckOutDate.setTextColor(getResources().getColor(R.color.white));
                        tvCheckOutTime.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                        tvCheckOutTime.setTextColor(getResources().getColor(R.color.white));

                        cardviewSelectedHours.setVisibility(View.VISIBLE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case "6":
                    HoursPrice = hotel.getResponseData().getRoomPriceHourly().getHotel6hrsDiscountedCost();
                    FINAL_AMOUNT = Double.parseDouble(tvBookRooms.getText().toString()) * Double.parseDouble(HoursPrice);
                    lblSelectedHours.setText("Rs." + FINAL_AMOUNT);
                    COUPON_CODE = "";
                    etCoupon.setVisibility(View.VISIBLE);
                    btnCouponApply.setVisibility(View.VISIBLE);
                    tv3H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                    tv3H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                    tv6H.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                    tv6H.setTextColor(getResources().getColor(R.color.white));

                    tv12H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                    tv12H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                    try {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date d = df.parse(checkInDate + " " + checkInTime);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        cal.add(Calendar.HOUR, 6);

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(cal.getTime());
                        cal2.set(Calendar.HOUR_OF_DAY, 11);
                        cal2.set(Calendar.MINUTE, 00);
                        cal2.set(Calendar.SECOND, 00);
                        Utils.log("==== checkout date " + df.format(cal2.getTime()) + " " + Utils.compareDate(df.format(d), df.format(cal2.getTime())) + "" + Utils.isDateNextDay(df.format(cal.getTime()), df.format(cal2.getTime())));

                        checkOutTimeMillis = cal.getTimeInMillis();
                        String newTime = df.format(cal.getTime());
                        DAY_OF_MONTH2 = Integer.parseInt((String) DateFormat.format("dd", cal.getTime()));
                        MONTH2 = Integer.parseInt((String) DateFormat.format("MM", cal.getTime())) - 1;
                        YEAR2 = Integer.parseInt((String) DateFormat.format("yyyy", cal.getTime()));
                        MINUTE2 = Integer.parseInt((String) DateFormat.format("mm", cal.getTime()));
                        HOUR_OF_DAY2 = Integer.parseInt((String) DateFormat.format("HH", cal.getTime()));
                        checkOutDate = format.format(cal.getTime());
                        tvCheckOutDate.setText(checkOutDate);
                        checkOutTime = newTime.split(" ")[1];
                        tvCheckOutTime.setText(checkOutTime);
                        tvCheckOutDate.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                        tvCheckOutDate.setTextColor(getResources().getColor(R.color.white));
                        tvCheckOutTime.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                        tvCheckOutTime.setTextColor(getResources().getColor(R.color.white));

                        cardviewSelectedHours.setVisibility(View.VISIBLE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case "12":
                    HoursPrice = hotel.getResponseData().getRoomPriceHourly().getHotel9hrsDiscountedCost();
                    FINAL_AMOUNT = Double.parseDouble(tvBookRooms.getText().toString()) * Double.parseDouble(HoursPrice);
                    lblSelectedHours.setText("Rs." + FINAL_AMOUNT);
                    COUPON_CODE = "";
                    etCoupon.setVisibility(View.VISIBLE);
                    btnCouponApply.setVisibility(View.VISIBLE);
                    tv3H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                    tv3H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                    tv6H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                    tv6H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                    tv12H.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                    tv12H.setTextColor(getResources().getColor(R.color.white));

                    try {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date d = df.parse(checkInDate + " " + checkInTime);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        cal.add(Calendar.HOUR, 24);

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(cal.getTime());
                        cal2.set(Calendar.HOUR_OF_DAY, 11);
                        cal2.set(Calendar.MINUTE, 00);
                        cal2.set(Calendar.SECOND, 00);
                        Utils.log("==== checkout date " + df.format(cal2.getTime()) + " " + Utils.compareDate(df.format(d), df.format(cal2.getTime())) + "" + Utils.isDateNextDay(df.format(cal.getTime()), df.format(cal2.getTime())));

                        if (!Utils.compareDate(df.format(d), df.format(cal2.getTime())) && Utils.isDateNextDay(df.format(cal.getTime()), df.format(cal2.getTime()))) {
                            cal = Calendar.getInstance();
                            cal.setTime(cal2.getTime());
                        }
                        checkOutTimeMillis = cal.getTimeInMillis();
                        String newTime = df.format(cal.getTime());
                        DAY_OF_MONTH2 = Integer.parseInt((String) DateFormat.format("dd", cal.getTime()));
                        MONTH2 = Integer.parseInt((String) DateFormat.format("MM", cal.getTime())) - 1;
                        YEAR2 = Integer.parseInt((String) DateFormat.format("yyyy", cal.getTime()));
                        MINUTE2 = Integer.parseInt((String) DateFormat.format("mm", cal.getTime()));
                        HOUR_OF_DAY2 = Integer.parseInt((String) DateFormat.format("HH", cal.getTime()));
                        checkOutDate = format.format(cal.getTime());
                        tvCheckOutDate.setText(checkOutDate);
                        checkOutTime = newTime.split(" ")[1];
                        tvCheckOutTime.setText(checkOutTime);
                        tvCheckOutDate.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                        tvCheckOutDate.setTextColor(getResources().getColor(R.color.white));
                        tvCheckOutTime.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                        tvCheckOutTime.setTextColor(getResources().getColor(R.color.white));

                        cardviewSelectedHours.setVisibility(View.VISIBLE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2404) {
                Uri uri = data.getData();
                FileUtils fileUtils = new FileUtils(RoomDetail.this);
                kycImgUrl = fileUtils.getPath(uri);
                File file = new File(kycImgUrl);
                tvUploadKYC.setText(file.getName());
            } else {

                //Prints all extras. Replace with app logic.
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null)
                        Log.d("TAG", "API Response : " + bundle.keySet().toString());
                    CashfreePayment cashfreePayment = new CashfreePayment();
                    for (String key : bundle.keySet()) {
                        if (bundle.getString(key) != null) {

                            String response = bundle.getString(key);
                            Utils.log("=== response " + response + " " + key);
                            switch (key) {
                                case "status":
                                    cashfreePayment.setStatus(response);
                                    break;
                                case "txStatus":
                                    cashfreePayment.setTxStatus(response);
                                    break;
                                case "orderId":
                                    cashfreePayment.setOrderId(response);
                                    break;
                                case "referenceId":
                                    cashfreePayment.setReferenceId(response);
                                    break;
                                case "orderAmount":
                                    cashfreePayment.setOrderAmount(response);
                                    break;
                                case "txMsg":
                                    cashfreePayment.setTxMsg(response);
                                    break;
                                case "paymentMode":
                                    cashfreePayment.setPaymentMode(response);
                                    break;
                                case "txTime":
                                    cashfreePayment.setTxTime(response);
                                    break;
                                case "signature":
                                    cashfreePayment.setSignature(response);
                                    break;
                            }

                        }
                    }

                    if (cashfreePayment.getStatus().equals("OK") && cashfreePayment.getTxStatus().equals("SUCCESS")) {
                        updateOrderAPI(cashfreePayment);
                    }
                }
            }

        } else {

        }
    }

    private void updateOrderAPI(CashfreePayment cashfreePayment) {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("customer_id", method.userId());
        requestBody.put("order_id", cashfreePayment.getOrderId());
        requestBody.put("ref_no", cashfreePayment.getReferenceId());
        requestBody.put("payment_mode", cashfreePayment.getPaymentMode());
        requestBody.put("payment_time", cashfreePayment.getTxTime());
        requestBody.put("payment_status", "1");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DataRP> call = apiService.updateOrder(requestBody);
        call.enqueue(new Callback<DataRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {

                try {

                    DataRP result = response.body();

                    method.alertBox(result.getResponseText());

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                progressBar.setVisibility(View.VISIBLE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission()) {
            ImagePicker.with(this)
                    .start();
        }
    }

}

