package com.app.effistay.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.effistay.R;
import com.app.effistay.util.Method;
import com.google.android.material.appbar.MaterialToolbar;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class RefundCacellation extends AppCompatActivity {

    private Method method;
    private WebView webView;
    private ProgressBar progressBar;
    private ConstraintLayout conNoData;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_cancellation);

        method = new Method(RefundCacellation.this);
        method.forceRTLIfSupported();

        MaterialToolbar toolbar = findViewById(R.id.toolbar_rnc);
        toolbar.setTitle(getResources().getString(R.string.refund_and_cancellation));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.progressBar_rnc);
        conNoData = findViewById(R.id.con_noDataFound);
        webView = findViewById(R.id.webView_rnc);

        LinearLayout linearLayout = findViewById(R.id.linearLayout_rnc);
        method.bannerAd(linearLayout);

        conNoData.setVisibility(View.GONE);

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
            webView.loadUrl("https://sites.google.com/view/assure-refund-and-cancellation/home");
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
