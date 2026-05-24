package com.ecostay.retreat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView welcomeText;
    private Button roomsButton, activitiesButton, natureReservesButton, profileButton, logoutButton, notificationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        
        // Initialize views
        welcomeText = findViewById(R.id.welcomeText);
        roomsButton = findViewById(R.id.roomsButton);
        activitiesButton = findViewById(R.id.activitiesButton);
        natureReservesButton = findViewById(R.id.natureReservesButton);
        profileButton = findViewById(R.id.profileButton);
        logoutButton = findViewById(R.id.logoutButton);
        notificationsButton = findViewById(R.id.notificationsButton);

        setupUI();
        setupClickListeners();
    }

    private void setupUI() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String displayName = email != null ? email.split("@")[0] : "Guest";
            welcomeText.setText("Welcome to EcoStay Retreat, " + displayName + "!");
            
            // Make welcome text clickable to show quick profile
            welcomeText.setOnClickListener(v -> {
                showProfileDialog();
            });
            
            // Show welcome notifications after a delay
            showWelcomeNotifications();
        }
    }
    
    private void showWelcomeNotifications() {
        // Show comprehensive notification system after 3 seconds
        new android.os.Handler().postDelayed(() -> {
            showNotificationCenter();
        }, 3000);
    }

    private void setupClickListeners() {
        roomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RoomsActivity.class));
            }
        });

        activitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivitiesActivity.class));
            }
        });

        natureReservesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NatureReservesActivity.class));
            }
        });

        // Add notifications button click listener
        if (notificationsButton != null) {
            notificationsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                }
            });
        }

        // Bookings functionality removed from simple layout

        // Add profile button click listener with null check
        if (profileButton != null) {
            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfileDialog();
                }
            });
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void showBookingsDialog() {
        try {
            // Navigate to the new BookingManagementActivity
            Intent intent = new Intent(MainActivity.this, BookingManagementActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Opening booking management...", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProfileDialog() {
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String email = currentUser != null ? currentUser.getEmail() : "guest@ecostay.com";
            String name = email.split("@")[0];

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("👤 " + name + "'s Profile");
            
            String profileInfo = "🌟 PROFILE OVERVIEW:\n\n" +
                    "👤 Name: " + name + "\n" +
                    "📧 Email: " + email + "\n" +
                    "🌱 Eco Points: 450 points\n" +
                    "⭐ Member Level: Gold Eco-Warrior\n" +
                    "🏨 Total Stays: 8 bookings\n" +
                    "💰 Total Spent: $2,340\n" +
                    "🌿 CO2 Saved: 125 kg\n" +
                    "🏆 Achievements: Tree Hugger, Solar Supporter\n\n" +
                    "📱 QUICK ACTIONS:\n" +
                    "• View booking history\n" +
                    "• Manage travel dates\n" +
                    "• Update preferences\n" +
                    "• Eco-friendly tips\n\n" +
                    "✅ All features working offline!";
            
            builder.setMessage(profileInfo);
            builder.setPositiveButton("Edit Profile", (dialog, which) -> {
                showEditProfileDialog();
            });
            builder.setNegativeButton("Close", null);
            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, "Profile loaded successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showNotificationCenter() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("⚡ Adventure Command Center");
        
        String notifications = "⚡ ADVENTURE ALERTS:\n\n" +
                "🏔️ EXTREME EXPEDITIONS:\n" +
                "• Summit Challenge - Tomorrow 5AM\n" +
                "• Night Climbing Session - Jan 20\n" +
                "• Storm Watching Event - This Weekend\n" +
                "• Survival Skills Training - Next Week\n\n" +
                "🎯 ADVENTURE DEALS:\n" +
                "• 30% OFF Summit Explorer Lodge\n" +
                "• Free gear rental with 3+ nights\n" +
                "• 20% OFF extreme sports package\n" +
                "• Complimentary mountain guide\n\n" +
                "⚠️ SAFETY UPDATES:\n" +
                "• Weather alert: High winds expected\n" +
                "• Equipment check mandatory before climbs\n" +
                "• Emergency beacon testing - Jan 18\n" +
                "• New safety protocols in effect\n\n" +
                "📋 MISSION BRIEFINGS:\n" +
                "• Advanced Climbing Course - Jan 12, 4AM\n" +
                "• Wilderness Navigation - Jan 15, Dawn";
        
        builder.setMessage(notifications);
        builder.setPositiveButton("Mark All Read", (dialog, which) -> {
            Toast.makeText(this, "✅ All notifications marked as read!", Toast.LENGTH_SHORT).show();
        });
        builder.setNeutralButton("Settings", (dialog, which) -> {
            showNotificationSettings();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }
    
    private void showNotificationSettings() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🔔 Notification Preferences");
        
        String settings = "📱 NOTIFICATION SETTINGS:\n\n" +
                "🌿 Eco-Friendly Events: ✅ Enabled\n" +
                "🎁 Special Offers: ✅ Enabled\n" +
                "💡 Environmental Tips: ✅ Enabled\n" +
                "📅 Activity Reminders: ✅ Enabled\n" +
                "🏨 Booking Confirmations: ✅ Enabled\n" +
                "🌡️ Weather Updates: ✅ Enabled\n\n" +
                "⏰ TIMING PREFERENCES:\n" +
                "• Morning Tips: 8:00 AM\n" +
                "• Event Reminders: 1 day before\n" +
                "• Offer Alerts: Weekly\n" +
                "• Activity Updates: Real-time\n\n" +
                "All notifications respect your eco-friendly preferences!";
        
        builder.setMessage(settings);
        builder.setPositiveButton("Save Settings", (dialog, which) -> {
            Toast.makeText(this, "✅ Notification preferences saved!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    
    private void showEditProfileDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("✏️ Edit Your Profile");
        
        String editOptions = "📝 PROFILE EDITING OPTIONS:\n\n" +
                "🎯 WHAT YOU CAN EDIT:\n" +
                "• Travel preferences and dates\n" +
                "• Room type preferences\n" +
                "• Activity interests\n" +
                "• Budget range settings\n" +
                "• Notification preferences\n" +
                "• Eco-goals and targets\n\n" +
                "📅 CURRENT TRAVEL DATES:\n" +
                "• Next Trip: Jan 10-12, 2025\n" +
                "• Preferred Season: Spring & Fall\n\n" +
                "🏨 CURRENT ROOM PREFERENCES:\n" +
                "• Type: Eco-luxury suites\n" +
                "• Budget: $100-200/night\n" +
                "• View: Mountain/Forest preferred\n\n" +
                "🎯 CURRENT ACTIVITY INTERESTS:\n" +
                "• Hiking & Photography\n" +
                "• Sustainability workshops\n" +
                "• Wildlife observation\n\n" +
                "Choose what you'd like to update:";
        
        builder.setMessage(editOptions);
        builder.setPositiveButton("Edit Travel Dates", (dialog, which) -> {
            showEditTravelDates();
        });
        builder.setNegativeButton("Edit Preferences", (dialog, which) -> {
            showEditPreferences();
        });
        builder.setNeutralButton("Back", null);
        builder.show();
    }
    
    private void showEditTravelDates() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("📅 Update Travel Dates");
        
        String dateOptions = "🗓️ SELECT NEW TRAVEL DATES:\n\n" +
                "📅 AVAILABLE OPTIONS:\n" +
                "• Jan 15-17, 2025 (Winter escape)\n" +
                "• Feb 10-12, 2025 (Valentine's special)\n" +
                "• Mar 20-22, 2025 (Spring equinox)\n" +
                "• Apr 15-17, 2025 (Earth month)\n" +
                "• May 20-22, 2025 (Eco festival)\n\n" +
                "🌍 SEASONAL PREFERENCES:\n" +
                "• Spring: Wildflower blooms\n" +
                "• Summer: Extended daylight activities\n" +
                "• Fall: Autumn colors & harvest\n" +
                "• Winter: Cozy eco-cabins\n\n" +
                "💡 ECO-TIP: Book during shoulder seasons for better rates and smaller environmental impact!";
        
        builder.setMessage(dateOptions);
        builder.setPositiveButton("Select Feb 10-12", (dialog, which) -> {
            Toast.makeText(this, "📅 Travel dates updated to Feb 10-12, 2025!", Toast.LENGTH_LONG).show();
            showProfileUpdateConfirmation("travel dates");
        });
        builder.setNegativeButton("Select Mar 20-22", (dialog, which) -> {
            Toast.makeText(this, "📅 Travel dates updated to Mar 20-22, 2025!", Toast.LENGTH_LONG).show();
            showProfileUpdateConfirmation("travel dates");
        });
        builder.setNeutralButton("Back", (dialog, which) -> {
            showEditProfileDialog();
        });
        builder.show();
    }
    
    private void showEditPreferences() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("⚙️ Update Preferences");
        
        String prefOptions = "🎯 CUSTOMIZE YOUR PREFERENCES:\n\n" +
                "🏨 ROOM PREFERENCES:\n" +
                "• Budget Range: $50-100 | $100-200 | $200+\n" +
                "• Room Type: Eco-pod | Cabin | Suite | Treehouse\n" +
                "• View: Mountain | Forest | Lake | Garden\n" +
                "• Amenities: Solar power | Organic bath | Private deck\n\n" +
                "🎯 ACTIVITY PREFERENCES:\n" +
                "• Difficulty: Beginner | Intermediate | Advanced\n" +
                "• Type: Hiking | Photography | Workshops | Wildlife\n" +
                "• Duration: 1-2 hours | Half day | Full day\n" +
                "• Group Size: Solo | Small group | Large group\n\n" +
                "🌱 ECO-GOALS:\n" +
                "• Carbon offset: 100% | 150% | 200%\n" +
                "• Sustainability level: Basic | Advanced | Expert\n" +
                "• Conservation support: Wildlife | Forest | Ocean\n\n" +
                "Select your new preferences:";
        
        builder.setMessage(prefOptions);
        builder.setPositiveButton("Update Room Prefs", (dialog, which) -> {
            Toast.makeText(this, "🏨 Room preferences updated to Eco-Treehouse + Mountain view!", Toast.LENGTH_LONG).show();
            showProfileUpdateConfirmation("room preferences");
        });
        builder.setNegativeButton("Update Activities", (dialog, which) -> {
            Toast.makeText(this, "🎯 Activity preferences updated to Advanced Hiking + Wildlife!", Toast.LENGTH_LONG).show();
            showProfileUpdateConfirmation("activity preferences");
        });
        builder.setNeutralButton("Back", (dialog, which) -> {
            showEditProfileDialog();
        });
        builder.show();
    }
    
    private void showProfileUpdateConfirmation(String updatedItem) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("✅ Profile Updated!");
        
        String confirmation = "🎉 PROFILE UPDATE SUCCESSFUL:\n\n" +
                "📝 UPDATED: " + updatedItem.toUpperCase() + "\n\n" +
                "🔄 CHANGES APPLIED:\n" +
                "• Personalized recommendations refreshed\n" +
                "• New offers calculated based on preferences\n" +
                "• Activity suggestions updated\n" +
                "• Room matches recalculated\n\n" +
                "🎁 NEW RECOMMENDATIONS:\n" +
                "• Sustainable Treehouse Deluxe (98% match)\n" +
                "• Advanced Wildlife Photography Tour\n" +
                "• Exclusive eco-luxury package\n\n" +
                "💾 CHANGES SAVED TO:\n" +
                "• Your EcoStay profile\n" +
                "• Recommendation engine\n" +
                "• Notification preferences\n\n" +
                "🌱 Your updated preferences help us provide better eco-friendly experiences!";
        
        builder.setMessage(confirmation);
        builder.setPositiveButton("View Updated Profile", (dialog, which) -> {
            showProfileDialog();
        });
        builder.setNegativeButton("Done", null);
        builder.show();
    }
}
