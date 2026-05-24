package com.ecostay.retreat.models;

public class Room {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private int capacity;
    private String size;
    private boolean isEcoFriendly;
    private double ecoScore;
    private double rating;
    private String roomType;
    private boolean isAvailable;

    // Default constructor required for Firestore
    public Room() {}

    public Room(String id, String name, String description, String imageUrl, 
                double price, int capacity, String size, boolean isEcoFriendly, 
                double ecoScore, double rating, String roomType, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.capacity = capacity;
        this.size = size;
        this.isEcoFriendly = isEcoFriendly;
        this.ecoScore = ecoScore;
        this.rating = rating;
        this.roomType = roomType;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public boolean isEcoFriendly() { return isEcoFriendly; }
    public void setEcoFriendly(boolean ecoFriendly) { isEcoFriendly = ecoFriendly; }

    public double getEcoScore() { return ecoScore; }
    public void setEcoScore(double ecoScore) { this.ecoScore = ecoScore; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    // Helper methods
    public String getFormattedPrice() {
        return "$" + (int) price;
    }

    public String getCapacityText() {
        return capacity + (capacity == 1 ? " guest" : " guests");
    }

    public String getEcoScoreText() {
        return (int) (ecoScore * 100) + "% Eco";
    }
}
