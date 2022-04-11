package com.zeyaly.extractor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.zeyaly.extractor.session.Session;

public class SplashScreen extends AppCompatActivity {
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.splash_layout);
        initView();
    }

    private void initView() {
        session = new Session(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.getLogin().equalsIgnoreCase("Login")) {
                    Intent mainIntent = new Intent(SplashScreen.this, HomeScreen.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, 2000);
    }


}
