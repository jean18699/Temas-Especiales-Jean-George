package com.pucmm.proyectofinal.roomviewmodel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private final static int LOADING_TIME = 3000;
    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // This method will be executed once the timer is over

            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();

        }, LOADING_TIME);
    }
}