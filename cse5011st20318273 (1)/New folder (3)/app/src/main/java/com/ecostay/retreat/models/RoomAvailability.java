package com.ecostay.retreat.models;

import java.util.Date;
import java.util.List;

public class RoomAvailability {
    private String roomId;
    private Date date;
    private boolean isAvailable;
    private int availableUnits;
    private int totalUnits;
    private double dynamicPrice;
    private double basePrice;
    private String unavailabilityReason; // "booked", "maintenance", "seasonal_closure"
    private List<String> bookedTimeSlots;
    private boolean isEcoDiscountApplicable;
    private double ecoDiscountPercentage;

    // Default constructor required for Firestore
    public RoomAvailability() {}

    public RoomAvailability(String roomId, Date date, boolean isAvailable, 
                           int availableUnits, int totalUnits, double dynamicPrice, double basePrice) {
        this.roomId = roomId;
        this.date = date;
        this.isAvailable = isAvailable;
        this.availableUnits = availableUnits;
        this.totalUnits = totalUnits;
        this.dynamicPrice = dynamicPrice;
        this.basePrice = basePrice;
        this.isEcoDiscountApplicable = true;
        this.ecoDiscountPercentage = 10.0; // Default 10% eco discount
    }

    // Getters and Setters
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public int getAvailableUnits() { return availableUnits; }
    public void setAvailableUnits(int availableUnits) { this.availableUnits = availableUnits; }

    public int getTotalUnits() { return totalUnits; }
    public void setTotalUnits(int totalUnits) { this.totalUnits = totalUnits; }

    public double getDynamicPrice() { return dynamicPrice; }
    public void setDynamicPrice(double dynamicPrice) { this.dynamicPrice = dynamicPrice; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public String getUnavailabilityReason() { return unavailabilityReason; }
    public void setUnavailabilityReason(String unavailabilityReason) { this.unavailabilityReason = unavailabilityReason; }

    public List<String> getBookedTimeSlots() { return bookedTimeSlots; }
    public void setBookedTimeSlots(List<String> bookedTimeSlots) { this.bookedTimeSlots = bookedTimeSlots; }

    public boolean isEcoDiscountApplicable() { return isEcoDiscountApplicable; }
    public void setEcoDiscountApplicable(boolean ecoDiscountApplicable) { isEcoDiscountApplicable = ecoDiscountApplicable; }

    public double getEcoDiscountPercentage() { return ecoDiscountPercentage; }
    public void setEcoDiscountPercentage(double ecoDiscountPercentage) { this.ecoDiscountPercentage = ecoDiscountPercentage; }

    // Helper methods
    public double getEcoDiscountedPrice() {
        if (isEcoDiscountApplicable) {
            return dynamicPrice * (1 - ecoDiscountPercentage / 100);
        }
        return dynamicPrice;
    }

    public String getAvailabilityStatus() {
        if (!isAvailable) {
            switch (unavailabilityReason) {
                case "booked": return "❌ Fully Booked";
                case "maintenance": return "🔧 Under Maintenance";
                case "seasonal_closure": return "🌨️ Seasonal Closure";
                default: return "❌ Unavailable";
            }
        } else if (availableUnits == 0) {
            return "❌ No Units Available";
        } else if (availableUnits <= 2) {
            return "🟡 Limited Availability (" + availableUnits + " left)";
        } else {
            return "✅ Available (" + availableUnits + " units)";
        }
    }

    public String getPriceDisplayText() {
        if (isEcoDiscountApplicable && ecoDiscountPercentage > 0) {
            return "$" + (int)getEcoDiscountedPrice() + " (was $" + (int)dynamicPrice + ")";
        }
        return "$" + (int)dynamicPrice;
    }

    public boolean isHighDemand() {
        return availableUnits <= 2 && totalUnits > 2;
    }

    public boolean isPeakPricing() {
        return dynamicPrice > basePrice * 1.2; // 20% above base price
    }

    public double getOccupancyRate() {
        if (totalUnits == 0) return 0;
        return ((double)(totalUnits - availableUnits) / totalUnits) * 100;
    }
}
