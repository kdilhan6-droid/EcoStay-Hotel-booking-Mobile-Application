package com.ecostay.retreat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivitiesActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> activityList;
    private FirebaseFirestore db;
    private TextView titleText;
    private Button calendarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        db = FirebaseFirestore.getInstance();
        
        titleText = findViewById(R.id.titleText);
        // Initialize activity buttons
        Button hikingButton = findViewById(R.id.hikingButton);
        Button climbingButton = findViewById(R.id.climbingButton);
        Button campingButton = findViewById(R.id.campingButton);
        Button survivalButton = findViewById(R.id.survivalButton);
        Button expeditionButton = findViewById(R.id.expeditionButton);
        Button weatherButton = findViewById(R.id.weatherButton);
        Button allActivitiesButton = findViewById(R.id.allActivitiesButton);
        
        // Setup button click listeners
        setupActivityButtons(hikingButton, climbingButton, campingButton, survivalButton, expeditionButton, weatherButton, allActivitiesButton);
    }

    private void setupActivityButtons(Button hiking, Button climbing, Button camping, Button survival, Button expedition, Button weather, Button allActivities) {
        hiking.setOnClickListener(v -> Toast.makeText(this, "🥾 Mountain Hiking selected!", Toast.LENGTH_SHORT).show());
        climbing.setOnClickListener(v -> Toast.makeText(this, "🧗 Rock Climbing selected!", Toast.LENGTH_SHORT).show());
        camping.setOnClickListener(v -> Toast.makeText(this, "🏕️ Wilderness Camping selected!", Toast.LENGTH_SHORT).show());
        survival.setOnClickListener(v -> Toast.makeText(this, "🎯 Survival Training selected!", Toast.LENGTH_SHORT).show());
        expedition.setOnClickListener(v -> Toast.makeText(this, "⛰️ Peak Expedition selected!", Toast.LENGTH_SHORT).show());
        weather.setOnClickListener(v -> Toast.makeText(this, "🌩️ Storm Watching selected!", Toast.LENGTH_SHORT).show());
        allActivities.setOnClickListener(v -> Toast.makeText(this, "📋 Viewing all adventure activities!", Toast.LENGTH_SHORT).show());
    }

    private void loadActivities() {
        // Load sample activities directly for demo purposes
        loadSampleActivities();
    }
    
    private void setupCalendarButton() {
        calendarButton.setOnClickListener(v -> showBookingCalendar());
    }
    
    private void showBookingCalendar() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("📅 Activity Booking Calendar");
        
        String calendarInfo = "🗓️ JANUARY 2025 AVAILABILITY:\n\n" +
                "📅 AVAILABLE DATES:\n" +
                "• Jan 10 (Thu) - 🥾 Nature Hike (8AM, 2PM)\n" +
                "• Jan 11 (Fri) - 🚴‍♂️ Bike Tour (9AM, 3PM)\n" +
                "• Jan 12 (Sat) - 🦋 Photography (10AM, 2PM)\n" +
                "• Jan 13 (Sun) - 🌿 Sustainability Tour (11AM)\n" +
                "• Jan 15 (Tue) - 🧘‍♀️ Yoga Session (7AM, 6PM)\n" +
                "• Jan 17 (Thu) - 🎣 Eco Fishing (6AM, 4PM)\n" +
                "• Jan 20 (Sun) - 🌅 Sunrise Meditation (6AM)\n\n" +
                "❌ FULLY BOOKED:\n" +
                "• Jan 14 (Mon) - All activities\n" +
                "• Jan 16 (Wed) - Photography workshop\n" +
                "• Jan 18 (Fri) - Bike tours\n\n" +
                "📝 BOOKING NOTES:\n" +
                "• Book 24h in advance\n" +
                "• Weather dependent\n" +
                "• Group discounts available\n" +
                "• Equipment provided";
        
        builder.setMessage(calendarInfo);
        builder.setPositiveButton("Book Time Slot", (dialog, which) -> {
            showInteractiveCalendar();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }
    
    private void loadSampleActivities() {
        // Enhanced sample activities with better descriptions
        activityList.add("🥾 Guided Nature Hike - 2 hours (Moderate difficulty)");
        activityList.add("🚴‍♂️ Eco-Friendly Bike Tour - 3 hours (Easy difficulty)");
        activityList.add("🦋 Wildlife Photography Workshop - 4 hours (All levels)");
        activityList.add("🌱 Organic Farming Experience - Full day (Easy difficulty)");
        activityList.add("🏞️ Sustainable Forest Walk - 1.5 hours (Easy difficulty)");
        activityList.add("🐦 Bird Watching Tour - 2.5 hours (Easy difficulty)");
        activityList.add("🌿 Sustainability Workshop - 2 hours (Educational)");
        activityList.add("🏔️ Mountain Climbing - 6 hours (Advanced difficulty)");
        adapter.notifyDataSetChanged();
        
        Toast.makeText(this, "📱 Loaded sample eco-activities for demonstration!", Toast.LENGTH_SHORT).show();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedActivity = activityList.get(position);
            showActivityBookingDialog(selectedActivity);
        });
    }
    
    private void showInteractiveCalendar() {
        // Show DatePicker for activity booking
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy (EEE)", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());
                    
                    showTimeSlotSelection(formattedDate, selectedDate.getTime());
                }, year, month, day);

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        
        // Set maximum date to 3 months from now
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 3);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        
        datePickerDialog.setTitle("📅 Select Activity Date");
        datePickerDialog.show();
    }
    
    private void showTimeSlotSelection(String selectedDate, Date date) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🕐 Available Time Slots");
        
        String timeSlotInfo = "📅 SELECTED DATE: " + selectedDate + "\n\n" +
                "🟢 AVAILABLE TIME SLOTS:\n\n" +
                "🌅 MORNING SLOTS:\n" +
                "• 8:00 AM - Nature Hike (2 spots left)\n" +
                "• 9:00 AM - Bike Tour (3 spots left)\n" +
                "• 10:00 AM - Photography Workshop (4 spots left)\n\n" +
                "🌞 AFTERNOON SLOTS:\n" +
                "• 2:00 PM - Sustainability Tour (5 spots left)\n" +
                "• 3:00 PM - Wildlife Watching (2 spots left)\n" +
                "• 4:00 PM - Eco Fishing (3 spots left)\n\n" +
                "🌅 EVENING SLOTS:\n" +
                "• 6:00 PM - Sunset Meditation (6 spots left)\n" +
                "• 7:00 PM - Night Photography (1 spot left)\n\n" +
                "💡 BOOKING NOTES:\n" +
                "• Weather dependent activities\n" +
                "• Equipment provided\n" +
                "• Group discounts available\n\n" +
                "Select your preferred time slot:";
        
        builder.setMessage(timeSlotInfo);
        builder.setPositiveButton("Book Morning Slot", (dialog, which) -> {
            bookTimeSlot("Morning Activity", selectedDate);
        });
        builder.setNegativeButton("Book Afternoon Slot", (dialog, which) -> {
            bookTimeSlot("Afternoon Activity", selectedDate);
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }
    
    
    private void showMoreTimeSlots() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("📅 More Available Time Slots");
        
        String moreSlots = "🗓️ ADDITIONAL BOOKING OPTIONS:\n\n" +
                "📅 FEBRUARY 2025:\n" +
                "🟢 FEB 5 (WED) - Multiple slots available\n" +
                "🟢 FEB 12 (WED) - Valentine's special activities\n" +
                "🟢 FEB 19 (WED) - Winter photography tours\n\n" +
                "📅 MARCH 2025:\n" +
                "🟢 MAR 20 (THU) - Spring equinox celebration\n" +
                "🟢 MAR 27 (THU) - Wildflower hikes begin\n\n" +
                "🎯 SPECIAL ACTIVITY PACKAGES:\n" +
                "• 3-Day Photography Intensive\n" +
                "• Weekend Eco-Adventure Package\n" +
                "• Couples Nature Retreat\n" +
                "• Family Sustainability Workshop\n\n" +
                "🎁 SEASONAL OFFERS:\n" +
                "• Early Bird: 20% OFF March bookings\n" +
                "• Group Rate: 15% OFF 4+ people\n" +
                "• Loyalty: 10% OFF repeat bookings\n\n" +
                "Contact reception for custom scheduling!";
        
        builder.setMessage(moreSlots);
        builder.setPositiveButton("Book Package", (dialog, which) -> {
            Toast.makeText(this, "📞 Connecting to reception for package booking...", Toast.LENGTH_LONG).show();
        });
        builder.setNegativeButton("Back to Calendar", (dialog, which) -> {
            showInteractiveCalendar();
        });
        builder.show();
    }
    
    private void bookTimeSlot(String activity, String timeSlot) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("✅ Confirm Activity Booking");
        
        String confirmation = "🎯 ACTIVITY BOOKING CONFIRMATION:\n\n" +
                "📋 BOOKING DETAILS:\n" +
                "• Activity: " + activity + "\n" +
                "• Date & Time: " + timeSlot + "\n" +
                "• Duration: 2-4 hours\n" +
                "• Difficulty: Suitable for your level\n\n" +
                "💰 PRICING:\n" +
                "• Base Price: $45/person\n" +
                "• Eco Discount: -$5\n" +
                "• Equipment Included: ✅\n" +
                "• Total: $40/person\n\n" +
                "🌿 INCLUDED BENEFITS:\n" +
                "• Professional eco-guide\n" +
                "• All necessary equipment\n" +
                "• Organic snacks & water\n" +
                "• Digital photo memories\n" +
                "• Sustainability certificate\n\n" +
                "📝 BOOKING POLICIES:\n" +
                "• Free cancellation 24h before\n" +
                "• Weather protection guarantee\n" +
                "• Small group sizes (max 8 people)\n\n" +
                "Confirm your booking?";
        
        builder.setMessage(confirmation);
        builder.setPositiveButton("Confirm Booking", (dialog, which) -> {
            finalizeActivityBooking(activity, timeSlot);
        });
        builder.setNegativeButton("Change Time", (dialog, which) -> {
            showInteractiveCalendar();
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }
    
    private void finalizeActivityBooking(String activity, String timeSlot) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🎉 Booking Confirmed!");
        
        String finalConfirmation = "✅ ACTIVITY SUCCESSFULLY BOOKED!\n\n" +
                "📋 CONFIRMATION ID: ACT-" + System.currentTimeMillis() % 10000 + "\n" +
                "🎯 Activity: " + activity + "\n" +
                "📅 Scheduled: " + timeSlot + "\n" +
                "💰 Paid: $40 (Eco-discount applied)\n\n" +
                "📧 CONFIRMATIONS SENT TO:\n" +
                "• Your email address\n" +
                "• SMS reminder 1 day before\n" +
                "• Calendar invitation attached\n\n" +
                "🎒 PREPARATION CHECKLIST:\n" +
                "• Comfortable hiking shoes\n" +
                "• Weather-appropriate clothing\n" +
                "• Reusable water bottle\n" +
                "• Camera (optional - we provide one)\n\n" +
                "📍 MEETING POINT:\n" +
                "• EcoStay Reception Lobby\n" +
                "• Arrive 15 minutes early\n" +
                "• Look for guide with green EcoStay shirt\n\n" +
                "🌱 ECO-POINTS EARNED: +25 points\n" +
                "🏆 Sustainability Level: Advanced\n\n" +
                "Thank you for choosing eco-friendly adventures! 🌿";
        
        builder.setMessage(finalConfirmation);
        builder.setPositiveButton("Add to Calendar", (dialog, which) -> {
            Toast.makeText(this, "📅 Activity added to your calendar!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("View My Bookings", (dialog, which) -> {
            Toast.makeText(this, "📋 Opening activity bookings...", Toast.LENGTH_SHORT).show();
        });
        builder.setNeutralButton("Done", null);
        builder.show();
    }
    
    private void showActivityBookingDialog(String selectedActivity) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🎯 Book " + selectedActivity.split(" - ")[0]);
        
        String activityInfo = "🎯 ACTIVITY DETAILS:\n\n" +
                "📋 Selected: " + selectedActivity + "\n\n" +
                "📅 NEXT AVAILABLE SLOTS:\n" +
                "• Tomorrow 8:00 AM (2 spots left)\n" +
                "• Tomorrow 2:00 PM (5 spots left)\n" +
                "• Day after 10:00 AM (3 spots left)\n\n" +
                "💰 PRICING:\n" +
                "• Standard Rate: $45/person\n" +
                "• With Eco Discount: $40/person\n" +
                "• Group Rate (4+): $35/person\n\n" +
                "🌿 WHAT'S INCLUDED:\n" +
                "• Expert eco-guide\n" +
                "• All equipment provided\n" +
                "• Organic refreshments\n" +
                "• Photo memories\n" +
                "• Sustainability certificate\n\n" +
                "Ready to book this activity?";
        
        builder.setMessage(activityInfo);
        builder.setPositiveButton("Book Now", (dialog, which) -> {
            showInteractiveCalendar();
        });
        builder.setNegativeButton("View Details", (dialog, which) -> {
            Toast.makeText(this, "📋 Showing detailed info for " + selectedActivity.split(" - ")[0], Toast.LENGTH_LONG).show();
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }
}
