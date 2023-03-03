package com.app.effistay.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.effistay.R;
import com.app.effistay.util.Method;
import com.google.android.material.appbar.MaterialToolbar;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AboutUs extends AppCompatActivity {

    private Method method;
    private WebView webView;
    private ProgressBar progressBar;
    private ConstraintLayout conNoData;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        method = new Method(AboutUs.this);
        method.forceRTLIfSupported();

        MaterialToolbar toolbar = findViewById(R.id.toolbar_AboutUs);
        toolbar.setTitle(getResources().getString(R.string.about_us));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webView = findViewById(R.id.webView_AboutUs);
        progressBar = findViewById(R.id.progressBar_AboutUs);
        conNoData = findViewById(R.id.con_noDataFound);
        LinearLayout linearLayout = findViewById(R.id.linearLayout_AboutUs);

        conNoData.setVisibility(View.GONE);

        method.bannerAd(linearLayout);

        if (method.isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);

            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setFocusableInTouchMode(false);
            webView.setFocusable(false);
            webView.getSettings().setDefaultTextEncodingName("UTF-8");
            webView.getSettings().setJavaScriptEnabled(true);
            String mimeType = "text/html";
            String encoding = "utf-8";
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
//            privacyPolicy();
                    webView.setVisibility(View.VISIBLE);
                }
            });
            webView.loadUrl("https://sites.google.com/view/assure-about-us/home");
        } else {
            progressBar.setVisibility(View.GONE);
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
