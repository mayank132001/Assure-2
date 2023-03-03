package com.app.effistay.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.app.effistay.activity.MainActivity;
import com.app.effistay.response.DataRP;
import com.app.effistay.response.ProfileRP;
import com.app.effistay.rest.ApiClient;
import com.app.effistay.rest.ApiInterface;
import com.app.effistay.util.Events;
import com.app.effistay.util.GlobalBus;
import com.app.effistay.util.Method;
import com.bumptech.glide.Glide;
import com.app.effistay.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private Method method;
    private String imageProfile;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private CircleImageView imageViewUser;
    private ImageView imageViewEdit;
    private InputMethodManager imm;
    private MaterialButton buttonSubmit;
    private MaterialTextView textViewName;
    private TextInputLayout textInputEmail;
    private ConstraintLayout conMain, conNoData;
    private boolean isProfile = false, isRemove = false;
    private TextInputEditText editTextName, editTextEmail, editTextPhoneNo;

    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile_fragment, container, false);

        if (MainActivity.toolbar != null) {
            MainActivity.toolbar.setTitle(getResources().getString(R.string.edit_profile));
        }

        GlobalBus.getBus().register(this);

        method = new Method(getActivity());

        progressDialog = new ProgressDialog(getActivity());

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        conMain = view.findViewById(R.id.con_main_editPro);
        conNoData = view.findViewById(R.id.con_noDataFound);
        progressBar = view.findViewById(R.id.progressbar_edit_profile);
        imageViewUser = view.findViewById(R.id.imageView_user_edit_profile);
        imageViewEdit = view.findViewById(R.id.imageView_editPro);
        textViewName = view.findViewById(R.id.textView_name_editPro);
        editTextName = view.findViewById(R.id.editText_name_edit_profile);
        editTextEmail = view.findViewById(R.id.editText_email_edit_profile);
        editTextPhoneNo = view.findViewById(R.id.editText_phone_edit_profile);
        textInputEmail = view.findViewById(R.id.textInput_email_edit_profile);
        buttonSubmit = view.findViewById(R.id.button_edit_profile);

        if (method.isDarkMode()) {
            imageViewEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_profile));
        } else {
            imageViewEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_profile_white));
        }

        if (method.getLoginType().equals("google") || method.getLoginType().equals("facebook")) {
            editTextName.setFocusable(false);
            editTextName.setCursorVisible(false);
        }

        conMain.setVisibility(View.GONE);
        conNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        if (method.isNetworkAvailable()) {
            profile(method.userId());
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

        return view;

    }

    @Subscribe
    public void getData(Events.ProImage proImage) {
        isProfile = proImage.isProfile();
        isRemove = proImage.isRemove();
        if (proImage.isProfile()) {
            imageProfile = proImage.getImagePath();
            Uri uri = Uri.fromFile(new File(imageProfile));
            Glide.with(getActivity().getApplicationContext()).load(uri)
                    .placeholder(R.drawable.user_profile)
                    .into(imageViewUser);
        }
        if (proImage.isRemove()) {
            Glide.with(getActivity().getApplicationContext()).load(R.drawable.user_profile)
                    .placeholder(R.drawable.user_profile)
                    .into(imageViewUser);
        }
    }

    private void save() {

        String name = editTextName.getText().toString();
        String phoneNo = editTextPhoneNo.getText().toString();
        String email = editTextEmail.getText().toString();

        editTextName.setError(null);
        editTextPhoneNo.setError(null);

        if (name.equals("") || name.isEmpty()) {
            editTextName.requestFocus();
            editTextName.setError(getResources().getString(R.string.please_enter_name));
        } else if (phoneNo.equals("") || phoneNo.isEmpty()) {
            editTextPhoneNo.requestFocus();
            editTextPhoneNo.setError(getResources().getString(R.string.please_enter_phone));
        } else if (email.equals("") || email.isEmpty()) {
            editTextEmail.requestFocus();
            editTextEmail.setError(getResources().getString(R.string.please_enter_email));
        } else {
            if (method.isNetworkAvailable()) {

                editTextName.clearFocus();
                editTextPhoneNo.clearFocus();
                imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextPhoneNo.getWindowToken(), 0);

                profileUpdate(method.userId(), name, phoneNo, email,imageProfile);

            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }

    }

    public void profile(String userId) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("customer_id", userId);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ProfileRP> call = apiService.getProfile(requestBody);
            call.enqueue(new Callback<ProfileRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<ProfileRP> call, @NotNull Response<ProfileRP> response) {
                    int statusCode = response.code();

                    if (getActivity() != null) {

                        try {

                            ProfileRP profileRP = response.body();

                            assert profileRP != null;
                            if (profileRP.getResponseCode()==1) {

                                imageProfile = profileRP.getResponseData().getProfileImage();

                                Glide.with(getActivity().getApplicationContext()).load(profileRP.getResponseData().getProfileImage())
                                        .placeholder(R.drawable.user_profile).into(imageViewUser);

                                textViewName.setText(profileRP.getResponseData().getName());

                                editTextName.setText(profileRP.getResponseData().getName());
                                if (profileRP.getResponseData().getEmail().equals("")) {
                                    textInputEmail.setVisibility(View.GONE);
                                } else {
                                    textInputEmail.setVisibility(View.VISIBLE);
                                    editTextEmail.setText(profileRP.getResponseData().getEmail());
                                }
                                editTextPhoneNo.setText(profileRP.getResponseData().getPhone());

                                imageViewEdit.setOnClickListener(v -> {
                                    BottomSheetDialogFragment fragment = new ProImage();
                                    fragment.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                                });

                                imageViewUser.setOnClickListener(V -> {
                                    BottomSheetDialogFragment fragment = new ProImage();
                                    fragment.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                                });

                                buttonSubmit.setOnClickListener(v -> save());

                                conMain.setVisibility(View.VISIBLE);

                            } else if (profileRP.getResponseCode()==2) {
                                method.suspend(profileRP.getResponseText());
                            } else {
                                conNoData.setVisibility(View.VISIBLE);
                                method.alertBox(profileRP.getResponseText());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<ProfileRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    conNoData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

    private void profileUpdate(String id, String sendName, String sendPhone, String email, String imageProfile) {

        if (getActivity() != null) {

            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            MultipartBody.Part body = null;

//            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
//            jsObj.addProperty("user_id", id);
//            jsObj.addProperty("name", sendName);
//            jsObj.addProperty("phone", sendPhone);
//            jsObj.addProperty("is_remove", isRemove);
//            jsObj.addProperty("method_name", "user_profile_update");

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("customer_id", id);
            requestBody.put("name", sendName);
            requestBody.put("email", email);
            requestBody.put("phone", sendPhone);
            requestBody.put("address", "Test");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<DataRP> call = apiService.editProfile(requestBody);
            call.enqueue(new Callback<DataRP>() {
                @Override
                public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {
                    int statusCode = response.code();

//                    if (getActivity() != null) {
                        try {
                            DataRP dataRP = response.body();
                            assert dataRP != null;

                            if (dataRP.getResponseCode()==1) {
//                                if (dataRP.getSuccess().equals("1")) {
                                    Events.ProfileUpdate profileUpdate = new Events.ProfileUpdate("");
                                    GlobalBus.getBus().post(profileUpdate);
                                    getActivity().getSupportFragmentManager().popBackStack();
//                                }
                                Toast.makeText(getActivity(), dataRP.getResponseText(), Toast.LENGTH_SHORT).show();
                            } else if (dataRP.getResponseCode()==2) {
                                if (!isProfile) {
                                    method.suspend(dataRP.getMessage());
                                }
                            } else {
                                if (!isProfile) {
                                    method.alertBox(dataRP.getResponseText());
                                }
                            }

                        } catch (Exception e) {
                            if (!isProfile) {
                                Log.d("exception_error", e.toString());
                                method.alertBox(getResources().getString(R.string.failed_try_again));
                            }
                            e.printStackTrace();
                        }
//                    }
                    imageUpdate(id,imageProfile);
//                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
//                    progressDialog.dismiss();
//                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    imageUpdate(id,imageProfile);
                }
            });

        }

    }


    private void imageUpdate(String id, String profileImage) {

        if (getActivity() != null) {

//            progressDialog.show();
//            progressDialog.setMessage(getResources().getString(R.string.loading));
//            progressDialog.setCancelable(false);

            MultipartBody.Part body = null;

//            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
//            jsObj.addProperty("customer_id", id);
            RequestBody customer_id =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), id);
            if (isProfile) {
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(profileImage));
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("image", new File(profileImage).getName(), requestFile);
            }
            // add another part within the multipart request
//            RequestBody requestBody_data =
//                    RequestBody.create(MediaType.parse("multipart/form-data"), API.toBase64(jsObj.toString()));
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<DataRP> call = apiService.imageUpdate(body, customer_id);
            call.enqueue(new Callback<DataRP>() {
                @Override
                public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {
                    int statusCode = response.code();

                    if (getActivity() != null) {
                        try {
                            DataRP dataRP = response.body();
                            assert dataRP != null;

                            if (dataRP.getResponseCode()==1) {
//                                if (dataRP.getSuccess().equals("1")) {
                                Events.ProfileUpdate profileUpdate = new Events.ProfileUpdate("");
                                GlobalBus.getBus().post(profileUpdate);
                                getActivity().getSupportFragmentManager().popBackStack();
//                                }
                                Toast.makeText(getActivity(), dataRP.getResponseText(), Toast.LENGTH_SHORT).show();
                            } /*else if (dataRP.getResponseCode()==2) {
                                method.suspend(dataRP.getResponseText());
                            } */else {
                                method.alertBox(dataRP.getResponseText());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

}
