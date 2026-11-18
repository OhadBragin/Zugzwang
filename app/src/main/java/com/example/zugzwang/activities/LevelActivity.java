package com.example.zugzwang.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class LevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
        MaterialButtonToggleGroup groupLevel = findViewById(R.id.groupLevel);
        findViewById(R.id.btnStart).setOnClickListener(v -> {
            int checked = groupLevel.getCheckedButtonId();
            int level = checked == R.id.btnEasy ? 1 : checked == R.id.btnHard ? 3 : 2;
            startActivity(GameActivity.newIntent(this, GameActivity.MODE_AI)
                    .putExtra(GameActivity.EXTRA_LEVEL, level));
        });
    }
}
