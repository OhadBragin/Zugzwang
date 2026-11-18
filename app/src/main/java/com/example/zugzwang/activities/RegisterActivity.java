package com.example.zugzwang.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zugzwang.R;
import com.example.zugzwang.data.AuthService;
import com.example.zugzwang.data.DatabaseCallback;
import com.example.zugzwang.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.zugzwang.activities.LoginActivity.textOf;

public class RegisterActivity extends AppCompatActivity {

    private static final int MIN_PASSWORD_LENGTH = 6; // Firebase's own minimum

    private TextInputLayout tilUsername;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilPasswordConfirm;
    private View btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tilUsername = findViewById(R.id.tilUsername);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilPasswordConfirm = findViewById(R.id.tilPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);

        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> signUp());
        // The login screen is still on the back stack.
        findViewById(R.id.btnToLogin).setOnClickListener(v -> finish());
    }

    private void signUp() {
        String username = textOf(tilUsername);
        String email = textOf(tilEmail);
        String password = textOf(tilPassword);
        String confirm = textOf(tilPasswordConfirm);

        tilUsername.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilPasswordConfirm.setError(null);
        boolean ok = true;
        if (username.isEmpty()) {
            tilUsername.setError(getString(R.string.error_required));
            ok = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(email.isEmpty() ? R.string.error_required : R.string.error_email_invalid));
            ok = false;
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            tilPassword.setError(getString(R.string.error_password_short));
            ok = false;
        }
        if (!confirm.equals(password)) {
            tilPasswordConfirm.setError(getString(R.string.error_passwords_differ));
            ok = false;
        }
        if (!ok) return;

        btnRegister.setEnabled(false);
        AuthService.signUp(email, password, username, new DatabaseCallback<User>() {
            @Override
            public void onSuccess(User user) {
                // Straight into the app, and clear Login off the back stack.
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this,
                        AuthService.errorMessage(RegisterActivity.this, e), Toast.LENGTH_LONG).show();
            }
        });
    }
}
