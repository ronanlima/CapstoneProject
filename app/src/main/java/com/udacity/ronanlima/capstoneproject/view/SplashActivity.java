package com.udacity.ronanlima.capstoneproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.udacity.ronanlima.capstoneproject.R;

public class SplashActivity extends AppCompatActivity {

    private static final int DELAY_MILLIS = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }, DELAY_MILLIS);
    }
}
