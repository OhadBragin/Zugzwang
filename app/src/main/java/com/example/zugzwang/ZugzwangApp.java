package com.example.zugzwang;

import android.app.Application;

/** App entry point: applies the saved light/dark theme before any screen opens. */
public class ZugzwangApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemePrefs.applySaved(this);
    }
}
