package com.example.zugzwang.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.zugzwang.R;
import com.example.zugzwang.ThemePrefs;
import com.example.zugzwang.data.AuthService;
import com.example.zugzwang.data.DatabaseCallback;
import com.example.zugzwang.data.DatabaseService;
import com.example.zugzwang.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.zugzwang.activities.LoginActivity.textOf;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvAccountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());

        setUpAccount();
        setUpThemeToggle();
    }

    /** Shows who is logged in, with buttons to change the username and to log out. */
    private void setUpAccount() {
        tvAccountName = findViewById(R.id.tvAccountName);
        ((TextView) findViewById(R.id.tvAccountEmail)).setText(AuthService.currentEmail());

        DatabaseService.getUser(AuthService.currentUid(), new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (user != null) tvAccountName.setText(user.getUsername());
            }

            @Override
            public void onFailure(Exception e) {
                // Leave the name empty; the email is still shown.
            }
        });

        findViewById(R.id.btnChangeUsername).setOnClickListener(v -> showChangeUsernameDialog());
        findViewById(R.id.btnLogOut).setOnClickListener(v -> {
            AuthService.logOut();
            Intent intent = new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void showChangeUsernameDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_username, null);
        TextInputLayout tilUsername = view.findViewById(R.id.tilUsername);
        if (tilUsername.getEditText() != null) {
            tilUsername.getEditText().setText(tvAccountName.getText());
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.change_username)
                .setView(view)
                .setPositiveButton(R.string.save, null) // set below, so an empty name doesn't close the dialog
                .setNegativeButton(R.string.cancel, null)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String username = textOf(tilUsername);
            if (username.isEmpty()) {
                tilUsername.setError(getString(R.string.error_required));
                return;
            }
            DatabaseService.updateUsername(AuthService.currentUid(), username, new DatabaseCallback<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    tvAccountName.setText(username);
                    Toast.makeText(SettingsActivity.this, R.string.username_updated, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(SettingsActivity.this, R.string.error_generic, Toast.LENGTH_LONG).show();
                }
            });
            dialog.dismiss();
        });
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
