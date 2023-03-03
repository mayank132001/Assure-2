package com.app.effistay.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.viewpager.widget.ViewPager;

import com.app.effistay.rest.ApiInterface;
import com.app.effistay.R;
import com.app.effistay.util.API;
import com.app.effistay.util.Method;
import com.app.effistay.response.BookingRoomRP;
import com.app.effistay.response.ProfileRP;
import com.app.effistay.rest.ApiClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRoom extends AppCompatActivity {

    private Method method;
    private MaterialToolbar toolbar;
    private boolean isDate = false;
    private MaterialButton button;
    private InputMethodManager imm;
    private ProgressDialog progressDialog;
    private String[] numberAdults;
    private String[] numberChildren;
    private DatePickerDialog datePickerDialog;
    private MaterialTextView textViewArrivalDate, textViewDepartureDate;
    private AppCompatSpinner spinnerAdults, spinnerChildren;
    private TextInputEditText editTextName, editTextEmail, editTextPhoneNo;
    private int year, month, day, arrYear, arrMonth, arrDay;
    private String roomId, adults, children, arrivalDate, departureDate;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);

        method = new Method(BookRoom.this);
        method.forceRTLIfSupported();

        roomId = getIntent().getStringExtra("id");

        progressDialog = new ProgressDialog(BookRoom.this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        numberAdults = new String[]{getResources().getString(R.string.adults), "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        numberChildren = new String[]{getResources().getString(R.string.children), "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        toolbar = findViewById(R.id.toolbar_book_room);
        toolbar.setTitle(getResources().getString(R.string.pay_now));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextName = findViewById(R.id.editText_name_book_room);
        editTextEmail = findViewById(R.id.editText_email_book_room);
        editTextPhoneNo = findViewById(R.id.editText_phoneNo_book_room);
        spinnerAdults = findViewById(R.id.spinner_adults_book_room);
        spinnerChildren = findViewById(R.id.spinner_children_book_room);
        textViewArrivalDate = findViewById(R.id.textView_arrivalDate_booking);
        textViewDepartureDate = findViewById(R.id.textView_departureDate_booking);
        button = findViewById(R.id.button_book_room);

        LinearLayout linearLayout = findViewById(R.id.linearLayout_book_room);
        method.bannerAd(linearLayout);

        spinnerAdults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textView_book_room));
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textView_app_color));
                }
                adults = numberAdults[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textView_book_room));
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textView_app_color));
                }
                children = numberChildren[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        textViewArrivalDate.setOnClickListener(v -> {

            datePickerDialog = new DatePickerDialog(BookRoom.this, R.style.datePicker, (view, year, month, dayOfMonth) -> {

                arrYear = year;
                arrMonth = month;
                arrDay = dayOfMonth;

                isDate = true;

                arrivalDate = selectDate(dayOfMonth, month, year);
                textViewArrivalDate.setText(arrivalDate);
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        textViewDepartureDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(arrYear, arrMonth, arrDay);
            long startTime = calendar.getTimeInMillis();

            if (isDate) {
                datePickerDialog = new DatePickerDialog(BookRoom.this, R.style.DatePickerTheme, (view, year, month, dayOfMonth) -> {
                    departureDate = selectDate(dayOfMonth, month, year);
                    textViewDepartureDate.setText(departureDate);
                }, arrYear, arrMonth, arrDay + 1);
                datePickerDialog.getDatePicker().setMinDate(startTime - 1000);
                datePickerDialog.show();
            } else {
                method.alertBox(getResources().getString(R.string.please_select_first_arrivalDate));
            }
        });

        button.setOnClickListener(v -> submit());

        adultsSpinner();
        childrenSpinner();

        if (method.isNetworkAvailable()) {
            if (method.isLogin()) {
                profile(method.userId());
            }
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

    }

    public String selectDate(int day, int month, int year) {

        String monthYear;
        String dayMonth;

        if (month + 1 < 10) {
            monthYear = "0" + (month + 1);
        } else {
            monthYear = String.valueOf(month + 1);
        }
        if (day < 10) {
            dayMonth = "0" + day;
        } else {
            dayMonth = String.valueOf(day);
        }

        return dayMonth + "/" + monthYear + "/" + year;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void adultsSpinner() {

        List<String> adults = new ArrayList<String>(); // Spinner Drop down elements
        Collections.addAll(adults, numberAdults);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BookRoom.this, android.R.layout.simple_spinner_item, adults);  // Creating adapter for spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Drop down layout style - list view with radio button
        spinnerAdults.setAdapter(dataAdapter); // attaching data adapter to spinner
    }

    public void childrenSpinner() {

        List<String> children = new ArrayList<String>(); // Spinner Drop down elements
        Collections.addAll(children, numberChildren);
        ArrayAdapter<String> dataAdapterChildren = new ArrayAdapter<String>(BookRoom.this, android.R.layout.simple_spinner_item, children); // Creating adapter for spinner
        dataAdapterChildren.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Drop down layout style - list view with radio button
        spinnerChildren.setAdapter(dataAdapterChildren);// attaching data adapter to spinner
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void submit() {

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phoneNo = editTextPhoneNo.getText().toString();

        if (name.equals("") || name.isEmpty()) {
            editTextName.requestFocus();
            editTextName.setError(getResources().getString(R.string.please_enter_name));
        } else if (!isValidMail(email) || email.isEmpty()) {
            editTextEmail.requestFocus();
            editTextEmail.setError(getResources().getString(R.string.please_enter_email));
        } else if (phoneNo.equals("") || phoneNo.isEmpty()) {
            editTextPhoneNo.requestFocus();
            editTextPhoneNo.setError(getResources().getString(R.string.please_enter_name));
        } else if (adults.equals(getResources().getString(R.string.adults)) || adults.equals("")) {
            method.alertBox(getResources().getString(R.string.please_select_adults));
        } else if (children.equals(getResources().getString(R.string.children)) || children.equals("")) {
            method.alertBox(getResources().getString(R.string.please_select_children));
        } else if (arrivalDate == null || arrivalDate.equals("")) {
            method.alertBox(getResources().getString(R.string.please_select_arrivalDate));
        } else if (departureDate == null || departureDate.equals("")) {
            method.alertBox(getResources().getString(R.string.please_select_departureDate));
        } else if (adults.equals("0") && children.equals("0")) {
            method.alertBox(getResources().getString(R.string.please_select_adults_children));
        } else {

            editTextName.clearFocus();
            editTextEmail.clearFocus();
            editTextPhoneNo.clearFocus();
            imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editTextEmail.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editTextPhoneNo.getWindowToken(), 0);

            if (method.isNetworkAvailable()) {
                booking(name, email, phoneNo, roomId, adults, children, arrivalDate, departureDate);
            }

        }

    }

    public void profile(String userId) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("customer_id", userId);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileRP> call = apiService.getProfile(requestBody);
        call.enqueue(new Callback<ProfileRP>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<ProfileRP> call, @NotNull Response<ProfileRP> response) {
                int statusCode = response.code();

                try {

                    ProfileRP profileRP = response.body();

                    assert profileRP != null;
                    if (profileRP.getResponseCode()==1) {

//                        if (profileRP.getSuccess().equals("1")) {

                            editTextEmail.setText(profileRP.getResponseData().getEmail());
                            editTextName.setText(profileRP.getResponseData().getName());
                            editTextPhoneNo.setText(profileRP.getResponseData().getPhone());
//                        }

                    } else if (profileRP.getResponseCode()==2) {
                        method.suspend(profileRP.getResponseText());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<ProfileRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }


    private void booking(String name, String email, String phoneNo, String roomId, String adults, String children, String arrivalDate, String departureDate) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(BookRoom.this));
        jsObj.addProperty("name", name);
        jsObj.addProperty("email", email);
        jsObj.addProperty("phone", phoneNo);
        jsObj.addProperty("room_id", roomId);
        jsObj.addProperty("adults", adults);
        jsObj.addProperty("children", children);
        jsObj.addProperty("check_in_date", arrivalDate);
        jsObj.addProperty("check_out_date", departureDate);
        jsObj.addProperty("method_name", "get_room_booking");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BookingRoomRP> call = apiService.roomBooking(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<BookingRoomRP>() {
            @Override
            public void onResponse(@NotNull Call<BookingRoomRP> call, @NotNull Response<BookingRoomRP> response) {
                int statusCode = response.code();
                try {

                    BookingRoomRP bookingRoomRP = response.body();

                    assert bookingRoomRP != null;

                    if (bookingRoomRP.getStatus().equals("1")) {

                        if (bookingRoomRP.getSuccess().equals("1")) {
                            bookingDialog(bookingRoomRP.getMsg());
                        } else {
                            method.alertBox(bookingRoomRP.getMsg());
                        }

                    } else {
                        method.alertBox(bookingRoomRP.getMessage());
                    }


                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<BookingRoomRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void bookingDialog(String message) {

        Dialog dialog = new Dialog(BookRoom.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking);
        dialog.setCancelable(false);
        if (method.isRtl()) {
            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);

        MaterialTextView textView_message = dialog.findViewById(R.id.textView_message_dialog_booking);
        MaterialButton button = dialog.findViewById(R.id.button_dialog_booking);

        textView_message.setText(message);

        button.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(BookRoom.this, MainActivity.class));
            finishAffinity();
        });

        dialog.show();

    }

}