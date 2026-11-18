package com.example.zugzwang.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;
import com.example.zugzwang.data.AuthService;
import com.example.zugzwang.data.DatabaseCallback;
import com.example.zugzwang.data.DatabaseService;
import com.example.zugzwang.models.User;

public class MainActivity extends AppCompatActivity {

    private TextView tvHello;
    private TextView tvWins;
    private TextView tvLosses;
    private TextView tvDraws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHello = findViewById(R.id.tvHello);
        tvWins = findViewById(R.id.tvWins);
        tvLosses = findViewById(R.id.tvLosses);
        tvDraws = findViewById(R.id.tvDraws);

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

    /** Reloads every time the screen comes back, so stats are fresh after a game or a history change. */
    @Override
    protected void onResume() {
        super.onResume();
        DatabaseService.getUser(AuthService.currentUid(), new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (user == null) return;
                tvHello.setText(getString(R.string.hello_name, user.getUsername()));
                tvWins.setText(String.valueOf(user.getWins()));
                tvLosses.setText(String.valueOf(user.getLosses()));
                tvDraws.setText(String.valueOf(user.getDraws()));
            }

            @Override
            public void onFailure(Exception e) {
                // Keep showing what is already on screen.
            }
        });
    }
}
