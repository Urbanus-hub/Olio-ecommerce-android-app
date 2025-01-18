package com.example.olio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etConfirmPassword;
    private ProgressBar progressB;
    private FirebaseAuth auth;
    private DatabaseHelper dbHelper; // Add the DatabaseHelper for local storage

    @Override
    public void onStart() {
        super.onStart();


        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressB = findViewById(R.id.progressSpinner);
        auth = FirebaseAuth.getInstance(); // Initialize Firebase Auth instance
        dbHelper = new DatabaseHelper(this); // Initialize DatabaseHelper

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);

        tvLoginLink.setOnClickListener(v -> startActivity(new Intent(RegistrationActivity.this, LoginActivity.class)));

        btnRegister.setOnClickListener(v -> {
            progressB.setVisibility(View.VISIBLE);

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            // Validation checks
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                progressB.setVisibility(View.GONE);
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            } else if (!password.equals(confirmPassword)) {
                progressB.setVisibility(View.GONE);
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase user creation
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressB.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // User creation successful in Firebase
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    String uid = user.getUid(); // Get Firebase UID

                                    // Save the user's email and UID in SQLite
                                    boolean isInserted = dbHelper.addUser(email, uid);
                                    if (isInserted) {
                                        Toast.makeText(RegistrationActivity.this, "Account created and saved locally.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Failed to save user locally.", Toast.LENGTH_SHORT).show();
                                    }

                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                }
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Authentication failed: " + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}
