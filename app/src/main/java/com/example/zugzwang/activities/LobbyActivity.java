package com.example.zugzwang.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;
import com.google.android.material.appbar.MaterialToolbar;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());

        View btnCreateRoom = findViewById(R.id.btnCreateRoom);
        View layoutWaiting = findViewById(R.id.layoutWaiting);
        btnCreateRoom.setOnClickListener(v -> {
            btnCreateRoom.setVisibility(View.GONE);
            layoutWaiting.setVisibility(View.VISIBLE);
        });
        findViewById(R.id.btnCancelRoom).setOnClickListener(v -> {
            layoutWaiting.setVisibility(View.GONE);
            btnCreateRoom.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.btnJoin).setOnClickListener(v ->
                startActivity(GameActivity.newIntent(this, GameActivity.MODE_ONLINE)));
        findViewById(R.id.btnRandom).setOnClickListener(v ->
                startActivity(GameActivity.newIntent(this, GameActivity.MODE_ONLINE)));
    }
}
