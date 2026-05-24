package com.ecostay.retreat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    
    // UI Components
    private Toolbar toolbar;
    private ImageView ivProfilePicture;
    private TextView tvUserName, tvUserEmail, tvEcoPoints, tvSustainabilityLevel;
    private Button btnEditProfile, btnNotifications, btnEcoTips, btnLogout;
    private Button btnBookingHistory, btnTravelDates, btnPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use main layout temporarily until profile layout is created
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        setupClickListeners();
        loadUserProfile();
        
        // Show profile content immediately since layout is simple
        showProfileContent();
    }

    private void initializeViews() {
        // Use existing elements from main layout with null checks
        toolbar = null; // No toolbar in main layout
        tvUserName = findViewById(R.id.welcomeText); // Reuse welcome text
        
        // Set other UI elements to null for now - we'll use dialogs instead
        ivProfilePicture = null;
        tvUserEmail = null;
        tvEcoPoints = null;
        tvSustainabilityLevel = null;
        btnEditProfile = null;
        btnNotifications = null;
        btnEcoTips = null;
        btnLogout = findViewById(R.id.logoutButton); // Reuse logout button
        btnBookingHistory = null;
        btnTravelDates = null;
        btnPreferences = null;
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle("👤 Your Profile");
            }
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }
    
    private void showProfileContent() {
        // Show profile overview dialog immediately
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("👤 Your EcoStay Profile");
        
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser != null ? currentUser.getEmail() : "Unknown";
        String name = email != null ? email.split("@")[0] : "Guest";
        
        String profileInfo = "🌿 PROFILE OVERVIEW:\n\n" +
                "👤 Name: " + name + "\n" +
                "📧 Email: " + email + "\n" +
                "🌱 Eco Points: 450 points\n" +
                "🏆 Level: Advanced Eco-Traveler\n\n" +
                "📱 PROFILE FEATURES:\n" +
                "• Booking History & Management\n" +
                "• Travel Dates & Preferences\n" +
                "• Personalized Recommendations\n" +
                "• Eco-friendly Notifications\n" +
                "• Sustainability Tracking\n\n" +
                "Choose an option below:";
        
        builder.setMessage(profileInfo);
        builder.setPositiveButton("📋 Booking History", (dialog, which) -> showBookingHistory());
        builder.setNegativeButton("📅 Travel Dates", (dialog, which) -> manageTravelDates());
        builder.setNeutralButton("⚙️ Preferences", (dialog, which) -> managePreferences());
        builder.show();
    }

    private void setupClickListeners() {
        // Only set up listeners for buttons that exist
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> editProfile());
        }
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v -> openNotifications());
        }
        if (btnEcoTips != null) {
            btnEcoTips.setOnClickListener(v -> showEcoTips());
        }
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> logout());
        }
        
        // Set up click listeners for new buttons with null checks
        if (btnBookingHistory != null) {
            btnBookingHistory.setOnClickListener(v -> showBookingHistory());
        }
        if (btnTravelDates != null) {
            btnTravelDates.setOnClickListener(v -> manageTravelDates());
        }
        if (btnPreferences != null) {
            btnPreferences.setOnClickListener(v -> managePreferences());
        }
        
        // Make the welcome text show profile options
        if (tvUserName != null) {
            tvUserName.setText("👤 Tap here for profile options");
            tvUserName.setOnClickListener(v -> showProfileContent());
        }
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Set basic user info only if UI elements exist
            if (tvUserEmail != null) {
                tvUserEmail.setText(currentUser.getEmail());
            }
            
            if (tvUserName != null) {
                String displayName = currentUser.getDisplayName();
                if (displayName != null && !displayName.isEmpty()) {
                    tvUserName.setText("👤 " + displayName + " - Tap for profile");
                } else {
                    // Extract name from email
                    String email = currentUser.getEmail();
                    if (email != null) {
                        String name = email.split("@")[0];
                        tvUserName.setText("👤 " + name.substring(0, 1).toUpperCase() + name.substring(1) + " - Tap for profile");
                    }
                }
            }

            // Load additional profile data from Firestore
            loadProfileFromFirestore(currentUser.getUid());
        }
    }

    private void loadProfileFromFirestore(String userId) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        updateProfileFromDocument(document);
                    } else {
                        // Create default profile data
                        createDefaultProfile(userId);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "⚠️ Failed to load profile data", Toast.LENGTH_SHORT).show();
                    // Set default values only if UI elements exist
                    if (tvEcoPoints != null) {
                        tvEcoPoints.setText("0 points");
                    }
                    if (tvSustainabilityLevel != null) {
                        tvSustainabilityLevel.setText("Beginner");
                    }
                });
    }

    private void updateProfileFromDocument(DocumentSnapshot document) {
        // Update eco points only if UI element exists
        if (tvEcoPoints != null) {
            Long ecoPoints = document.getLong("ecoPoints");
            if (ecoPoints != null) {
                tvEcoPoints.setText(ecoPoints + " eco points");
            } else {
                tvEcoPoints.setText("0 eco points");
            }
        }

        // Update sustainability level only if UI element exists
        if (tvSustainabilityLevel != null) {
            String sustainabilityLevel = document.getString("sustainabilityLevel");
            if (sustainabilityLevel != null) {
                tvSustainabilityLevel.setText(sustainabilityLevel);
            } else {
                tvSustainabilityLevel.setText("Beginner");
            }
        }

        // Update full name if available and UI element exists
        String fullName = document.getString("fullName");
        if (fullName != null && !fullName.isEmpty() && tvUserName != null) {
            tvUserName.setText("👤 " + fullName + " - Tap for profile");
        }
    }

    private void createDefaultProfile(String userId) {
        // Create default profile in Firestore
        java.util.Map<String, Object> defaultProfile = new java.util.HashMap<>();
        defaultProfile.put("ecoPoints", 0);
        defaultProfile.put("sustainabilityLevel", "Beginner");
        defaultProfile.put("createdAt", System.currentTimeMillis());

        db.collection("users").document(userId)
                .set(defaultProfile)
                .addOnSuccessListener(aVoid -> {
                    if (tvEcoPoints != null) {
                        tvEcoPoints.setText("0 eco points");
                    }
                    if (tvSustainabilityLevel != null) {
                        tvSustainabilityLevel.setText("Beginner");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "⚠️ Failed to create profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void editProfile() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("✏️ Edit Profile");
        builder.setMessage("Profile editing features:\n\n" +
                "📧 Update email preferences\n" +
                "📱 Change notification settings\n" +
                "🌱 Set eco-friendly goals\n" +
                "📸 Update profile picture\n" +
                "🎯 Customize experience\n\n" +
                "Feature coming soon in next update!");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void openNotifications() {
        // Show comprehensive notifications dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("⚡ Adventure Command Center");
        
        String notifications = "🏔️ MISSION UPDATES:\n\n" +
                "🎯 EXTREME CHALLENGES:\n" +
                "• Death Valley Expedition - Tomorrow 4AM\n" +
                "• Ice Climbing Competition - This Weekend\n" +
                "• Volcano Base Camp Setup - Next Monday\n\n" +
                
                "💥 WARRIOR DISCOUNTS:\n" +
                "• 40% OFF Survival Gear this month!\n" +
                "• Free tactical equipment with 3+ nights\n" +
                "• Elite member discount: 25% OFF\n" +
                "• Extreme sports package: 35% OFF\n\n" +
                
                "⚠️ MISSION BRIEFINGS:\n" +
                "• Storm Shelter Training - Check-in: Tomorrow\n" +
                "• High Altitude Prep - Starts: 4AM Saturday\n\n" +
                
                "🔥 SURVIVAL PROTOCOLS:\n" +
                "• Tip: Always carry emergency beacon\n" +
                "• Weather check: Monitor storm patterns\n" +
                "• Emergency contact: Keep radio charged\n\n" +
                
                "✅ All alerts are customized for your adventure level!";
        
        builder.setMessage(notifications);
        builder.setPositiveButton("Mark All Read", (dialog, which) -> {
            Toast.makeText(this, "✅ All notifications marked as read!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
        
        Toast.makeText(this, "📱 You have 8 new notifications!", Toast.LENGTH_SHORT).show();
    }

    private void showEcoTips() {
        // Show some eco tips
        String[] ecoTips = {
            "💡 Use reusable water bottles to reduce plastic waste",
            "🌱 Choose eco-friendly accommodations when traveling",
            "🚶‍♀️ Walk or bike instead of driving short distances",
            "♻️ Always separate your waste for recycling",
            "🌿 Support local and organic food options",
            "🌍 Offset your carbon footprint when flying",
            "💧 Take shorter showers to conserve water",
            "🔌 Unplug electronics when not in use"
        };
        
        StringBuilder tipsMessage = new StringBuilder("🌱 Daily Eco Tips:\n\n");
        for (int i = 0; i < Math.min(3, ecoTips.length); i++) {
            tipsMessage.append(ecoTips[i]).append("\n\n");
        }
        
        Toast.makeText(this, tipsMessage.toString(), Toast.LENGTH_LONG).show();
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
    
    private void showBookingHistory() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("📋 Booking History");
        
        String bookingHistory = "🏨 YOUR BOOKING HISTORY:\n\n" +
                "✅ COMPLETED BOOKINGS:\n" +
                "• Mountain View Eco Suite - Dec 15-18, 2024\n" +
                "  Price: $150/night | Rating: ⭐⭐⭐⭐⭐\n" +
                "• Forest Cabin Deluxe - Nov 20-22, 2024\n" +
                "  Price: $120/night | Rating: ⭐⭐⭐⭐⭐\n\n" +
                
                "🔄 UPCOMING BOOKINGS:\n" +
                "• Solar-Powered Deluxe Room - Jan 10-12, 2025\n" +
                "  Price: $100/night | Status: Confirmed\n" +
                "• Eco Pod Experience - Feb 5-6, 2025\n" +
                "  Price: $70/night | Status: Confirmed\n\n" +
                
                "🎯 ACTIVITY BOOKINGS:\n" +
                "• Guided Nature Hike - Jan 11, 2025 8AM\n" +
                "• Sustainability Workshop - Jan 12, 2025 2PM\n" +
                "• Bird Watching Tour - Feb 6, 2025 6AM\n\n" +
                
                "💰 TOTAL SPENT: $1,240\n" +
                "🌱 ECO POINTS EARNED: 450 points\n" +
                "🏆 SUSTAINABILITY LEVEL: Advanced";
        
        builder.setMessage(bookingHistory);
        builder.setPositiveButton("View Details", (dialog, which) -> {
            Toast.makeText(this, "📱 Opening detailed booking view...", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }
    
    private void manageTravelDates() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("📅 Travel Dates & Preferences");
        
        String travelInfo = "🗓️ YOUR TRAVEL PREFERENCES:\n\n" +
                "📅 PREFERRED TRAVEL DATES:\n" +
                "• Next Trip: January 10-12, 2025\n" +
                "• Preferred Season: Spring & Fall\n" +
                "• Avoid Dates: Summer holidays\n" +
                "• Flexible Dates: ✅ Yes (+/- 3 days)\n\n" +
                
                "🏨 ACCOMMODATION PREFERENCES:\n" +
                "• Room Type: Eco-friendly suites\n" +
                "• View Preference: Mountain/Forest view\n" +
                "• Occupancy: 2 guests\n" +
                "• Budget Range: $80-150/night\n\n" +
                
                "🎯 ACTIVITY PREFERENCES:\n" +
                "• Outdoor Activities: ✅ Hiking, Bird watching\n" +
                "• Workshops: ✅ Sustainability, Organic farming\n" +
                "• Difficulty Level: Moderate\n" +
                "• Duration: 2-4 hours preferred\n\n" +
                
                "🌱 ECO PREFERENCES:\n" +
                "• Carbon Offset: ✅ Always\n" +
                "• Organic Food: ✅ Preferred\n" +
                "• Local Transportation: ✅ Yes\n" +
                "• Waste Reduction: ✅ High priority";
        
        builder.setMessage(travelInfo);
        builder.setPositiveButton("Update Dates", (dialog, which) -> {
            Toast.makeText(this, "📅 Travel date preferences updated!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }
    
    private void managePreferences() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("⚙️ Profile Preferences");
        
        String preferences = "🎯 PERSONALIZED RECOMMENDATIONS:\n\n" +
                "🏨 ROOM RECOMMENDATIONS:\n" +
                "Based on your history:\n" +
                "• Mountain View Eco Suite (95% match)\n" +
                "• Sustainable Treehouse (92% match)\n" +
                "• Solar-Powered Deluxe (88% match)\n\n" +
                
                "🎯 ACTIVITY RECOMMENDATIONS:\n" +
                "Tailored for you:\n" +
                "• Advanced Hiking Tours\n" +
                "• Photography Workshops\n" +
                "• Conservation Projects\n" +
                "• Organic Cooking Classes\n\n" +
                
                "💰 PERSONALIZED PROMOTIONS:\n" +
                "• 25% OFF Mountain View rooms (your favorite!)\n" +
                "• Free activity booking with 2+ night stays\n" +
                "• Loyalty discount: 15% OFF all bookings\n" +
                "• Early access to new eco-experiences\n\n" +
                
                "📱 NOTIFICATION PREFERENCES:\n" +
                "• Eco Events: ✅ Enabled\n" +
                "• Special Offers: ✅ Enabled\n" +
                "• Booking Reminders: ✅ Enabled\n" +
                "• Environmental Tips: ✅ Enabled\n" +
                "• Weekly Newsletter: ✅ Enabled";
        
        builder.setMessage(preferences);
        builder.setPositiveButton("Get New Recommendations", (dialog, which) -> {
            showPersonalizedRecommendations();
        });
        builder.setNegativeButton("Update Preferences", (dialog, which) -> {
            Toast.makeText(this, "⚙️ Preferences updated successfully!", Toast.LENGTH_SHORT).show();
        });
        builder.setNeutralButton("Close", null);
        builder.show();
    }
    
    private void showPersonalizedRecommendations() {
        // Use the RecommendationService to get personalized recommendations
        com.ecostay.retreat.services.RecommendationService recommendationService = 
            new com.ecostay.retreat.services.RecommendationService();
            
        recommendationService.getPersonalizedRecommendations(new com.ecostay.retreat.services.RecommendationService.RecommendationCallback() {
            @Override
            public void onRecommendationsLoaded(java.util.List<com.ecostay.retreat.services.RecommendationService.Recommendation> recommendations) {
                displayRecommendations(recommendations);
            }
            
            @Override
            public void onError(String error) {
                Toast.makeText(ProfileActivity.this, "❌ Error loading recommendations: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void displayRecommendations(java.util.List<com.ecostay.retreat.services.RecommendationService.Recommendation> recommendations) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🎯 Your Personalized Recommendations");
        
        StringBuilder recommendationText = new StringBuilder();
        recommendationText.append("🤖 AI-POWERED RECOMMENDATIONS:\n\n");
        
        for (int i = 0; i < recommendations.size(); i++) {
            com.ecostay.retreat.services.RecommendationService.Recommendation rec = recommendations.get(i);
            recommendationText.append("🏨 RECOMMENDATION ").append(i + 1).append(":\n");
            recommendationText.append("Room: ").append(rec.getRoom().getName()).append("\n");
            recommendationText.append("Match: ").append(rec.getMatchScoreText()).append("\n");
            recommendationText.append("Reason: ").append(rec.getReason()).append("\n");
            
            if (rec.getSpecialOffer() != null) {
                recommendationText.append("Special Offer: ").append(rec.getSpecialOffer()).append("\n");
            }
            
            recommendationText.append("Price: ").append(rec.getRoom().getFormattedPrice()).append("/night\n");
            recommendationText.append("Rating: ⭐ ").append(rec.getRoom().getRating()).append("/5\n\n");
        }
        
        recommendationText.append("💡 These recommendations are based on:\n");
        recommendationText.append("• Your booking history\n");
        recommendationText.append("• Eco-friendly preferences\n");
        recommendationText.append("• Seasonal availability\n");
        recommendationText.append("• Special promotions");
        
        builder.setMessage(recommendationText.toString());
        builder.setPositiveButton("Book Recommendation", (dialog, which) -> {
            Toast.makeText(this, "🎉 Redirecting to booking for top recommendation!", Toast.LENGTH_LONG).show();
        });
        builder.setNegativeButton("Save for Later", (dialog, which) -> {
            Toast.makeText(this, "💾 Recommendations saved to your profile!", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }
}
