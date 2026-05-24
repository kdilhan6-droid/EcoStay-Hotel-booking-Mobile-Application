package com.ecostay.retreat;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivitySimple extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_main);
            mAuth = FirebaseAuth.getInstance();
            
            // Show profile immediately
            showProfileDialog();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void showProfileDialog() {
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String email = currentUser != null ? currentUser.getEmail() : "Unknown";
            String name = email != null ? email.split("@")[0] : "Guest";
            
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("👤 Your EcoStay Profile");
        
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
            builder.setNegativeButton("📅 Travel Dates", (dialog, which) -> showTravelDates());
            builder.setNeutralButton("⚙️ Preferences", (dialog, which) -> showPreferences());
            builder.setOnDismissListener(dialog -> finish()); // Close activity when dialog closes
            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, "Error showing profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void showBookingHistory() {
        try {
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
            builder.setPositiveButton("Back to Profile", (dialog, which) -> showProfileDialog());
            builder.setNegativeButton("Close", (dialog, which) -> finish());
            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, "Error showing booking history", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showTravelDates() {
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
        builder.setPositiveButton("Back to Profile", (dialog, which) -> showProfileDialog());
        builder.setNegativeButton("Close", (dialog, which) -> finish());
        builder.show();
    }
    
    private void showPreferences() {
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
        builder.setPositiveButton("Back to Profile", (dialog, which) -> showProfileDialog());
        builder.setNegativeButton("Close", (dialog, which) -> finish());
        builder.show();
    }
}
