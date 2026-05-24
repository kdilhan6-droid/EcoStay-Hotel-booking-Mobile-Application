package com.ecostay.retreat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RoomsActivity extends AppCompatActivity {

    private ListView listView;
    private RoomAdapter adapter;
    private List<RoomAdapter.RoomItem> roomList;
    private List<RoomAdapter.RoomItem> allRooms; // Store all rooms for filtering
    private FirebaseFirestore db;
    private TextView titleText;
    private EditText searchEditText;
    private Button searchButton, sortPriceBtn, sortRatingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        db = FirebaseFirestore.getInstance();
        
        titleText = findViewById(R.id.titleText);
        listView = findViewById(R.id.roomsListView);
        
        // Initialize search components
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        
        // Initialize sorting buttons
        sortPriceBtn = findViewById(R.id.sortPriceBtn);
        sortRatingBtn = findViewById(R.id.sortRatingBtn);
        
        roomList = new ArrayList<>();
        allRooms = new ArrayList<>();
        adapter = new RoomAdapter(this, roomList);
        listView.setAdapter(adapter);

        setupSearchFunctionality();
        setupSortingButtons();
        setupRoomClickListener();
        loadRooms();
    }

    private void loadRooms() {
        // Load sample rooms directly for demo purposes
        loadSampleRooms();
    }
    
    private void loadSampleRooms() {
        roomList.clear();
        allRooms.clear();
        
        // DILHAN'S UNIQUE MOUNTAIN ADVENTURE ROOMS - Completely Different Theme
        allRooms.add(new RoomAdapter.RoomItem("⛰️ Summit Explorer Lodge", "$180/night", "Cliff-edge location, Extreme views"));
        allRooms.add(new RoomAdapter.RoomItem("🔥 Basecamp Warrior Hut", "$95/night", "Rugged design, Adventure gear included"));
        allRooms.add(new RoomAdapter.RoomItem("🏕️ Expedition Command Center", "$140/night", "High-tech, Weather monitoring station"));
        allRooms.add(new RoomAdapter.RoomItem("🧗 Climber's Refuge Elite", "$220/night", "Rock climbing wall, Professional equipment"));
        allRooms.add(new RoomAdapter.RoomItem("🎒 Backpacker's Den Basic", "$65/night", "Minimalist survival, Essential gear only"));
        allRooms.add(new RoomAdapter.RoomItem("🏂 Alpine Storm Shelter", "$190/night", "Weather-proof, Emergency communications"));
        allRooms.add(new RoomAdapter.RoomItem("⚡ Thunder Peak Observatory", "$170/night", "Lightning protection, Storm watching"));
        allRooms.add(new RoomAdapter.RoomItem("🌪️ Wilderness Survival Pod", "$75/night", "Basic shelter, Survival training included"));
        allRooms.add(new RoomAdapter.RoomItem("🏆 Champion's Victory Suite", "$280/night", "Luxury after conquest, Trophy display"));
        allRooms.add(new RoomAdapter.RoomItem("💪 Rookie Adventure Bunk", "$55/night", "Starter level, Training equipment"));
        
        // Initially show all rooms
        roomList.addAll(allRooms);
        adapter.notifyDataSetChanged();
        
        Toast.makeText(this, "📱 Loaded sample eco-rooms for demonstration!", Toast.LENGTH_SHORT).show();
    }
    
    private void setupSearchFunctionality() {
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().toLowerCase().trim();
            searchRooms(query);
        });
    }
    
    private void searchRooms(String query) {
        roomList.clear();
        
        if (query.isEmpty()) {
            roomList.addAll(allRooms);
            titleText.setText("⛰️ All Adventure Shelters");
        } else {
            for (RoomAdapter.RoomItem room : allRooms) {
                String roomText = (room.getName() + " " + room.getFeatures() + " " + room.getPrice()).toLowerCase();
                if (roomText.contains(query)) {
                    roomList.add(room);
                }
            }
            titleText.setText("🔍 Search Results: " + roomList.size() + " shelters found");
        }
        
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Found " + roomList.size() + " matching shelters", Toast.LENGTH_SHORT).show();
    }
    
    private void filterRooms(String category) {
        roomList.clear();
        
        if (category.equals("All")) {
            roomList.addAll(allRooms);
            titleText.setText("⛰️ All Adventure Shelters");
        } else if (category.equals("Eco")) {
            for (RoomAdapter.RoomItem room : allRooms) {
                String roomText = (room.getName() + " " + room.getFeatures()).toLowerCase();
                if (roomText.contains("explorer") || roomText.contains("basecamp") || 
                    roomText.contains("expedition") || roomText.contains("survival")) {
                    roomList.add(room);
                }
            }
            titleText.setText("🏕️ Expedition Shelters");
        } else if (category.equals("Luxury")) {
            for (RoomAdapter.RoomItem room : allRooms) {
                String roomText = (room.getName() + " " + room.getFeatures()).toLowerCase();
                if (roomText.contains("elite") || roomText.contains("suite") || 
                    roomText.contains("champion") || roomText.contains("observatory")) {
                    roomList.add(room);
                }
            }
            titleText.setText("🏆 Elite Adventure Suites");
        } else if (category.equals("Budget")) {
            for (RoomAdapter.RoomItem room : allRooms) {
                String roomText = (room.getName() + " " + room.getPrice() + " " + room.getFeatures()).toLowerCase();
                if (roomText.contains("basic") || roomText.contains("rookie") || 
                    roomText.contains("den") || roomText.contains("$55") || roomText.contains("$65")) {
                    roomList.add(room);
                }
            }
            titleText.setText("💪 Rookie Adventure Bunks");
        }
        
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Showing " + roomList.size() + " " + category + " rooms", Toast.LENGTH_SHORT).show();
    }
    
    // Price filter buttons removed - using search instead
    
    private void setupSortingButtons() {
        sortPriceBtn.setOnClickListener(v -> sortRooms("price"));
        sortRatingBtn.setOnClickListener(v -> sortRooms("rating"));
    }
    
    private void filterByPriceRange(String priceRange) {
        roomList.clear();
        
        if (priceRange.equals("All")) {
            roomList.addAll(allRooms);
            titleText.setText("🏨 All Price Ranges");
        } else {
            for (RoomAdapter.RoomItem room : allRooms) {
                double price = extractPrice(room.getPrice());
                
                if (priceRange.equals("50-100") && price >= 50 && price <= 100) {
                    roomList.add(room);
                } else if (priceRange.equals("100-200") && price > 100 && price <= 200) {
                    roomList.add(room);
                } else if (priceRange.equals("200+") && price > 200) {
                    roomList.add(room);
                }
            }
            titleText.setText("💰 $" + priceRange + " Price Range");
        }
        
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Showing " + roomList.size() + " rooms in $" + priceRange + " range", Toast.LENGTH_SHORT).show();
    }
    
    private void sortRooms(String sortBy) {
        if (sortBy.equals("price")) {
            Collections.sort(roomList, new Comparator<RoomAdapter.RoomItem>() {
                @Override
                public int compare(RoomAdapter.RoomItem r1, RoomAdapter.RoomItem r2) {
                    double price1 = extractPrice(r1.getPrice());
                    double price2 = extractPrice(r2.getPrice());
                    return Double.compare(price1, price2);
                }
            });
            titleText.setText("📊 Sorted by Price (Low to High)");
        } else if (sortBy.equals("rating")) {
            Collections.sort(roomList, new Comparator<RoomAdapter.RoomItem>() {
                @Override
                public int compare(RoomAdapter.RoomItem r1, RoomAdapter.RoomItem r2) {
                    // Sort by luxury level (higher luxury = higher rating)
                    int rating1 = getRoomRating(r1);
                    int rating2 = getRoomRating(r2);
                    return Integer.compare(rating2, rating1); // Descending order
                }
            });
            titleText.setText("⭐ Sorted by Rating (High to Low)");
        }
        
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "✅ Rooms sorted by " + sortBy, Toast.LENGTH_SHORT).show();
    }
    
    private double extractPrice(String priceString) {
        // Extract numeric price from strings like "$110/night"
        try {
            String numericPart = priceString.replaceAll("[^0-9]", "");
            return Double.parseDouble(numericPart);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private int getRoomRating(RoomAdapter.RoomItem room) {
        String roomText = (room.getName() + " " + room.getFeatures()).toLowerCase();
        if (roomText.contains("luxury") || roomText.contains("suite") || roomText.contains("premium")) {
            return 5;
        } else if (roomText.contains("deluxe") || roomText.contains("treehouse")) {
            return 4;
        } else if (roomText.contains("eco") || roomText.contains("solar")) {
            return 3;
        } else if (roomText.contains("standard") || roomText.contains("garden")) {
            return 2;
        } else {
            return 1;
        }
    }
    
    private void setupRoomClickListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position < roomList.size()) {
                RoomAdapter.RoomItem selectedRoom = roomList.get(position);
                
                // Enhanced room selection with booking dialog
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("🏨 Room Selected");
                builder.setMessage("You selected:\n" + selectedRoom.getName() + "\n" + selectedRoom.getPrice() + "\n\n" + selectedRoom.getFeatures() + "\n\nWould you like to:\n• View room details\n• Book this room\n• Add to favorites");
                builder.setPositiveButton("Book Now", (dialog, which) -> {
                    showRoomBookingCalendar(selectedRoom);
                });
                builder.setNegativeButton("View Details", (dialog, which) -> {
                    Toast.makeText(this, "📋 Viewing details for " + selectedRoom.getName(), Toast.LENGTH_SHORT).show();
                });
                builder.setNeutralButton("Cancel", null);
                builder.show();
            }
        });
    }
    
    private void showBookingDialog(RoomAdapter.RoomItem selectedRoom) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("📅 Book " + selectedRoom.getName());
        
        String bookingInfo = "🏔️ ADVENTURE BOOKING:\n\n" +
                "⛰️ Shelter: " + selectedRoom.getName() + "\n" +
                "💰 Rate: " + selectedRoom.getPrice() + "\n" +
                "🎒 Gear: " + selectedRoom.getFeatures() + "\n\n" +
                "🎯 ADVENTURE PACKAGE:\n" +
                "• Duration: 2 nights\n" +
                "• Total Cost: $" + calculateTotal(selectedRoom) + "\n\n" +
                "🏕️ INCLUDED:\n" +
                "• Adventure gear rental\n" +
                "• Mountain guide access\n" +
                "• Emergency communication\n" +
                "• Weather updates";
        
        builder.setMessage(bookingInfo);
        builder.setPositiveButton("Confirm Booking", (dialog, which) -> {
            confirmRoomBooking(selectedRoom);
        });
        builder.setNegativeButton("Change Dates", (dialog, which) -> {
            showDatePicker(selectedRoom);
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }
    
    private int calculateTotal(RoomAdapter.RoomItem room) {
        try {
            String priceStr = room.getPrice().split("\\$")[1].split("/")[0];
            return Integer.parseInt(priceStr) * 2; // 2 nights
        } catch (Exception e) {
            return 200; // Default fallback
        }
    }
    
    private void showDatePicker(RoomAdapter.RoomItem selectedRoom) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("📅 Select Dates");
        
        String dateOptions = "🗓️ AVAILABLE DATES:\n\n" +
                "📅 JANUARY 2025:\n" +
                "• Jan 10-12 (2 nights) - Available ✅\n" +
                "• Jan 15-17 (2 nights) - Available ✅\n" +
                "• Jan 20-22 (2 nights) - Available ✅\n" +
                "• Jan 25-27 (2 nights) - Available ✅\n\n" +
                "📅 FEBRUARY 2025:\n" +
                "• Feb 5-7 (2 nights) - Available ✅\n" +
                "• Feb 12-14 (2 nights) - Available ✅\n" +
                "• Feb 19-21 (2 nights) - Available ✅\n\n" +
                "❌ UNAVAILABLE:\n" +
                "• Jan 13-14 (Maintenance)\n" +
                "• Jan 18-19 (Fully booked)\n" +
                "• Feb 1-4 (Eco renovation)\n\n" +
                "💡 TIP: Book 3+ nights for additional eco-discounts!";
        
        builder.setMessage(dateOptions);
        builder.setPositiveButton("Select Jan 10-12", (dialog, which) -> {
            Toast.makeText(this, "📅 Dates selected: Jan 10-12, 2025", Toast.LENGTH_SHORT).show();
            showBookingDialog(selectedRoom);
        });
        builder.setNegativeButton("Select Other", (dialog, which) -> {
            Toast.makeText(this, "📅 Please contact reception for custom dates", Toast.LENGTH_LONG).show();
        });
        builder.show();
    }
    
    private void confirmRoomBooking(RoomAdapter.RoomItem selectedRoom) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("✅ Booking Confirmed!");
        
        String confirmation = "🎉 BOOKING CONFIRMATION:\n\n" +
                "📋 BOOKING ID: ECO-" + System.currentTimeMillis() % 10000 + "\n" +
                "🏨 Room: " + selectedRoom.getName() + "\n" +
                "📅 Dates: Jan 10-12, 2025\n" +
                "💰 Total: $" + (calculateTotal(selectedRoom) - 20) + "\n\n" +
                "📧 CONFIRMATION SENT TO:\n" +
                "• Your registered email\n" +
                "• SMS to your phone\n\n" +
                "🌿 ECO-PERKS ACTIVATED:\n" +
                "• Carbon offset certificate\n" +
                "• Organic welcome basket\n" +
                "• Free nature tour voucher\n" +
                "• Sustainability points: +50\n\n" +
                "📱 NEXT STEPS:\n" +
                "• Check-in: 3:00 PM\n" +
                "• Eco-orientation: 4:00 PM\n" +
                "• Welcome dinner: 7:00 PM\n\n" +
                "Thank you for choosing sustainable travel! 🌱";
        
        builder.setMessage(confirmation);
        builder.setPositiveButton("View My Bookings", (dialog, which) -> {
            Toast.makeText(this, "📋 Opening booking management...", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Done", (dialog, which) -> {
            Toast.makeText(this, "🎉 Booking saved! Welcome to EcoStay!", Toast.LENGTH_LONG).show();
        });
        builder.show();
    }
    
    private void showRoomBookingCalendar(RoomAdapter.RoomItem selectedRoom) {
        // Show DatePicker for room booking
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String checkInDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                    showCheckOutDatePicker(selectedRoom, checkInDate);
                },
                year, month, day
        );

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        
        // Set maximum date to 1 year from now
        java.util.Calendar maxDate = java.util.Calendar.getInstance();
        maxDate.add(java.util.Calendar.YEAR, 1);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.setTitle("📅 Select Check-in Date");
        datePickerDialog.show();
    }
    
    private void showCheckOutDatePicker(RoomAdapter.RoomItem selectedRoom, String checkInDate) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String checkOutDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                    showBookingDialog(selectedRoom, checkInDate, checkOutDate);
                },
                year, month, day
        );

        // Set minimum date to tomorrow
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        
        datePickerDialog.setTitle("📅 Select Check-out Date");
        datePickerDialog.show();
    }
    
    private void showBookingDialog(RoomAdapter.RoomItem selectedRoom, String checkInDate, String checkOutDate) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("📅 Confirm Room Booking");
        
        // Calculate nights
        int nights = 2; // Simplified calculation
        int totalPrice = calculateTotal(selectedRoom);
        
        String bookingInfo = "🏨 ROOM BOOKING CONFIRMATION:\n\n" +
                "📋 BOOKING DETAILS:\n" +
                "• Room: " + selectedRoom.getName() + "\n" +
                "• Check-in: " + checkInDate + "\n" +
                "• Check-out: " + checkOutDate + "\n" +
                "• Duration: " + nights + " nights\n\n" +
                "💰 PRICING:\n" +
                "• Room Rate: " + selectedRoom.getPrice() + "\n" +
                "• Total (" + nights + " nights): $" + totalPrice + "\n" +
                "• Eco Discount: -$20\n" +
                "• Final Total: $" + (totalPrice - 20) + "\n\n" +
                "🌿 ECO BENEFITS INCLUDED:\n" +
                "• Carbon offset for your stay\n" +
                "• Organic welcome amenities\n" +
                "• Solar-powered room features\n" +
                "• Complimentary eco-tour\n\n" +
                "Confirm your booking?";
        
        builder.setMessage(bookingInfo);
        builder.setPositiveButton("Confirm Booking", (dialog, which) -> {
            finalizeRoomBooking(selectedRoom, checkInDate, checkOutDate);
        });
        builder.setNegativeButton("Change Dates", (dialog, which) -> {
            showRoomBookingCalendar(selectedRoom);
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }
    
    private void finalizeRoomBooking(RoomAdapter.RoomItem selectedRoom, String checkInDate, String checkOutDate) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🎉 Room Booking Confirmed!");
        
        String confirmation = "✅ ROOM SUCCESSFULLY BOOKED!\n\n" +
                "📋 CONFIRMATION ID: ROOM-" + System.currentTimeMillis() % 10000 + "\n" +
                "🏨 Room: " + selectedRoom.getName() + "\n" +
                "📅 Check-in: " + checkInDate + "\n" +
                "📅 Check-out: " + checkOutDate + "\n" +
                "💰 Total Paid: $" + (calculateTotal(selectedRoom) - 20) + "\n\n" +
                "📧 CONFIRMATION SENT TO:\n" +
                "• Your registered email\n" +
                "• SMS to your phone\n\n" +
                "🎯 NEXT STEPS:\n" +
                "• Check-in instructions sent\n" +
                "• Eco-activity recommendations\n" +
                "• Sustainability guide included\n\n" +
                "Thank you for choosing EcoStay Retreat! 🌿";
        
        builder.setMessage(confirmation);
        builder.setPositiveButton("View Booking", (dialog, which) -> {
            Toast.makeText(this, "📱 Redirecting to booking management...", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Book Another Room", (dialog, which) -> {
            Toast.makeText(this, "🏨 Ready for another booking!", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }
}
