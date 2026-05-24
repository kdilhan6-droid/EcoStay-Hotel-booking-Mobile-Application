package com.ecostay.retreat;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecostay.retreat.services.NotificationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationActivity extends AppCompatActivity {

    private ListView listViewNotifications;
    private TextView titleText;
    private NotificationService notificationService;
    private List<NotificationService.EcoNotification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initializeViews();
        loadNotifications();
    }

    private void initializeViews() {
        listViewNotifications = findViewById(R.id.list_notifications);
        titleText = findViewById(R.id.title_text);
        
        notificationService = new NotificationService(this);
        notificationList = new ArrayList<>();
    }

    private void loadNotifications() {
        notificationList = notificationService.getSampleNotifications();
        
        List<String> displayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
        
        for (NotificationService.EcoNotification notification : notificationList) {
            String timeStr = sdf.format(new Date(notification.getTimestamp()));
            String display = notification.getTypeIcon() + " " + notification.getTitle() + "\n" +
                    notification.getMessage() + "\n" +
                    "📅 " + timeStr;
            displayList.add(display);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_list_item_1, displayList);
        listViewNotifications.setAdapter(adapter);

        listViewNotifications.setOnItemClickListener((parent, view, position, id) -> {
            NotificationService.EcoNotification selectedNotification = notificationList.get(position);
            showNotificationDetails(selectedNotification);
        });

        Toast.makeText(this, "📢 Loaded " + notificationList.size() + " notifications", Toast.LENGTH_SHORT).show();
        
        // Setup button listeners
        setupButtonListeners();
    }
    
    private void setupButtonListeners() {
        findViewById(R.id.btn_mark_all_read).setOnClickListener(v -> {
            Toast.makeText(this, "✅ All notifications marked as read!", Toast.LENGTH_SHORT).show();
        });
        
        findViewById(R.id.btn_notification_settings).setOnClickListener(v -> {
            showNotificationSettings();
        });
    }
    
    private void showNotificationSettings() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("⚙️ Notification Settings");
        
        String settings = "📱 NOTIFICATION PREFERENCES:\n\n" +
                "🌱 ECO EVENTS:\n" +
                "• Tree planting ceremonies: ✅ ON\n" +
                "• Wildlife conservation talks: ✅ ON\n" +
                "• Sustainability workshops: ✅ ON\n\n" +
                
                "💰 SPECIAL OFFERS:\n" +
                "• Room discounts: ✅ ON\n" +
                "• Activity promotions: ✅ ON\n" +
                "• Loyalty rewards: ✅ ON\n\n" +
                
                "📅 BOOKING REMINDERS:\n" +
                "• Check-in reminders: ✅ ON\n" +
                "• Activity start times: ✅ ON\n" +
                "• Checkout notifications: ✅ ON\n\n" +
                
                "🌿 ECO TIPS:\n" +
                "• Daily sustainability tips: ✅ ON\n" +
                "• Energy saving reminders: ✅ ON\n" +
                "• Eco-friendly alternatives: ✅ ON";
        
        builder.setMessage(settings);
        builder.setPositiveButton("Save Settings", (dialog, which) -> {
            Toast.makeText(this, "💾 Notification settings saved!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showNotificationDetails(NotificationService.EcoNotification notification) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(notification.getTypeIcon() + " " + notification.getTitle());
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault());
        String timeStr = sdf.format(new Date(notification.getTimestamp()));
        
        String details = notification.getMessage() + "\n\n" +
                "📅 Received: " + timeStr + "\n" +
                "🏷️ Type: " + notification.getType().replace("_", " ").toUpperCase();
        
        builder.setMessage(details);
        
        if (notification.getType().equals("discount")) {
            builder.setPositiveButton("Use Offer", (dialog, which) -> {
                Toast.makeText(this, "🎉 Discount applied to your next booking!", Toast.LENGTH_LONG).show();
            });
        } else if (notification.getType().equals("eco_event")) {
            builder.setPositiveButton("Join Event", (dialog, which) -> {
                Toast.makeText(this, "✅ You're registered for this eco event!", Toast.LENGTH_LONG).show();
            });
        }
        
        builder.setNegativeButton("Close", null);
        builder.show();
    }
}
