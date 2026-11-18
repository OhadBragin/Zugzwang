package com.example.zugzwang.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.zugzwang.R;
import com.example.zugzwang.ThemePrefs;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());

        setUpThemeToggle();
    }

    /** Light / dark / system buttons. Picking one saves it and recolors the app immediately. */
    private void setUpThemeToggle() {
        MaterialButtonToggleGroup groupTheme = findViewById(R.id.groupTheme);
        groupTheme.check(buttonForMode(ThemePrefs.getMode(this)));

        groupTheme.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            int mode = modeForButton(checkedId);
            if (mode != ThemePrefs.getMode(this)) {
                ThemePrefs.setMode(this, mode);
            }
        });
    }

    private static int buttonForMode(int mode) {
        if (mode == AppCompatDelegate.MODE_NIGHT_NO) return R.id.btnThemeLight;
        if (mode == AppCompatDelegate.MODE_NIGHT_YES) return R.id.btnThemeDark;
        return R.id.btnThemeSystem;
    }

    private static int modeForButton(int buttonId) {
        if (buttonId == R.id.btnThemeLight) return AppCompatDelegate.MODE_NIGHT_NO;
        if (buttonId == R.id.btnThemeDark) return AppCompatDelegate.MODE_NIGHT_YES;
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    }
}
