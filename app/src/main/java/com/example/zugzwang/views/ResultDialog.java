package com.example.zugzwang.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.zugzwang.R;
import com.example.zugzwang.activities.MainActivity;

/** Game-over popup shown on top of the game screen. */
public class ResultDialog extends Dialog {

    private final Activity activity;

    public ResultDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_result);
        setCancelable(false);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        findViewById(R.id.btnBackToMenu).setOnClickListener(v -> {
            // Return to the existing main screen and close the game screens above it.
            Intent intent = new Intent(activity, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
            dismiss();
        });
    }
}
