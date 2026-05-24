package com.ecostay.retreat.services;

import com.ecostay.retreat.models.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecommendationService {
    
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Random random;

    public RecommendationService() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        random = new Random();
    }

    public interface RecommendationCallback {
        void onRecommendationsLoaded(List<Recommendation> recommendations);
        void onError(String error);
    }

    public static class Recommendation {
        private Room room;
        private double matchScore;
        private String reason;
        private double discountPercentage;
        private String specialOffer;
        private boolean isPersonalized;

        public Recommendation(Room room, double matchScore, String reason) {
            this.room = room;
            this.matchScore = matchScore;
            this.reason = reason;
        }

        // Getters and setters
        public Room getRoom() { return room; }
        public void setRoom(Room room) { this.room = room; }

        public double getMatchScore() { return matchScore; }
        public void setMatchScore(double matchScore) { this.matchScore = matchScore; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }

        public double getDiscountPercentage() { return discountPercentage; }
        public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

        public String getSpecialOffer() { return specialOffer; }
        public void setSpecialOffer(String specialOffer) { this.specialOffer = specialOffer; }

        public boolean isPersonalized() { return isPersonalized; }
        public void setPersonalized(boolean personalized) { isPersonalized = personalized; }

        public String getMatchScoreText() {
            return (int)(matchScore * 100) + "% match";
        }
    }

    public void getPersonalizedRecommendations(RecommendationCallback callback) {
        // Generate sample recommendations
        List<Recommendation> recommendations = generateSampleRecommendations();
        callback.onRecommendationsLoaded(recommendations);
    }

    private List<Recommendation> generateSampleRecommendations() {
        List<Recommendation> recommendations = new ArrayList<>();
        
        // Create sample rooms for recommendations
        Room ecoSuite = new Room("eco_suite", "🌿 Eco Suite Deluxe", 
            "Solar-powered luxury with organic amenities", "", 
            150.0, 2, "Large", true, 9.5, 4.8, "Suite", true);
            
        Room treehouse = new Room("treehouse", "🌸 Sustainable Treehouse", 
            "Carbon neutral tree-top experience", "", 
            200.0, 2, "Medium", true, 9.8, 4.9, "Treehouse", true);
            
        Room cabin = new Room("cabin", "🏡 Eco Cabin", 
            "Recycled materials with rainwater system", "", 
            90.0, 4, "Large", true, 9.0, 4.6, "Cabin", true);

        // Create recommendations with match scores
        Recommendation rec1 = new Recommendation(ecoSuite, 0.95, "Perfect for eco-conscious travelers");
        rec1.setDiscountPercentage(15.0);
        rec1.setSpecialOffer("🎁 15% OFF + Free Eco Tour");
        rec1.setPersonalized(true);

        Recommendation rec2 = new Recommendation(treehouse, 0.88, "Unique sustainable experience");
        rec2.setDiscountPercentage(10.0);
        rec2.setSpecialOffer("🌳 10% OFF + Tree Planting Certificate");
        rec2.setPersonalized(true);

        Recommendation rec3 = new Recommendation(cabin, 0.82, "Great value eco-friendly option");
        rec3.setDiscountPercentage(20.0);
        rec3.setSpecialOffer("💚 20% OFF + Organic Breakfast");
        rec3.setPersonalized(false);

        recommendations.add(rec1);
        recommendations.add(rec2);
        recommendations.add(rec3);

        return recommendations;
    }
}
