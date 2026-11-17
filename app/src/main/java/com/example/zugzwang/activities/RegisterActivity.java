package com.example.zugzwang.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;
import com.google.android.material.appbar.MaterialToolbar;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
        // Both buttons return to the login screen, which is still on the back stack.
        findViewById(R.id.btnRegister).setOnClickListener(v -> finish());
        findViewById(R.id.btnToLogin).setOnClickListener(v -> finish());
    }
}
