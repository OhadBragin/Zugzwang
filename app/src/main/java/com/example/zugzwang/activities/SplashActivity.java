package com.example.zugzwang.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;
import com.example.zugzwang.data.AuthService;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isFinishing()) return; // the user left during the splash
            // Firebase remembers the login, so a returning player skips the login screen.
            Class<?> next = AuthService.isLoggedIn() ? MainActivity.class : LoginActivity.class;
            startActivity(new Intent(this, next));
            finish();
        }, SPLASH_DELAY_MS);
    }
}
