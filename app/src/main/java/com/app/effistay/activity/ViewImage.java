package com.app.effistay.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app.effistay.util.TouchImageView;
import com.bumptech.glide.Glide;
import com.app.effistay.R;
import com.app.effistay.util.Method;
import com.google.android.material.appbar.MaterialToolbar;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ViewImage extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Method method = new Method(ViewImage.this);
        method.forceRTLIfSupported();

        String string = getIntent().getStringExtra("path");

        MaterialToolbar toolbar = findViewById(R.id.toolbar_view_image);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Making notification bar transparent
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // making notification bar transparent
        method.changeStatusBarColor();

        LinearLayout linearLayout = findViewById(R.id.linearLayout_view_image);
        method.bannerAd(linearLayout);

        TouchImageView imageView = findViewById(R.id.imageView_view_image);

        Glide.with(ViewImage.this).load(string)
                .placeholder(R.drawable.placeholder_landscape)
                .into(imageView);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
