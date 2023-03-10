package com.app.effistay.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.effistay.response.RegisterRP;
import com.app.effistay.rest.ApiClient;
import com.app.effistay.rest.ApiInterface;
import com.app.effistay.util.Method;
import com.app.effistay.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private Method method;
    private InputMethodManager imm;
    private ProgressDialog progressDialog;
    private MaterialCheckBox checkBox;
    private TextInputEditText editTextName, editTextEmail, editTextPassword, editTextConformPassword, editTextPhoneNo;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        method = new Method(Register.this);
        method.forceRTLIfSupported();

        progressDialog = new ProgressDialog(Register.this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        editTextName = findViewById(R.id.editText_name_register);
        editTextEmail = findViewById(R.id.editText_email_register);
        editTextPassword = findViewById(R.id.editText_password_register);
        editTextConformPassword = findViewById(R.id.editText_conform_password_register);
        editTextPhoneNo = findViewById(R.id.editText_phoneNo_register);
        checkBox = findViewById(R.id.checkbox_register);
        MaterialTextView textViewLogin = findViewById(R.id.textView_login_register);
        MaterialTextView textViewTerms = findViewById(R.id.textView_terms_register);
        MaterialButton buttonSubmit = findViewById(R.id.button_submit);

        textViewTerms.setOnClickListener(v -> startActivity(new Intent(Register.this, TermsConditions.class)));

        textViewLogin.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finishAffinity();
        });

        buttonSubmit.setOnClickListener(v -> form());

    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void form() {

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String conformPassword = editTextConformPassword.getText().toString();
        String phoneNo = editTextPhoneNo.getText().toString();

        editTextName.setError(null);
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        editTextConformPassword.setError(null);
        editTextPhoneNo.setError(null);

        if (name.equals("") || name.isEmpty()) {
            editTextName.requestFocus();
            editTextName.setError(getResources().getString(R.string.please_enter_name));
        } else if (!isValidMail(email) || email.isEmpty()) {
            editTextEmail.requestFocus();
            editTextEmail.setError(getResources().getString(R.string.please_enter_email));
        } else if (password.equals("") || password.isEmpty()) {
            editTextPassword.requestFocus();
            editTextPassword.setError(getResources().getString(R.string.please_enter_password));
        } else if (conformPassword.equals("") || conformPassword.isEmpty()) {
            editTextConformPassword.requestFocus();
            editTextConformPassword.setError(getResources().getString(R.string.please_enter_confirm_password));
        } else if (phoneNo.equals("") || phoneNo.isEmpty()) {
            editTextPhoneNo.requestFocus();
            editTextPhoneNo.setError(getResources().getString(R.string.please_enter_phone));
        } else if (!password.equals(conformPassword)) {
            method.alertBox(getResources().getString(R.string.password_not_match));
        } else if (!checkBox.isChecked()) {
            method.alertBox(getResources().getString(R.string.please_select_terms));
        } else {

            editTextName.clearFocus();
            editTextEmail.clearFocus();
            editTextPassword.clearFocus();
            editTextConformPassword.clearFocus();
            editTextPhoneNo.clearFocus();
            imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editTextEmail.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editTextConformPassword.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editTextPhoneNo.getWindowToken(), 0);

            if (method.isNetworkAvailable()) {
                register(name, email, password, phoneNo);
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }

        }
    }

    @SuppressLint("HardwareIds")
    public void register(String sendName, String sendEmail, String sendPassword, String sendPhone) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

//        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(Register.this));
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", sendName);
        requestBody.put("email", sendEmail);
        requestBody.put("password", sendPassword);
        requestBody.put("phone", sendPhone);
        requestBody.put("address", "Please Enter Your Address Here");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterRP> call = apiService.getRegister(requestBody);
        call.enqueue(new Callback<RegisterRP>() {
            @Override
            public void onResponse(@NotNull Call<RegisterRP> call, @NotNull Response<RegisterRP> response) {
                int statusCode = response.code();
                try {

                    RegisterRP registerRP = response.body();

                    assert registerRP != null;

                    if (registerRP.getResponseCode()==1) {

//                        if (registerRP.getSuccess().equals("1")) {

                            editTextName.setText("");
                            editTextEmail.setText("");
                            editTextPassword.setText("");
                            editTextConformPassword.setText("");
                            editTextPhoneNo.setText("");

                            Toast.makeText(Register.this, registerRP.getResponseText(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(Register.this, Login.class));
                            finishAffinity();

                        } else {
                            method.alertBox(registerRP.getResponseText());
                        }

//                    } else {
//                        method.alertBox(registerRP.getMessage());
//                    }


                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.registration_success));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<RegisterRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.registration_success));
            }
        });

    }

}
