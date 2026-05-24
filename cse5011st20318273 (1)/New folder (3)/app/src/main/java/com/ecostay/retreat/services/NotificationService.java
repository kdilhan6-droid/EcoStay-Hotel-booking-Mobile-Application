package com.ecostay.retreat.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.ecostay.retreat.MainActivity;
import com.ecostay.retreat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotificationService {
    
    private static final String CHANNEL_ID = "eco_stay_notifications";
    private static final String CHANNEL_NAME = "EcoStay Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for eco-friendly events and offers";
    
    private Context context;
    private NotificationManager notificationManager;
    private Random random;

    public NotificationService(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.random = new Random();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendEcoEventNotification(String title, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("🌿 " + title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        notificationManager.notify(random.nextInt(1000), builder.build());
    }

    public void sendDiscountNotification(String offer, String discount) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("💚 Eco Discount Available!")
                .setContentText(offer + " - " + discount + " OFF")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("🎉 Special Eco-Friendly Offer!\n\n" + offer + "\n\nGet " + discount + " OFF when you book now!\n\n🌱 Help save the planet while saving money!"));

        notificationManager.notify(random.nextInt(1000), builder.build());
    }

    public void sendActivityReminder(String activity, String time) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("⏰ Activity Reminder")
                .setContentText("Your " + activity + " starts at " + time)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(random.nextInt(1000), builder.build());
    }

    // Sample notifications for demonstration
    public List<EcoNotification> getSampleNotifications() {
        List<EcoNotification> notifications = new ArrayList<>();
        
        notifications.add(new EcoNotification(
            "🌱 Earth Day Special Event",
            "Join our tree planting ceremony this weekend! Free for all guests.",
            "eco_event",
            System.currentTimeMillis()
        ));
        
        notifications.add(new EcoNotification(
            "💚 20% Off Eco Tours",
            "Book any eco-tour this month and get 20% off! Limited time offer.",
            "discount",
            System.currentTimeMillis() - 3600000
        ));
        
        notifications.add(new EcoNotification(
            "🦋 Wildlife Photography Workshop",
            "Learn sustainable photography techniques with our expert guides.",
            "eco_event",
            System.currentTimeMillis() - 7200000
        ));
        
        notifications.add(new EcoNotification(
            "🌿 Sustainability Tip",
            "Did you know? Our solar panels generate 80% of the resort's energy!",
            "eco_tip",
            System.currentTimeMillis() - 10800000
        ));
        
        notifications.add(new EcoNotification(
            "🏡 15% Off Eco Cabins",
            "Book our sustainable cabins and save 15%! Made from recycled materials.",
            "discount",
            System.currentTimeMillis() - 14400000
        ));

        return notifications;
    }

    public static class EcoNotification {
        private String title;
        private String message;
        private String type; // "eco_event", "discount", "eco_tip", "reminder"
        private long timestamp;

        public EcoNotification(String title, String message, String type, long timestamp) {
            this.title = title;
            this.message = message;
            this.type = type;
            this.timestamp = timestamp;
        }

        // Getters
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getType() { return type; }
        public long getTimestamp() { return timestamp; }

        public String getTypeIcon() {
            switch (type) {
                case "eco_event": return "🌱";
                case "discount": return "💚";
                case "eco_tip": return "🌿";
                case "reminder": return "⏰";
                default: return "📢";
            }
        }
    }
}
