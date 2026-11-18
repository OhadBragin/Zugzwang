package com.example.zugzwang.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;
import com.example.zugzwang.data.AuthService;
import com.example.zugzwang.data.DatabaseCallback;
import com.example.zugzwang.data.DatabaseService;
import com.example.zugzwang.models.GameRecord;
import com.example.zugzwang.views.ResultDialog;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "mode";
    /** Computer difficulty for MODE_AI: 1 easy, 2 medium, 3 hard. */
    public static final String EXTRA_LEVEL = "level";
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

        findViewById(R.id.btnResign).setOnClickListener(v -> {
            if (MODE_AI.equals(mode)) saveResignation();
            new ResultDialog(this).show();
        });
    }

    /**
     * Resigning against the computer counts as a loss in the player's history and stats.
     * TODO: save wins, draws and the real move count here too once the game logic exists.
     * Two-player games on one phone aren't saved: there's no way to know which side the account owner played.
     */
    private void saveResignation() {
        int level = getIntent().getIntExtra(EXTRA_LEVEL, 2);
        GameRecord game = new GameRecord(GameRecord.MODE_AI, GameRecord.RESULT_LOSS,
                GameRecord.REASON_RESIGN, 0, level);
        DatabaseService.saveGame(AuthService.currentUid(), game, new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(GameActivity.this, R.string.game_save_failed, Toast.LENGTH_LONG).show();
            }
        });
    }
}
