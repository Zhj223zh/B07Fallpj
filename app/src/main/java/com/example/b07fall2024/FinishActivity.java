package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.b072024gr2.ecoproj.R;
import com.example.b07fall2024.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FinishActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://b07ecoproject-default-rtdb.firebaseio.com/").getReference();

        // Fetch the current user details
        fetchUserDetails();

        AppCompatButton restartBtn = findViewById(R.id.bk);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    submitData();
                    Toast.makeText(FinishActivity.this, "Quiz completed successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FinishActivity.this, "User data not loaded. Try again.", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(FinishActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        AppCompatButton endQuizBtn = findViewById(R.id.end_quiz);
        endQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    submitData();
                    Toast.makeText(FinishActivity.this, "Quiz completed successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FinishActivity.this, "User data not loaded. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchUserDetails() {
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(FinishActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FinishActivity.this, "Error fetching user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitData() {
        if (user != null) {
            // Update the isQuestionsCompleted field
            user.setIsQuestionsCompleted(true);
            String userId = mAuth.getCurrentUser().getUid();

            // Get data passed from intent
            Intent intent = getIntent();
            float transportation = intent.getFloatExtra("transportation", 0);
            float food = intent.getFloatExtra("food", 0);
            float housing = intent.getFloatExtra("housing", 0);
            float consumption= intent.getFloatExtra("consumption", 0);
            float country= intent.getFloatExtra("country", 0);
            // Update Firebase database
            DatabaseReference userRef = databaseReference.child("Users").child(userId);
            userRef.child("AnualCF").child("EnergyUse").setValue(housing);
            userRef.child("AnualCF").child("FoodConsumption").setValue(food);
            userRef.child("AnualCF").child("Shopping").setValue(consumption);
            userRef.child("AnualCF").child("Transportation").setValue(transportation);
            userRef.child("Location").child("Country").setValue("Canada");
            userRef.child("Location").child("Region").setValue("World");

            // Mark the quiz as completed
            userRef.child("isQuestionsCompleted").setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(FinishActivity.this, "User status updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FinishActivity.this, "Failed to update user status.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
