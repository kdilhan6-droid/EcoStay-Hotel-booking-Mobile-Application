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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        setupClickListeners();
    }

    private void setupClickListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Enhanced input validation with detailed checks
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Full name is required");
            nameEditText.requestFocus();
            return;
        }

        if (name.length() < 2) {
            nameEditText.setError("Full name must be at least 2 characters");
            nameEditText.requestFocus();
            return;
        }

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

        // Enhanced password validation
        if (!password.matches(".*[A-Za-z].*")) {
            passwordEditText.setError("Password must contain at least one letter");
            passwordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Please confirm your password");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // Show loading state
        registerButton.setEnabled(false);
        registerButton.setText("Creating Account...");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserProfile(name, email);
                        } else {
                            registerButton.setEnabled(true);
                            registerButton.setText("Register");
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserProfile(String name, String email) {
        String userId = mAuth.getCurrentUser().getUid();
        
        // Create comprehensive user profile with all required fields
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", name);
        user.put("email", email);
        user.put("createdAt", System.currentTimeMillis());
        user.put("ecoPoints", 0);
        user.put("sustainabilityLevel", "Beginner");
        
        // Profile management fields
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("roomType", "Eco-friendly suites");
        preferences.put("viewPreference", "Mountain/Forest view");
        preferences.put("occupancy", 2);
        preferences.put("budgetRange", "$80-150/night");
        preferences.put("carbonOffset", true);
        preferences.put("organicFood", true);
        preferences.put("localTransportation", true);
        user.put("preferences", preferences);
        
        // Travel dates and booking history
        Map<String, Object> travelDates = new HashMap<>();
        travelDates.put("preferredSeason", "Spring & Fall");
        travelDates.put("flexibleDates", true);
        travelDates.put("avoidDates", "Summer holidays");
        user.put("travelDates", travelDates);
        
        Map<String, Object> bookingHistory = new HashMap<>();
        bookingHistory.put("totalSpent", 0);
        bookingHistory.put("totalBookings", 0);
        bookingHistory.put("completedBookings", new java.util.ArrayList<>());
        bookingHistory.put("upcomingBookings", new java.util.ArrayList<>());
        user.put("bookingHistory", bookingHistory);
        
        // Notification preferences
        Map<String, Object> notifications = new HashMap<>();
        notifications.put("ecoEvents", true);
        notifications.put("specialOffers", true);
        notifications.put("bookingReminders", true);
        notifications.put("environmentalTips", true);
        notifications.put("weeklyNewsletter", true);
        user.put("notificationPreferences", notifications);

        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        registerButton.setEnabled(true);
                        registerButton.setText("Register");
                        
                        if (task.isSuccessful()) {
                            // Show welcome message with profile features
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
                            builder.setTitle("🎉 Welcome to EcoStay Retreat!");
                            builder.setMessage("Your account has been created successfully!\n\n" +
                                    "✅ Profile created with eco-preferences\n" +
                                    "✅ Personalized recommendations enabled\n" +
                                    "✅ Notification preferences set\n" +
                                    "✅ Booking history tracking ready\n" +
                                    "✅ Travel dates management available\n\n" +
                                    "Start exploring our sustainable accommodations!");
                            builder.setPositiveButton("Start Exploring", (dialog, which) -> {
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            });
                            builder.show();
                            
                            Toast.makeText(RegisterActivity.this, "🌿 Welcome to sustainable travel!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Enhanced error handling
                            String errorMessage = "Failed to save user profile";
                            if (task.getException() != null && task.getException().getMessage() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
                            builder.setTitle("⚠️ Profile Setup Error");
                            builder.setMessage("Account created but profile setup failed:\n" + errorMessage + 
                                    "\n\nYou can still use the app and complete your profile later.");
                            builder.setPositiveButton("Continue", (dialog, which) -> {
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            });
                            builder.setNegativeButton("Try Again", (dialog, which) -> {
                                saveUserProfile(name, email); // Retry
                            });
                            builder.show();
                        }
                    }
                });
    }
}
