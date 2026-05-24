package com.ecostay.retreat;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NatureReservesActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> reserveList;
    private FirebaseFirestore db;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nature_reserves);

        db = FirebaseFirestore.getInstance();
        
        titleText = findViewById(R.id.titleText);
        // Initialize nature reserve buttons
        Button alpineButton = findViewById(R.id.alpineButton);
        Button forestButton = findViewById(R.id.forestButton);
        Button glacierButton = findViewById(R.id.glacierButton);
        Button wildlifeButton = findViewById(R.id.wildlifeButton);
        Button canyonButton = findViewById(R.id.canyonButton);
        
        // Setup button click listeners
        setupReserveButtons(alpineButton, forestButton, glacierButton, wildlifeButton, canyonButton);
    }

    private void setupReserveButtons(Button alpine, Button forest, Button glacier, Button wildlife, Button canyon) {
        alpine.setOnClickListener(v -> Toast.makeText(this, "🏔️ Alpine Wilderness Reserve selected!", Toast.LENGTH_SHORT).show());
        forest.setOnClickListener(v -> Toast.makeText(this, "🌲 Ancient Forest Preserve selected!", Toast.LENGTH_SHORT).show());
        glacier.setOnClickListener(v -> Toast.makeText(this, "❄️ Glacier National Park selected!", Toast.LENGTH_SHORT).show());
        wildlife.setOnClickListener(v -> Toast.makeText(this, "🦅 Wildlife Sanctuary selected!", Toast.LENGTH_SHORT).show());
        canyon.setOnClickListener(v -> Toast.makeText(this, "🏜️ Desert Canyon Reserve selected!", Toast.LENGTH_SHORT).show());
    }

    private void loadReserves() {
        // Try to load from Firebase first with enhanced error handling
        db.collection("nature_reserves")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reserveList.clear();
                    
                    if (queryDocumentSnapshots.isEmpty()) {
                        // No data in Firebase, use sample data
                        loadSampleReserves();
                    } else {
                        // Load from Firebase
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String reserveName = document.getString("name");
                            String area = document.getString("area");
                            String conservation = document.getString("conservation_status");
                            if (reserveName != null) {
                                String displayText = reserveName + " - " + 
                                    (area != null ? area : "Protected area") +
                                    (conservation != null ? " (" + conservation + ")" : "");
                                reserveList.add(displayText);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "✅ Loaded " + reserveList.size() + " nature reserves!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Enhanced error handling
                    String errorMessage = "Unable to load nature reserves";
                    if (e.getMessage() != null) {
                        if (e.getMessage().contains("network")) {
                            errorMessage = "Network error. Using offline reserves data.";
                        } else if (e.getMessage().contains("permission")) {
                            errorMessage = "Access denied. Using sample reserves.";
                        }
                    }
                    
                    Toast.makeText(this, "⚠️ " + errorMessage, Toast.LENGTH_LONG).show();
                    loadSampleReserves(); // Fallback to sample data
                });

        setupReserveClickListener();
    }
    
    private void loadSampleReserves() {
        // Enhanced sample nature reserves with conservation details
        reserveList.add("🌲 Green Valley Nature Reserve - 500 acres (Protected since 1985)");
        reserveList.add("🏔️ Mountain Peak Conservation Area - 1200 acres (UNESCO Heritage)");
        reserveList.add("🌊 Lakeside Wildlife Sanctuary - 800 acres (Wetland Protection)");
        reserveList.add("🦋 Butterfly Garden Reserve - 150 acres (Species Recovery)");
        reserveList.add("🌿 Ancient Forest Preserve - 2000 acres (Old Growth Protection)");
        reserveList.add("🐦 Wetland Bird Sanctuary - 600 acres (Migratory Bird Route)");
        reserveList.add("🌺 Alpine Meadow Reserve - 300 acres (Wildflower Conservation)");
        reserveList.add("🏞️ River Valley Preserve - 900 acres (Watershed Protection)");
        adapter.notifyDataSetChanged();
        
        Toast.makeText(this, "📱 Loaded sample nature reserves for demonstration!", Toast.LENGTH_SHORT).show();
    }
    
    private void setupReserveClickListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position < reserveList.size()) {
                String selectedReserve = reserveList.get(position);
                showReserveDetails(selectedReserve, position);
            }
        });
    }
    
    private void showReserveDetails(String reserveName, int position) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🌿 " + reserveName.split(" - ")[0]);
        
        // Different details for different reserves
        String details = getReserveDetails(position);
        
        builder.setMessage(details);
        builder.setPositiveButton("Learn More", (dialog, which) -> {
            showGreenInitiatives();
        });
        builder.setNegativeButton("Visit Info", (dialog, which) -> {
            Toast.makeText(this, "📍 Getting directions to " + reserveName.split(" - ")[0], Toast.LENGTH_SHORT).show();
        });
        builder.setNeutralButton("Close", null);
        builder.show();
    }
    
    private String getReserveDetails(int position) {
        String[] details = {
            "🌲 GREEN VALLEY DETAILS:\n\n" +
            "• Established: 1985\n" +
            "• Wildlife: 150+ species\n" +
            "• Features: Hiking trails, Bird watching\n" +
            "• Conservation: Forest restoration\n" +
            "• Eco-initiatives: Carbon sequestration\n\n" +
            "🌱 EcoStay Partnership:\n" +
            "• Guest volunteer programs\n" +
            "• Educational nature walks\n" +
            "• Tree planting activities",
            
            "🏔️ MOUNTAIN PEAK DETAILS:\n\n" +
            "• UNESCO World Heritage Site\n" +
            "• Elevation: 2,500m peak\n" +
            "• Features: Alpine ecosystem\n" +
            "• Conservation: Endangered species\n" +
            "• Research: Climate monitoring\n\n" +
            "🌱 EcoStay Partnership:\n" +
            "• Guided mountain tours\n" +
            "• Conservation education\n" +
            "• Research participation",
            
            "🌊 LAKESIDE SANCTUARY DETAILS:\n\n" +
            "• Wetland Protection Area\n" +
            "• Water quality: Pristine\n" +
            "• Features: Kayaking, Fishing\n" +
            "• Conservation: Water ecosystem\n" +
            "• Wildlife: Otters, waterfowl\n\n" +
            "🌱 EcoStay Partnership:\n" +
            "• Eco-friendly water sports\n" +
            "• Water quality monitoring\n" +
            "• Wetland restoration",
            
            "🦋 BUTTERFLY GARDEN DETAILS:\n\n" +
            "• Species Recovery Program\n" +
            "• Butterflies: 50+ species\n" +
            "• Features: Pollinator garden\n" +
            "• Conservation: Habitat restoration\n" +
            "• Research: Migration patterns\n\n" +
            "🌱 EcoStay Partnership:\n" +
            "• Photography workshops\n" +
            "• Pollinator education\n" +
            "• Garden maintenance",
            
            "🌿 ANCIENT FOREST DETAILS:\n\n" +
            "• Old Growth Protection\n" +
            "• Trees: 500+ years old\n" +
            "• Features: Canopy walks\n" +
            "• Conservation: Biodiversity\n" +
            "• Research: Forest ecology\n\n" +
            "🌱 EcoStay Partnership:\n" +
            "• Forest bathing sessions\n" +
            "• Ecology education\n" +
            "• Conservation funding",
            
            "🐦 WETLAND BIRD DETAILS:\n\n" +
            "• Migratory Bird Route\n" +
            "• Species: 200+ bird types\n" +
            "• Features: Observation blinds\n" +
            "• Conservation: Habitat protection\n" +
            "• Research: Migration tracking\n\n" +
            "🌱 EcoStay Partnership:\n" +
            "• Bird watching tours\n" +
            "• Citizen science projects\n" +
            "• Habitat restoration",
            
            "🌺 ALPINE MEADOW DETAILS:\n\n" +
            "• Wildflower Conservation\n" +
            "• Flowers: 100+ species\n" +
            "• Features: Seasonal blooms\n" +
            "• Conservation: Native plants\n" +
            "• Research: Climate adaptation\n\n" +
            "🌱 EcoStay Partnership:\n" +
            "• Wildflower photography\n" +
            "• Botanical education\n" +
            "• Seed collection program",
            
            "🏞️ RIVER VALLEY DETAILS:\n\n" +
            "• Watershed Protection\n" +
            "• River: Crystal clear waters\n" +
            "• Features: Rafting, Swimming\n" +
            "• Conservation: Water purity\n" +
            "• Wildlife: River ecosystem\n\n" +
            "🌱 EcoStay Partnership:\n" +
            "• Eco-friendly river tours\n" +
            "• Water conservation education\n" +
            "• Riparian restoration"
        };
        
        return position < details.length ? details[position] : details[0];
    }
    
    private void showGreenInitiatives() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("🌍 EcoStay's Green Initiatives");
        
        String initiatives = "🌱 OUR ENVIRONMENTAL COMMITMENT:\n\n" +
                "♻️ CONSERVATION PARTNERSHIPS:\n" +
                "• 8 protected nature reserves\n" +
                "• 15,000+ acres under protection\n" +
                "• $50,000 annual conservation funding\n" +
                "• 25 research projects supported\n\n" +
                
                "🌿 SUSTAINABILITY PRACTICES:\n" +
                "• 100% renewable energy resort\n" +
                "• Zero waste to landfill policy\n" +
                "• Native species restoration\n" +
                "• Carbon neutral operations\n\n" +
                
                "👥 GUEST INVOLVEMENT:\n" +
                "• Volunteer conservation programs\n" +
                "• Educational nature walks\n" +
                "• Citizen science participation\n" +
                "• Tree planting activities\n\n" +
                
                "🏆 ACHIEVEMENTS (2024):\n" +
                "• 10,000+ trees planted\n" +
                "• 500+ volunteers engaged\n" +
                "• 95% waste reduction\n" +
                "• 50+ species protected\n\n" +
                
                "🎯 2025 GOALS:\n" +
                "• Expand protected areas by 20%\n" +
                "• Launch wildlife corridor project\n" +
                "• Achieve net-positive impact\n" +
                "• Engage 1000+ eco-volunteers";
        
        builder.setMessage(initiatives);
        builder.setPositiveButton("Join Our Mission", (dialog, which) -> {
            Toast.makeText(this, "🌱 Thank you for supporting conservation!", Toast.LENGTH_LONG).show();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }
}
