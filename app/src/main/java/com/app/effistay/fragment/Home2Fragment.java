package com.app.effistay.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.effistay.activity.HotelListActivity;
import com.app.effistay.response.HomeRP;
import com.app.effistay.rest.ApiInterface;
import com.app.effistay.R;
import com.app.effistay.activity.AboutUs;
import com.app.effistay.activity.MainActivity;
import com.app.effistay.activity.RoomDetail;
import com.app.effistay.activity.SearchCityActivity;
import com.app.effistay.adapter.SliderAdapter;
import com.app.effistay.interfaces.OnClick;
import com.app.effistay.rest.ApiClient;
import com.app.effistay.util.API;
import com.app.effistay.util.EnchantedViewPager;
import com.app.effistay.util.Method;
import com.app.effistay.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home2Fragment extends Fragment {

    private Method method;
    private OnClick onClick;
    private Animation myAnim;
    private ProgressBar progressBar;
    private ConstraintLayout conMain, conNoData;

    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    final Handler handler = new Handler();
    private Runnable Update;
    EditText etSearch;
    TextView tvWhen,tvWhere,tv3H,tv6H,tv12H;
    TextView btnSearch;
    CardView cardsearch;
    private EnchantedViewPager viewPager;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SliderAdapter sliderAdapter;

    private int CITY_SELECT_CODE = 501;
    private String selectCityName = "";
    private String selectCityID = "";
    private String checkInDate = "";
    private String checkInTime = "";
    private String selHours = "";

    int DAY_OF_MONTH = 0;
    int MONTH = 0;
    int YEAR = 0;
    int HOUR_OF_DAY = 0;
    int MINUTE = 0;
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.home_fragment_2, container, false);
        final Calendar newCalendar = Calendar.getInstance();
        YEAR = newCalendar.get(Calendar.YEAR);
        MONTH = newCalendar.get(Calendar.MONTH);
        DAY_OF_MONTH = newCalendar.get(Calendar.DAY_OF_MONTH);

        HOUR_OF_DAY = newCalendar.get(Calendar.HOUR_OF_DAY);
        MINUTE = newCalendar.get(Calendar.MINUTE);

        if (MainActivity.toolbar != null) {
            MainActivity.toolbar.setTitle(getResources().getString(R.string.home));
        }
        cardsearch = view.findViewById(R.id.cardsearch);
        viewPager = view.findViewById(R.id.viewPager_home);
        onClick = (position, type, id, title) -> {
            switch (type) {
                case "room":
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_main, new RoomFragment(),
                            getResources().getString(R.string.room)).addToBackStack(getResources().getString(R.string.room))
                            .commitAllowingStateLoss();
                    break;
                case "location":
//                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_main, new LocationFragment(),
//                            getResources().getString(R.string.location)).addToBackStack(getResources().getString(R.string.location))
//                            .commitAllowingStateLoss();
                    break;
                case "facility":
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_main, new FacilitiesFragment(),
                            getResources().getString(R.string.facilities)).addToBackStack(getResources().getString(R.string.facilities))
                            .commitAllowingStateLoss();
                    break;
                case "gallery":
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_main, new GalleryFragment(),
                            getResources().getString(R.string.gallery)).addToBackStack(getResources().getString(R.string.gallery))
                            .commitAllowingStateLoss();
                    break;
                case "contactUS":
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_main, new ContactUsFragment(),
                            getResources().getString(R.string.contact_us)).addToBackStack(getResources().getString(R.string.contact_us))
                            .commitAllowingStateLoss();
                    break;
                case "aboutUs":
                    startActivity(new Intent(getActivity(), AboutUs.class));
                    break;
            }

        };
//        method = new Method(getActivity(), onClick);
//        myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        conMain = view.findViewById(R.id.con_home);
        conNoData = view.findViewById(R.id.con_noDataFound);
        progressBar = view.findViewById(R.id.progressbar_home);

        etSearch = view.findViewById(R.id.etSearch);
        tvWhen = view.findViewById(R.id.tvWhen);
        tvWhere = view.findViewById(R.id.tvWhere);
        tv3H = view.findViewById(R.id.tv3H);
        tv6H = view.findViewById(R.id.tv6H);
        tv12H = view.findViewById(R.id.tv12H);
        btnSearch = view.findViewById(R.id.btnSearch);
        onClick = (position, type, id, title) -> startActivity(new Intent(getActivity(), RoomDetail.class)
                .putExtra("DAY_OF_MONTH", DAY_OF_MONTH)
                .putExtra("MONTH", MONTH)
                .putExtra("YEAR", YEAR)
                .putExtra("HOUR_OF_DAY", HOUR_OF_DAY)
                .putExtra("MINUTE", MINUTE)
                .putExtra("room_id", id)
                .putExtra("title", title)
                .putExtra("position", position));
        method = new Method(getActivity(), onClick);



        tvWhen.setOnClickListener(v -> {
            openDatePickerDialog();
        });

        tvWhere.setOnClickListener(v -> {
            openTimePickerDialog();
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
        btnSearch.setOnClickListener(v -> {
            if (selectCityID.isEmpty()){
                Utils.showToast(getActivity(),"Please select city");
            }else if (checkInDate.isEmpty()){
                Utils.showToast(getActivity(),"Please select check in date");
            }else if (checkInTime.isEmpty()){
                Utils.showToast(getActivity(),"Please select check in time");
            }else if (selHours.isEmpty()){
                Utils.showToast(getActivity(),"Please select hours");
            }else {
                startActivity(new Intent(getActivity(), HotelListActivity.class)
                        .putExtra("DAY_OF_MONTH", DAY_OF_MONTH)
                        .putExtra("MONTH", MONTH)
                        .putExtra("YEAR", YEAR)
                        .putExtra("HOUR_OF_DAY", HOUR_OF_DAY)
                        .putExtra("MINUTE", MINUTE)
                        .putExtra("selectCityID",selectCityID)
                        .putExtra("checkInDate",checkInDate)
                        .putExtra("checkInTime",checkInTime)
                        .putExtra("selHours",selHours));
            }
        });

        cardsearch.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), SearchCityActivity.class),CITY_SELECT_CODE);
        });

        etSearch.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), SearchCityActivity.class),CITY_SELECT_CODE);
        });

//        conMain.setVisibility(View.GONE);
//        conNoData.setVisibility(View.GONE);
//        progressBar.setVisibility(View.GONE);

//        if (method.isNetworkAvailable()) {
//            home();
//        } else {
//            method.alertBox(getResources().getString(R.string.internet_connection));
//        }
//        home();
        return view;

    }

    private void selectHours(String hours) {
        selHours = hours;
        switch (selHours){
            case "3":
                tv3H.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                tv3H.setTextColor(getResources().getColor(R.color.white));

                tv6H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv6H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                tv12H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv12H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));
                break;

            case "6":
                tv3H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv3H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                tv6H.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                tv6H.setTextColor(getResources().getColor(R.color.white));

                tv12H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv12H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));
                break;

            case "12":
                tv3H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv3H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                tv6H.setBackground(getResources().getDrawable(R.drawable.bg_when_where));
                tv6H.setTextColor(getResources().getColor(R.color.txt_colorPrimary));

                tv12H.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                tv12H.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void openTimePickerDialog() {
//        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                HOUR_OF_DAY = selectedHour;
//                MINUTE = selectedMinute;
//                tvWhere.setText( selectedHour + ":" + selectedMinute);
//                tvWhere.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
//                tvWhere.setTextColor(getResources().getColor(R.color.white));
//                checkInTime = selectedHour + ":" + selectedMinute;
//            }
//        }, HOUR_OF_DAY, MINUTE, true);//Yes 24 hour time
//        mTimePicker.setTitle("Select Time");
//        mTimePicker.show();


        int  mHour, mMinute;
        final Calendar c = Calendar.getInstance();

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        HOUR_OF_DAY = hourOfDay;
                        MINUTE = minute;
                        tvWhere.setText( hourOfDay + ":" + minute);
                        tvWhere.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                        tvWhere.setTextColor(getResources().getColor(R.color.white));
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

        if (Utils.checkDates(checkInDate)){
            tpd.setMinTime(mHour, mMinute, 0); // MIN: hours, minute, secconds
        }
        tpd.show(getFragmentManager(), "TimePickerDialog");
    }

    private void openDatePickerDialog() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DatePickerTheme,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                YEAR = year;
                MONTH = monthOfYear;
                DAY_OF_MONTH =  dayOfMonth;
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvWhen.setText(format.format(newDate.getTime()));
                tvWhen.setBackground(getResources().getDrawable(R.drawable.bg_when_where_selected));
                tvWhen.setTextColor(getResources().getColor(R.color.white));
                checkInDate = format.format(newDate.getTime());
            }

        }, YEAR,MONTH,DAY_OF_MONTH);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void home() {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("method_name", "get_home");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<HomeRP> call = apiService.getHome(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<HomeRP>() {
                @Override
                public void onResponse(@NotNull Call<HomeRP> call, @NotNull Response<HomeRP> response) {
                    int statusCode = response.code();

                    if (getActivity() != null) {

                        try {

                            HomeRP homeRP = response.body();
                            assert homeRP != null;

                            if (homeRP.getStatus().equals("1")) {

//                                textViewTitle.setText(homeRP.getHotel_name());
//                                textViewAdd.setText(homeRP.getHotel_address());
//                                textViewEmail.setText(homeRP.getHotel_email());
//                                textViewPhone.setText(homeRP.getHotel_phone());

                                if (homeRP.getHomeBanners().size() != 0) {

                                    int columnWidth = method.getScreenWidth();
                                    viewPager.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, columnWidth / 2 + 80));

                                    viewPager.useScale();
                                    viewPager.removeAlpha();

                                    sliderAdapter = new SliderAdapter(getActivity(), "slider", homeRP.getHomeBanners(), onClick);
                                    viewPager.setAdapter(sliderAdapter);

                                    Update = () -> {
                                        if (viewPager.getCurrentItem() == (sliderAdapter.getCount() - 1)) {
                                            viewPager.setCurrentItem(0, true);
                                        } else {
                                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                        }
                                    };

                                    if (sliderAdapter.getCount() > 1) {
                                        timer = new Timer(); // This will create a new Thread
                                        timer.schedule(new TimerTask() { // task to be scheduled
                                            @Override
                                            public void run() {
                                                handler.post(Update);
                                            }
                                        }, DELAY_MS, PERIOD_MS);
                                    }

                                }

                                conMain.setVisibility(View.VISIBLE);

//                                imageViewFacebook.setOnClickListener(v -> {
//                                    imageViewFacebook.startAnimation(myAnim);
//                                    String string = homeRP.getFacebook_url();
//                                    if (string.equals("")) {
//                                        method.alertBox(getResources().getString(R.string.user_not_facebook_link));
//                                    } else {
//                                        try {
//                                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                                            intent.setData(Uri.parse(string));
//                                            startActivity(intent);
//                                        } catch (Exception e) {
//                                            method.alertBox(getResources().getString(R.string.wrong));
//                                        }
//                                    }
//                                });
//
//                                imageViewWhatsApp.setOnClickListener(v -> {
//                                    imageViewWhatsApp.startAnimation(myAnim);
//                                    String string = homeRP.getWhatsapp_url();
//                                    if (string.equals("")) {
//                                        method.alertBox(getResources().getString(R.string.user_not_whatsApp_link));
//                                    } else {
//                                        try {
//                                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                                            intent.setData(Uri.parse(string));
//                                            startActivity(intent);
//                                        } catch (Exception e) {
//                                            method.alertBox(getResources().getString(R.string.wrong));
//                                        }
//                                    }
//                                });
//
//                                imageViewTwitter.setOnClickListener(v -> {
//                                    imageViewTwitter.startAnimation(myAnim);
//                                    String string = homeRP.getTwitter_url();
//                                    if (string.equals("")) {
//                                        method.alertBox(getResources().getString(R.string.user_not_twitter_link));
//                                    } else {
//                                        try {
//                                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                                            intent.setData(Uri.parse(string));
//                                            startActivity(intent);
//                                        } catch (Exception e) {
//                                            method.alertBox(getResources().getString(R.string.wrong));
//                                        }
//                                    }
//                                });
//
//                                imageViewInstagram.setOnClickListener(v -> {
//                                    imageViewInstagram.startAnimation(myAnim);
//                                    String string = homeRP.getInstagram_url();
//                                    if (string.equals("")) {
//                                        method.alertBox(getResources().getString(R.string.user_not_instagram_link));
//                                    } else {
//                                        try {
//                                            Uri uri = Uri.parse(string);
//                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                            intent.setPackage("com.instagram.android");
//                                            startActivity(intent);
//                                        } catch (ActivityNotFoundException e) {
//                                            try {
//                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(string)));
//                                            } catch (Exception e1) {
//                                                method.alertBox(getResources().getString(R.string.wrong));
//                                            }
//                                        }
//                                    }
//                                });
//
//                                imageViewYouTube.setOnClickListener(v -> {
//                                    imageViewYouTube.startAnimation(myAnim);
//                                    String string = homeRP.getYoutube_url();
//                                    if (string.equals("")) {
//                                        method.alertBox(getResources().getString(R.string.user_not_youtube_link));
//                                    } else {
//                                        try {
//                                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                                            intent.setData(Uri.parse(string));
//                                            startActivity(intent);
//                                        } catch (Exception e) {
//                                            method.alertBox(getResources().getString(R.string.wrong));
//                                        }
//                                    }
//                                });
//
//                                imageViewWeb.setOnClickListener(v -> {
//                                    imageViewWeb.startAnimation(myAnim);
//                                    String string = homeRP.getWebsite_url();
//                                    if (string.equals("")) {
//                                        method.alertBox(getResources().getString(R.string.user_not_web_link));
//                                    } else {
//                                        try {
//                                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                                            intent.setData(Uri.parse(string));
//                                            startActivity(intent);
//                                        } catch (Exception e) {
//                                            method.alertBox(getResources().getString(R.string.wrong));
//                                        }
//                                    }
//                                });


                            } else {
                                conNoData.setVisibility(View.VISIBLE);
                                method.alertBox(homeRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<HomeRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    conNoData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CITY_SELECT_CODE && data!=null){
                selectCityID = data.getStringExtra("selectCityID");
                selectCityName = data.getStringExtra("selectCityName");
                etSearch.setText(selectCityName);
            }
        }
    }
}
