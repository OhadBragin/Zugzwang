package com.example.zugzwang.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.cardAi).setOnClickListener(v ->
                startActivity(new Intent(this, LevelActivity.class)));
        findViewById(R.id.cardLocal).setOnClickListener(v ->
                startActivity(GameActivity.newIntent(this, GameActivity.MODE_LOCAL)));
        findViewById(R.id.cardOnline).setOnClickListener(v ->
                startActivity(new Intent(this, LobbyActivity.class)));
        findViewById(R.id.btnHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
        findViewById(R.id.btnSettings).setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }
}
