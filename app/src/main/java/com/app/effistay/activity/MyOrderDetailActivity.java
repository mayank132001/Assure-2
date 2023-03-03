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
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.DatePicker;
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

import org.jetbrains.annotations.NotNull;

import java.io.File;
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

public class MyOrderDetailActivity extends AppCompatActivity {

    private Method method;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private WebView webViewDes, webViewRules;
//    private ImageView imageViewRating;
    private RatingView ratingView;
    private RecyclerView recyclerViewRa;
    private EnchantedViewPager viewPager;
    private MaterialCardView cardViewBookNow, cardViewPayAtHotel;
    private ConstraintLayout conMain, conNoData, conRating;
    private SliderRoomDetailAdapter sliderRoomDetailAdapter;
    private MaterialTextView textViewRoomName, textViewTotalRate;

    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    final Handler handler = new Handler();
    private Runnable Update;
    private InputMethodManager imm;
    TextView tvCheckINDate, tvCheckINTime, tvCheckOutDate, tvCheckOutTime, tv3H, tv6H, tv12H, tvBookRooms, tvUploadKYC,lblCheckIN,lblCheckOut;
    AppCompatImageView tvRoomPlus, tvRoomMin;
    private String selHours = "";
    private String bookHours = "";
    private String HoursPrice = "";
    private String checkInDate = "";
    private String checkInTime = "";
    private String checkOutDate = "";
    private String checkOutTime = "";
    private String checkOutDetails = "";
    private String kycImgUrl = "";
    HotelDetail hotel;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    int MAX_ROOMS = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        Intent intent = getIntent();
        String hotel_id = intent.getStringExtra("hotel_id");
        String title = intent.getStringExtra("title");
        int position = intent.getIntExtra("position", 0);
        checkInDate = getIntent().getStringExtra("checkInDate");
        checkInTime = getIntent().getStringExtra("checkInTime");
        selHours = getIntent().getStringExtra("selHours");
        checkOutDetails = getIntent().getStringExtra("checkOutDetails");
        method = new Method(MyOrderDetailActivity.this);
        method.forceRTLIfSupported();

        progressDialog = new ProgressDialog(MyOrderDetailActivity.this);
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
//        imageViewRating = findViewById(R.id.imageView_rating_rd);
        lblCheckIN = findViewById(R.id.lblCheckIN);
        lblCheckIN.setVisibility(View.VISIBLE);
        lblCheckOut = findViewById(R.id.lblCheckOut);
        lblCheckOut.setVisibility(View.VISIBLE);
        tv3H = findViewById(R.id.tv3H);
        tv3H.setVisibility(View.GONE);
        tv6H = findViewById(R.id.tv6H);
        tv6H.setVisibility(View.GONE);
        tv12H = findViewById(R.id.tv12H);
        tv12H.setVisibility(View.GONE);
        tvCheckINDate = findViewById(R.id.tvCheckINDate);
        tvCheckINTime = findViewById(R.id.tvCheckINTime);
        tvCheckOutDate = findViewById(R.id.tvCheckOutDate);
        tvCheckOutTime = findViewById(R.id.tvCheckOutTime);
        tvRoomPlus = findViewById(R.id.tvRoomPlus);
        tvBookRooms = findViewById(R.id.tvBookRooms);
        tvRoomMin = findViewById(R.id.tvRoomMin);
        tvUploadKYC = findViewById(R.id.tvUploadKYC);
        try {
            String[] array = checkOutDetails.split(" ");
            String checkInDate = array[0];
            String checkInTime = array[1];
            tvCheckOutDate.setText(checkInDate);
            tvCheckOutTime.setText(checkInTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        switch (selHours){
            case "3":
                tv3H.setVisibility(View.VISIBLE);
                break;
            case "6":
                tv6H.setVisibility(View.VISIBLE);
                break;
            case "12":
                tv12H.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        tvCheckINDate.setText(checkInDate);
        tvCheckINTime.setText(checkInTime);
        tvRoomPlus.setVisibility(View.GONE);
        tvRoomMin.setVisibility(View.GONE);
        tvUploadKYC.setVisibility(View.GONE);
        cardViewBookNow.setVisibility(View.GONE);
        cardViewPayAtHotel.setVisibility(View.GONE);
        textViewTotalRate.setTypeface(null);

        conMain.setVisibility(View.GONE);
        conNoData.setVisibility(View.GONE);

        recyclerViewRa.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_ra = new LinearLayoutManager(MyOrderDetailActivity.this);
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

    }

    public void getHotelDetail(String hotel_id) {

        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("customer_id", method.userId());
        requestBody.put("hotel_id", hotel_id);
        requestBody.put("checkin_date", checkInDate);
        requestBody.put("checkin_time", checkInTime);
        requestBody.put("hours", selHours);
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

                            sliderRoomDetailAdapter = new SliderRoomDetailAdapter(MyOrderDetailActivity.this, "room_detail_slider", hotel.getResponseData().getImages());
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
                            RoomAmenities roomAmenities = new RoomAmenities(MyOrderDetailActivity.this, "room_amenities", hotel.getResponseData().getFacilies());
                            recyclerViewRa.setAdapter(roomAmenities);
                        }

                        tv3H.setText("3 hours\nRs." + hotel.getResponseData().getRoomPriceHourly().getHotel3hrsDiscountedCost());
                        tv6H.setText("6 hours\nRs." + hotel.getResponseData().getRoomPriceHourly().getHotel6hrsDiscountedCost());
                        tv12H.setText("12 hours\nRs." + hotel.getResponseData().getRoomPriceHourly().getHotel9hrsDiscountedCost());
                        MAX_ROOMS = Integer.parseInt(hotel.getResponseData().getTotalRoom());

//                        cardViewBookNow.setOnClickListener(v -> {
//                            if (bookHours.isEmpty()) {
//                                Utils.showToast(MyOrderDetailActivity.this, "Please select hours");
//                            } else if (checkOutDate.isEmpty()) {
//                                Utils.showToast(MyOrderDetailActivity.this, "Please select check out date");
//                            } else if (checkOutTime.isEmpty()) {
//                                Utils.showToast(MyOrderDetailActivity.this, "Please select check out time");
//                            } else if (kycImgUrl.isEmpty()) {
//                                Utils.showToast(MyOrderDetailActivity.this, "Please upload kyc image");
//                            } else {
//                                bookHotelAPI(hotel_id);
//                            }
//
//                        });

//                        cardViewPayAtHotel.setOnClickListener(v -> {
//                            if (bookHours.isEmpty()) {
//                                Utils.showToast(MyOrderDetailActivity.this, "Please select hours");
//                            } else if (checkOutDate.isEmpty()) {
//                                Utils.showToast(MyOrderDetailActivity.this, "Please select check out date");
//                            } else if (checkOutTime.isEmpty()) {
//                                Utils.showToast(MyOrderDetailActivity.this, "Please select check out time");
//                            } else if (kycImgUrl.isEmpty()) {
//                                Utils.showToast(MyOrderDetailActivity.this, "Please upload kyc image");
//                            } else {
//                                bookHotelWithCashAPI(hotel_id);
//                            }
//
//                        });
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
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

//    public void bookHotelAPI(String hotel_id) {
//        Date date = Calendar.getInstance().getTime();
//        String currentDate = format.format(date);
//        progressBar.setVisibility(View.VISIBLE);
//
//        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), method.userId());
//        RequestBody hotelid = RequestBody.create(MediaType.parse("text/plain"), hotel_id);
//        RequestBody bookingdate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
//        RequestBody checkin_time = RequestBody.create(MediaType.parse("text/plain"), checkInDate + " " + checkInTime);
//        RequestBody checkout_time = RequestBody.create(MediaType.parse("text/plain"), checkOutDate + " " + checkOutTime);
//        RequestBody hour_price = RequestBody.create(MediaType.parse("text/plain"), HoursPrice);
//        RequestBody no_of_person = RequestBody.create(MediaType.parse("text/plain"), "1");
//        RequestBody total_room = RequestBody.create(MediaType.parse("text/plain"), tvBookRooms.getText().toString());
//        RequestBody final_amount = RequestBody.create(MediaType.parse("text/plain"), HoursPrice);
//        RequestBody payment_type = RequestBody.create(MediaType.parse("text/plain"), "1");
//        RequestBody payment_ref_no = RequestBody.create(MediaType.parse("text/plain"), "REF787867867867");
//        File file = new File(kycImgUrl);
//        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part fileBody = MultipartBody.Part.createFormData(
//                "image", file.getName(),
//                fileRequestBody
//        );
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<DataRP> call = apiService.bookHotel(customer_id, hotelid, bookingdate, checkin_time, checkout_time, hour_price,
//                no_of_person, total_room, final_amount, payment_type, payment_ref_no, fileBody);
//        call.enqueue(new Callback<DataRP>() {
//            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
//            @Override
//            public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {
//                try {
//                    DataRP result = response.body();
//                    assert result != null;
//                    Utils.log("=== result "+result.getResponseCode()+" "+result.getResponseText()+" "+result.getOrderToken().getStatus()+" "+result.getOrderToken().getMessage());
//                    if (result.getResponseCode() == 1) {
////                        Utils.showToast(RoomDetail.this,result.getResponseText()+" "+result.getOrderToken());
//                        if (result.getOrderToken() != null && result.getOrderToken().getCftoken() != null && !result.getOrderToken().getCftoken().isEmpty()) {
//                            openPaymentMethods(result.getOrderToken().getCftoken(), result.getBookingId(), HoursPrice);
//                        } else {
//                            method.alertBox(getResources().getString(R.string.failed_try_again));
//                        }
//
//                    } else {
//                        method.suspend(result.getResponseText());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    method.alertBox(getResources().getString(R.string.failed_try_again));
//                    Log.d("exception_error", e.toString());
//                }
//
//
//                progressBar.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
//                Log.d("exception_error", "=== " + t.getMessage());
//                progressBar.setVisibility(View.GONE);
//                method.alertBox(getResources().getString(R.string.failed_try_again));
//            }
//        });
//
//    }


//    public void bookHotelWithCashAPI(String hotel_id) {
//        Date date = Calendar.getInstance().getTime();
//        String currentDate = format.format(date);
//        progressBar.setVisibility(View.VISIBLE);
//
//        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), method.userId());
//        RequestBody hotelid = RequestBody.create(MediaType.parse("text/plain"), hotel_id);
//        RequestBody bookingdate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
//        RequestBody checkin_time = RequestBody.create(MediaType.parse("text/plain"), checkInDate + " " + checkInTime);
//        RequestBody checkout_time = RequestBody.create(MediaType.parse("text/plain"), checkOutDate + " " + checkOutTime);
//        RequestBody hour_price = RequestBody.create(MediaType.parse("text/plain"), HoursPrice);
//        RequestBody no_of_person = RequestBody.create(MediaType.parse("text/plain"), "1");
//        RequestBody total_room = RequestBody.create(MediaType.parse("text/plain"), tvBookRooms.getText().toString());
//        RequestBody final_amount = RequestBody.create(MediaType.parse("text/plain"), HoursPrice);
//        File file = new File(kycImgUrl);
//        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part fileBody = MultipartBody.Part.createFormData(
//                "image", file.getName(),
//                fileRequestBody
//        );
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<DataRP> call = apiService.bookHotelWithCash(customer_id, hotelid, bookingdate, checkin_time, checkout_time, hour_price,
//                no_of_person, total_room, final_amount, fileBody);
//        call.enqueue(new Callback<DataRP>() {
//            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
//            @Override
//            public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {
//                try {
//                    DataRP result = response.body();
//                    assert hotel != null;
////                    if (result.getResponseCode() == 1) {
//////                        Utils.showToast(RoomDetail.this,result.getResponseText());
////                        if (result.getOrderToken() != null && result.getOrderToken().getCftoken() != null && !result.getOrderToken().getCftoken().isEmpty()) {
////                            openPaymentMethods(result.getOrderToken().getCftoken(), result.getBookingId(), HoursPrice);
////                        } else {
////                            method.alertBox(result.getResponseText());
////                        }
////
////                    } else {
////                        method.suspend(hotel.getResponseText());
////                    }
//
//                    method.alertBox(result.getResponseText());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    method.alertBox(getResources().getString(R.string.failed_try_again));
//                    Log.d("exception_error", e.toString());
//                }
//
//
//                progressBar.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
//                Log.d("exception_error", "=== " + t.getMessage());
//                progressBar.setVisibility(View.GONE);
//                method.alertBox(getResources().getString(R.string.failed_try_again));
//            }
//        });
//
//    }

    private void openPaymentMethods(String cftoken, String orderId, String orderAmount) {
        String stage = "LIVE";
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
//        cfPaymentService.upiPayment(RoomDetail.this, getInputParams(orderId,orderAmount), cftoken, stage);
        cfPaymentService.doPayment(MyOrderDetailActivity.this, getInputParams(orderId, orderAmount), cftoken, stage, "#784BD2", "#FFFFFF", false);
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
        bookHours = hours;
        switch (bookHours) {
            case "3":
                HoursPrice = hotel.getResponseData().getRoomPriceHourly().getHotel3hrsDiscountedCost();
                tv3H.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                tv3H.setTextColor(getResources().getColor(R.color.white));

                tv6H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv6H.setTextColor(getResources().getColor(R.color.colorPrimary));

                tv12H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv12H.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

            case "6":
                HoursPrice = hotel.getResponseData().getRoomPriceHourly().getHotel6hrsDiscountedCost();
                tv3H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv3H.setTextColor(getResources().getColor(R.color.colorPrimary));

                tv6H.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                tv6H.setTextColor(getResources().getColor(R.color.white));

                tv12H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv12H.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

            case "12":
                HoursPrice = hotel.getResponseData().getRoomPriceHourly().getHotel9hrsDiscountedCost();
                tv3H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv3H.setTextColor(getResources().getColor(R.color.colorPrimary));

                tv6H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv6H.setTextColor(getResources().getColor(R.color.colorPrimary));

                tv12H.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                tv12H.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2404) {
                Uri uri = data.getData();
                FileUtils fileUtils = new FileUtils(MyOrderDetailActivity.this);
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
                            Utils.log("=== response "+response+" "+key);
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

