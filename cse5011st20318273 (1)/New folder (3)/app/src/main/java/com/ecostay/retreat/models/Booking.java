package com.ecostay.retreat.models;

import java.util.Date;
import java.util.List;

public class Booking {
    private String id;
    private String userId;
    private String roomId;
    private String roomName;
    private Date checkInDate;
    private Date checkOutDate;
    private int numberOfGuests;
    private double totalPrice;
    private double ecoDiscount;
    private String status; // "confirmed", "cancelled", "completed", "pending"
    private Date bookingDate;
    private String specialRequests;
    private List<String> ecoPerks;
    private boolean isEcoBooking;
    private double carbonOffset;
    private int sustainabilityPoints;

    // Default constructor required for Firestore
    public Booking() {}

    public Booking(String id, String userId, String roomId, String roomName, 
                   Date checkInDate, Date checkOutDate, int numberOfGuests,
                   double totalPrice, double ecoDiscount, String status) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
        this.ecoDiscount = ecoDiscount;
        this.status = status;
        this.bookingDate = new Date();
        this.isEcoBooking = true;
        this.sustainabilityPoints = 50; // Default eco points
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public Date getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Date checkInDate) { this.checkInDate = checkInDate; }

    public Date getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Date checkOutDate) { this.checkOutDate = checkOutDate; }

    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public double getEcoDiscount() { return ecoDiscount; }
    public void setEcoDiscount(double ecoDiscount) { this.ecoDiscount = ecoDiscount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public List<String> getEcoPerks() { return ecoPerks; }
    public void setEcoPerks(List<String> ecoPerks) { this.ecoPerks = ecoPerks; }

    public boolean isEcoBooking() { return isEcoBooking; }
    public void setEcoBooking(boolean ecoBooking) { isEcoBooking = ecoBooking; }

    public double getCarbonOffset() { return carbonOffset; }
    public void setCarbonOffset(double carbonOffset) { this.carbonOffset = carbonOffset; }

    public int getSustainabilityPoints() { return sustainabilityPoints; }
    public void setSustainabilityPoints(int sustainabilityPoints) { this.sustainabilityPoints = sustainabilityPoints; }

    // Helper methods
    public long getNumberOfNights() {
        if (checkInDate != null && checkOutDate != null) {
            long diffInMillies = Math.abs(checkOutDate.getTime() - checkInDate.getTime());
            return diffInMillies / (24 * 60 * 60 * 1000);
        }
        return 0;
    }

    public double getFinalPrice() {
        return totalPrice - ecoDiscount;
    }

    public String getFormattedPrice() {
        return "$" + (int) getFinalPrice();
    }

    public String getStatusDisplayText() {
        switch (status.toLowerCase()) {
            case "confirmed": return "✅ Confirmed";
            case "cancelled": return "❌ Cancelled";
            case "completed": return "🏆 Completed";
            case "pending": return "⏳ Pending";
            default: return status;
        }
    }

    public boolean canBeCancelled() {
        return "confirmed".equals(status) || "pending".equals(status);
    }

    public boolean canBeModified() {
        return "confirmed".equals(status) && checkInDate != null && 
               checkInDate.getTime() > System.currentTimeMillis() + (24 * 60 * 60 * 1000); // 24h before
    }
}
