package com.example.zugzwang.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;
import com.google.android.material.appbar.MaterialToolbar;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
    }
}
