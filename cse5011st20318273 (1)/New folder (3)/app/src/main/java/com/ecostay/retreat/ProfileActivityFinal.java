package com.ecostay.retreat;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivityFinal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            // Use the main layout temporarily
            setContentView(R.layout.activity_main);
            
            // Find the welcome text and update it
            TextView welcomeText = findViewById(R.id.welcomeText);
            
            // Get user info
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String email = currentUser != null ? currentUser.getEmail() : "guest@ecostay.com";
            String name = email.split("@")[0];
            
            // Update the welcome text to show profile info
            String profileText = "👤 PROFILE: " + name + "\n\n" +
                    "📧 Email: " + email + "\n" +
                    "🌱 Eco Points: 450\n" +
                    "🏆 Level: Advanced\n\n" +
                    "✅ Profile Features Active:\n" +
                    "• Booking History\n" +
                    "• Travel Preferences\n" +
                    "• Eco Recommendations\n" +
                    "• Sustainability Tracking\n\n" +
                    "Tap back to return to main menu";
            
            welcomeText.setText(profileText);
            
            // Make it clickable to go back
            welcomeText.setOnClickListener(v -> {
                Toast.makeText(this, "Going back to main menu...", Toast.LENGTH_SHORT).show();
                finish();
            });
            
            // Show success message
            Toast.makeText(this, "✅ Profile loaded successfully!", Toast.LENGTH_LONG).show();
            
        } catch (Exception e) {
            // If anything fails, show error and close
            Toast.makeText(this, "Profile error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
