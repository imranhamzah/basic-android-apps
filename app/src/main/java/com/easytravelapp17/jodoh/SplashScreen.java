package com.easytravelapp17.jodoh;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(4000)
                .withBackgroundColor(Color.parseColor(getString(R.string.backgroundSplashScreen)))
                .withLogo(R.mipmap.ic_launcher)
                .withHeaderText(getString(R.string.welcome_guest))
                .withFooterText(getString(R.string.copyright))
                .withBeforeLogoText(getString(R.string.app_name))
                .withAfterLogoText(getString(R.string.splash_screen_description));

        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.WHITE);
        config.getAfterLogoTextView().setTextColor(Color.WHITE);
        config.getBeforeLogoTextView().setTextColor(Color.WHITE);

        View view = config.create();

        setContentView(view);

    }
}
