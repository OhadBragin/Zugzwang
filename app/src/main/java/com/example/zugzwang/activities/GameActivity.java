package com.example.zugzwang.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;
import com.example.zugzwang.views.ResultDialog;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "mode";
    public static final String MODE_AI = "ai";
    public static final String MODE_LOCAL = "local";
    public static final String MODE_ONLINE = "online";

    public static Intent newIntent(Context context, String mode) {
        return new Intent(context, GameActivity.class).putExtra(EXTRA_MODE, mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String mode = getIntent().getStringExtra(EXTRA_MODE);
        int opponent = MODE_LOCAL.equals(mode) ? R.string.opponent_local
                : MODE_ONLINE.equals(mode) ? R.string.opponent_online
                : R.string.opponent_ai;
        ((TextView) findViewById(R.id.tvOpponent)).setText(opponent);

        findViewById(R.id.btnResign).setOnClickListener(v -> new ResultDialog(this).show());
    }
}
