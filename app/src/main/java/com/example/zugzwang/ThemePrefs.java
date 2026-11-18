package com.example.zugzwang;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

/** Saves the user's light/dark choice and applies it to the whole app. */
public final class ThemePrefs {

    private static final String PREFS = "settings";
    private static final String KEY_THEME_MODE = "theme_mode";

    private ThemePrefs() {
    }

    /**
     * Returns the saved mode: one of {@link AppCompatDelegate#MODE_NIGHT_FOLLOW_SYSTEM},
     * {@link AppCompatDelegate#MODE_NIGHT_NO} (light) or {@link AppCompatDelegate#MODE_NIGHT_YES} (dark).
     */
    public static int getMode(Context context) {
        return prefs(context).getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    /** Saves the mode and switches the app to it right away (open screens are recreated). */
    public static void setMode(Context context, int mode) {
        prefs(context).edit().putInt(KEY_THEME_MODE, mode).apply();
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    /** Applies the saved mode. Called once when the app starts. */
    public static void applySaved(Context context) {
        AppCompatDelegate.setDefaultNightMode(getMode(context));
    }

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
