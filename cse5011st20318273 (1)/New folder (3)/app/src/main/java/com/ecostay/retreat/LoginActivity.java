package com.ecostay.retreat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        
        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        setupClickListeners();
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Enhanced input validation
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        // Show loading state
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loginButton.setEnabled(true);
                        loginButton.setText("Login");
                        
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "🎉 Welcome to EcoStay Retreat!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // Enhanced error handling with specific error cases
                            String errorMessage = "Authentication failed";
                            if (task.getException() != null) {
                                String exceptionMessage = task.getException().getMessage();
                                if (exceptionMessage != null) {
                                    if (exceptionMessage.contains("password is invalid")) {
                                        errorMessage = "Incorrect password. Please try again.";
                                    } else if (exceptionMessage.contains("no user record")) {
                                        errorMessage = "No account found with this email. Please register first.";
                                    } else if (exceptionMessage.contains("network error")) {
                                        errorMessage = "Network error. Please check your internet connection.";
                                    } else if (exceptionMessage.contains("too many requests")) {
                                        errorMessage = "Too many failed attempts. Please try again later.";
                                    } else if (exceptionMessage.contains("email is badly formatted")) {
                                        errorMessage = "Invalid email format. Please enter a valid email.";
                                    } else {
                                        errorMessage = exceptionMessage;
                                    }
                                }
                            }
                            
                            // Show user-friendly error dialog
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("❌ Login Failed");
                            builder.setMessage(errorMessage + "\n\nNeed help?\n• Check your email and password\n• Ensure internet connection\n• Contact support if issues persist");
                            builder.setPositiveButton("Try Again", null);
                            builder.setNegativeButton("Register Instead", (dialog, which) -> {
                                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                            });
                            builder.show();
                            
                            Toast.makeText(LoginActivity.this, "❌ " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
