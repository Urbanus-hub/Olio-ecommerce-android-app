package com.example.olio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText logEmail, logPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        logEmail = findViewById(R.id.etEmail);
        logPassword = findViewById(R.id.etPassword);
        dbHelper = new DatabaseHelper(this);
    }

    private void setupClickListeners() {
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        // Redirect to RegistrationActivity
        btnSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class))
        );

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = logEmail.getText().toString().trim();
        String password = logPassword.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(email, password)) {
            return;
        }

        // Check credentials in the local database
        boolean isValidUser = dbHelper.isEmailExists(email); // Validate using email existence
        if (isValidUser) {
            Toast.makeText(LoginActivity.this, "Log in successful.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Invalid email or password.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            logEmail.setError("Email is required");
            logEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            logEmail.setError("Please enter a valid email");
            logEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            logPassword.setError("Password is required");
            logPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            logPassword.setError("Password must be at least 6 characters");
            logPassword.requestFocus();
            return false;
        }

        return true;
    }
}
