package com.ecostay.retreat.services;

import com.ecostay.retreat.models.RoomAvailability;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AvailabilityService {
    
    private FirebaseFirestore db;
    private Random random;

    public AvailabilityService() {
        db = FirebaseFirestore.getInstance();
        random = new Random();
    }

    public interface AvailabilityCallback {
        void onAvailabilityLoaded(List<RoomAvailability> availabilities);
        void onError(String error);
    }

    public void checkRoomAvailability(String roomId, Date checkInDate, Date checkOutDate, AvailabilityCallback callback) {
        // Generate realistic sample availability data
        List<RoomAvailability> sampleAvailabilities = generateSampleAvailability(roomId, checkInDate, checkOutDate);
        callback.onAvailabilityLoaded(sampleAvailabilities);
    }

    private List<RoomAvailability> generateSampleAvailability(String roomId, Date startDate, Date endDate) {
        List<RoomAvailability> availabilities = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        while (cal.getTime().before(endDate) || cal.getTime().equals(endDate)) {
            RoomAvailability availability = generateDayAvailability(roomId, cal.getTime());
            availabilities.add(availability);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return availabilities;
    }

    private RoomAvailability generateDayAvailability(String roomId, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        // Base room data
        double basePrice = 120.0;
        int totalUnits = 3;
        
        // Generate realistic availability patterns
        boolean isWeekend = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
                           cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        
        // Availability logic
        int availableUnits = isWeekend ? random.nextInt(2) + 1 : random.nextInt(3) + 1;
        double dynamicPrice = basePrice * (isWeekend ? 1.3 : 1.0);
        boolean isAvailable = availableUnits > 0;
        
        RoomAvailability availability = new RoomAvailability(
            roomId, date, isAvailable, availableUnits, totalUnits, dynamicPrice, basePrice
        );
        
        availability.setEcoDiscountApplicable(true);
        availability.setEcoDiscountPercentage(10.0);
        
        return availability;
    }
}
