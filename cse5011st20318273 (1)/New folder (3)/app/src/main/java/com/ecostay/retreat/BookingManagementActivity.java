package com.ecostay.retreat;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecostay.retreat.models.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingManagementActivity extends AppCompatActivity {

    private ListView listViewBookings;
    private List<Booking> bookingList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_management);

        initializeViews();
        loadUserBookings();
    }

    private void initializeViews() {
        listViewBookings = findViewById(R.id.list_bookings);
        titleText = findViewById(R.id.title_text);
        
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        bookingList = new ArrayList<>();
    }

    private void loadUserBookings() {
        // Load sample bookings for demonstration
        loadSampleBookings();
    }

    private void loadSampleBookings() {
        bookingList.clear();
        
        // Create sample bookings
        Calendar cal = Calendar.getInstance();
        
        // Upcoming booking
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Booking upcoming = new Booking("booking1", "user1", "room1", "🌿 Eco Suite Deluxe",
                cal.getTime(), getDatePlusDays(cal.getTime(), 3), 2, 450.0, 45.0, "confirmed");
        upcoming.setSpecialRequests("Late check-in requested");
        
        // Past booking
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Booking past = new Booking("booking2", "user1", "room2", "🏡 Sustainable Cabin",
                cal.getTime(), getDatePlusDays(cal.getTime(), 2), 4, 180.0, 18.0, "completed");
        
        // Cancelled booking
        cal.add(Calendar.DAY_OF_MONTH, -60);
        Booking cancelled = new Booking("booking3", "user1", "room3", "🌸 Treehouse Deluxe",
                cal.getTime(), getDatePlusDays(cal.getTime(), 2), 2, 400.0, 40.0, "cancelled");

        bookingList.add(upcoming);
        bookingList.add(past);
        bookingList.add(cancelled);

        // Create simple adapter
        List<String> displayList = new ArrayList<>();
        for (Booking booking : bookingList) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String display = booking.getRoomName() + "\n" +
                    sdf.format(booking.getCheckInDate()) + " - " + sdf.format(booking.getCheckOutDate()) + "\n" +
                    booking.getStatusDisplayText() + " | " + booking.getFormattedPrice();
            displayList.add(display);
        }

        // Set up click listener for booking actions
        listViewBookings.setOnItemClickListener((parent, view, position, id) -> {
            Booking selectedBooking = bookingList.get(position);
            showBookingActions(selectedBooking);
        });

        Toast.makeText(this, "📋 Loaded " + bookingList.size() + " bookings", Toast.LENGTH_SHORT).show();
    }

    private void showBookingActions(Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking Actions");
        builder.setMessage("Room: " + booking.getRoomName() + "\nStatus: " + booking.getStatusDisplayText());

        if (booking.canBeCancelled()) {
            builder.setPositiveButton("Cancel Booking", (dialog, which) -> {
                cancelBooking(booking);
            });
        }

        if (booking.canBeModified()) {
            builder.setNeutralButton("Modify Booking", (dialog, which) -> {
                modifyBooking(booking);
            });
        }

        builder.setNegativeButton("View Details", (dialog, which) -> {
            showBookingDetails(booking);
        });

        builder.show();
    }

    private void cancelBooking(Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Booking");
        builder.setMessage("Are you sure you want to cancel this booking?\n\nRoom: " + booking.getRoomName());
        
        builder.setPositiveButton("Yes, Cancel", (dialog, which) -> {
            booking.setStatus("cancelled");
            Toast.makeText(this, "❌ Booking cancelled successfully", Toast.LENGTH_SHORT).show();
            loadUserBookings(); // Refresh the list
        });
        
        builder.setNegativeButton("Keep Booking", null);
        builder.show();
    }

    private void modifyBooking(Booking booking) {
        Toast.makeText(this, "🔄 Booking modification feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void showBookingDetails(Booking booking) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking Details");
        
        String details = "Room: " + booking.getRoomName() + "\n" +
                "Check-in: " + sdf.format(booking.getCheckInDate()) + "\n" +
                "Check-out: " + sdf.format(booking.getCheckOutDate()) + "\n" +
                "Guests: " + booking.getNumberOfGuests() + "\n" +
                "Total Price: $" + (int)booking.getTotalPrice() + "\n" +
                "Eco Discount: $" + (int)booking.getEcoDiscount() + "\n" +
                "Final Price: " + booking.getFormattedPrice() + "\n" +
                "Status: " + booking.getStatusDisplayText() + "\n" +
                "Nights: " + booking.getNumberOfNights();
                
        if (booking.getSpecialRequests() != null) {
            details += "\nSpecial Requests: " + booking.getSpecialRequests();
        }
        
        builder.setMessage(details);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private Date getDatePlusDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
