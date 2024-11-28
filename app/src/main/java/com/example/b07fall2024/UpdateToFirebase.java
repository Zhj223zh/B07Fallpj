package com.example.b07fall2024;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateToFirebase {
    private static UpdateToFirebase instance;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;

    // Private constructor to enforce singleton
    private UpdateToFirebase() {
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    // Singleton instance getter
    public static UpdateToFirebase getInstance() {
        if (instance == null) {
            instance = new UpdateToFirebase();
        }
        return instance;
    }

    // Upload data to Firebase
    public void uploadDataToFirebase(Context context) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Toast.makeText(context, "User is not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gain the user ID
        String userId = user.getUid();

        // Retrieve data from EmissionStorage
        String year = String.valueOf(DateStorage.getInstance().getYear());
        String month = String.valueOf(DateStorage.getInstance().getMonth());
        String week = String.valueOf(DateStorage.getInstance().getWeek());
        String day = String.valueOf(DateStorage.getInstance().getDay());

        double Food = EmissionStorage.getInstance().getFoodConsumption();
        double EnergyUse = EmissionStorage.getInstance().getEnergyUse();
        double Transportation = EmissionStorage.getInstance().getTransportation();
        double Shopping = EmissionStorage.getInstance().getShopping();

        // Data map
        Map<String, Object> data = new HashMap<>();
        data.put("FoodConsumption", String.valueOf(Food));
        data.put("Shopping", String.valueOf(Shopping));
        data.put("Transportation", String.valueOf(Transportation));
        data.put("EnergyUse", String.valueOf(EnergyUse));

        // Define the path of saving the data
        DatabaseReference categoryBreakdownRef = databaseRef.child("users")
                .child(userId)
                .child(year)
                .child(month)
                .child(week)
                .child(day)
                .child("categoryBreakdown");

        // Check if the path exists
        categoryBreakdownRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // day exists, replace
                    categoryBreakdownRef.setValue(data)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Data replaced successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Failed to replace data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // not exist
                    categoryBreakdownRef.setValue(data)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Data uploaded successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Failed to upload data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error checking data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
