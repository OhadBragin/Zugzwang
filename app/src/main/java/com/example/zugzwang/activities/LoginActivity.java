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
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private View btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> logIn());
        findViewById(R.id.btnToRegister).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void logIn() {
        String email = textOf(tilEmail);
        String password = textOf(tilPassword);

        tilEmail.setError(null);
        tilPassword.setError(null);
        boolean ok = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(email.isEmpty() ? R.string.error_required : R.string.error_email_invalid));
            ok = false;
        }
        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.error_required));
            ok = false;
        }
        if (!ok) return;

        btnLogin.setEnabled(false);
        AuthService.logIn(email, password, new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this,
                        AuthService.errorMessage(LoginActivity.this, e), Toast.LENGTH_LONG).show();
            }
        });
    }

    static String textOf(TextInputLayout field) {
        return field.getEditText() == null ? "" : field.getEditText().getText().toString().trim();
    }
}
