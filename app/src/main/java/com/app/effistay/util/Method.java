package com.app.effistay.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.app.effistay.R;
import com.app.effistay.activity.MainActivity;
import com.app.effistay.interfaces.OnClick;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Method {

    public Activity activity;
    public OnClick onClick;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final String myPreference = "SingleHotel";
    public String prefLogin = "pref_login";
    private String firstTime = "firstTime";
    public String profileId = "profileId";
    public String userName = "userName";
    public String userEmail = "userEmail";
    public String userPhone = "userPhone";
    public String loginType = "loginType";
    public String showLogin = "show_login";
    public String notification = "notification";
    public String themSetting = "them";

    public static boolean personalizationAd = false, loginBack = false;

    public Method(Activity activity) {
        this.activity = activity;
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
    }

    public Method(Activity activity, OnClick onClick) {
        this.activity = activity;
        this.onClick = onClick;
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
    }

    public void clearPreference(){
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public void login() {
        if (!pref.getBoolean(firstTime, false)) {
            editor.putBoolean(prefLogin, false);
            editor.putBoolean(firstTime, true);
            editor.commit();
        }
    }

    //user login or not
    public boolean isLogin() {
        return pref.getBoolean(prefLogin, false);
    }

    //get login type
    public String getLoginType() {
        return pref.getString(loginType, null);
    }

    //get user id
    public String userId() {
        return pref.getString(profileId, "0");
    }

    public String getUserName() {
        return pref.getString(userName, "");
    }

    public String getUserEmail() {
        return pref.getString(userEmail, "");
    }

    public String getUserPhone() {
        return pref.getString(userPhone, "");
    }

    //Get theme
    public String getTheme() {
        return pref.getString(themSetting, "system");
    }

    //get device id
    @SuppressLint("HardwareIds")
    public String getDeviceId() {
        String deviceId;
        try {
            deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            deviceId = "NotFound";
        }
        return deviceId;
    }

    public void forceRTLIfSupported() {
        if (activity.getResources().getString(R.string.isRTL).equals("true")) {
            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    //rtl
    public boolean isRtl() {
        return activity.getResources().getString(R.string.isRTL).equals("true");
    }

    //network check
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //google map application installation or not check
    public boolean isAppInstalled() {
        String packageName = "com.google.android.apps.maps";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        return mIntent != null;
    }

    public void changeStatusBarColor() {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    //---------------Interstitial Ad---------------//

    public void onClickAd(final int position, final String type, final String id, final String title) {

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        if (Constant.appRP != null) {
                progressDialog.dismiss();
                onClick.position(position, type, id, title);
        } else {
            progressDialog.dismiss();
            onClick.position(position, type, id, title);
        }

    }

    //---------------Interstitial Ad---------------//

    //---------------Banner Ad---------------//

    public void bannerAd(LinearLayout linearLayout) {
//        if (Constant.appRP != null) {
//            if (Constant.appRP.isBanner_ad()) {
//                if (Constant.appRP.getBanner_ad_type().equals("admob")) {
//                    if (personalizationAd) {
//                        showPersonalizedAds(linearLayout);
//                    } else {
//                        showNonPersonalizedAds(linearLayout);
//                    }
//                } else {
//                    fbBannerAd(linearLayout);
//                }
//            } else {
//                linearLayout.setVisibility(View.GONE);
//            }
//        } else {
//            linearLayout.setVisibility(View.GONE);
//        }
    }

    //---------------Banner Ad---------------//

    //alert message box
    public void alertBox(String message) {

        try {
            if (activity != null) {
                if (!activity.isFinishing()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
                    builder.setMessage(Html.fromHtml(message));
                    builder.setCancelable(false);
                    builder.setPositiveButton(activity.getResources().getString(R.string.ok),
                            (arg0, arg1) -> {

                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        } catch (Exception e) {
            Log.d("error_message", e.toString());
        }

    }

    //account suspend
    public void suspend(String message) {

        if (isLogin()) {

            String loginType = getLoginType();
            assert loginType != null;
            if (loginType.equals("google")) {

                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                // Build a GoogleSignInClient with the options specified by gso.
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(activity, task -> {

                        });
            } else if (loginType.equals("facebook")) {
                LoginManager.getInstance().logOut();
            }

            editor.putBoolean(prefLogin, false);
            editor.commit();
        }

        try {
            if (activity != null) {
                if (!activity.isFinishing()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
                    builder.setMessage(Html.fromHtml(message));
                    builder.setCancelable(false);
                    builder.setPositiveButton(activity.getResources().getString(R.string.ok),
                            (arg0, arg1) -> {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                                activity.finishAffinity();
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        } catch (Exception e) {
            Log.d("error_message", e.toString());
        }

    }

    //check dark mode or not
    public boolean isDarkMode() {
        int currentNightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                return false;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                return true;
            default:
                return false;
        }
    }

    public String webViewText() {
        String color;
        if (isDarkMode()) {
            color = Constant.webTextDark;
        } else {
            color = Constant.webTextLight;
        }
        return color;
    }

    public String webViewLink() {
        String color;
        if (isDarkMode()) {
            color = Constant.webLinkDark;
        } else {
            color = Constant.webLinkLight;
        }
        return color;
    }

    public String isWebViewTextRtl() {
        String isRtl;
        if (isRtl()) {
            isRtl = "rtl";
        } else {
            isRtl = "ltr";
        }
        return isRtl;
    }

}
