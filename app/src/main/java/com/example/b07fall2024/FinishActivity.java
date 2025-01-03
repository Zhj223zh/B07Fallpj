package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
        System.out.println("FINISHING UP");
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
                handleButtonClick(v);
            }
        });

        AppCompatButton endQuizBtn = findViewById(R.id.end_quiz);
        endQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(v);
            }
        });
    }

    private void handleButtonClick(View view){
        int id = view.getId();
        if (id == R.id.bk) {
            handleBackButtonClick();
        }
        else if(id == R.id.end_quiz) {
            handleEndQuizButtonClick();
        }
        else {
            Toast.makeText(this, "Invalid click detected", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleBackButtonClick(){
        if (user != null) {
            submitData();
            Toast.makeText(FinishActivity.this, "Quiz completed successfully.", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(FinishActivity.this, FinishActivity.class);
            finish();
        } else {
            Toast.makeText(FinishActivity.this, "User data not loaded. Try again.", Toast.LENGTH_SHORT).show();
        }
        System.out.println("SWITCHING TO DASHBOARD");
        Intent intent = new Intent(FinishActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    private void handleEndQuizButtonClick(){
        if (user != null) {
            submitData();
            Toast.makeText(FinishActivity.this, "Quiz completed successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FinishActivity.this, Emission_Dashboard.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(FinishActivity.this, "User data not loaded. Try again.", Toast.LENGTH_SHORT).show();
        }
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

    private String newCountry(String country){
        if (!(country != null && !country.isEmpty())){
            Log.d("FinishActivity", "Country is null or empty!");
            //country = "Canada";
            System.out.println("null country");
            return "Canada";
        }
        else {
            Log.d("FinishActivity", "Country received: " + country);
            //TextView selectedCountry = findViewById(R.id.country_name);
            //return selectedCountry.getText().toString();
            System.out.println("country =" + country);
            return country;
        }
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
            //float country= intent.getFloatExtra("country", 0);
            String country= intent.getStringExtra("country");
            String newcountry = newCountry(country);

            System.out.println("PASSING TO DB: COUNTRY = " + newcountry);
            Log.i("FinishActivity", "PASSING TO DB: COUNTRY = " + newcountry);

            //newcountry = "Canada";
            //newcountry = null;

            // Update Firebase database
            DatabaseReference userRef = databaseReference.child("Users").child(userId);
            userRef.child("AnualCF").child("EnergyUse").setValue(housing);
            userRef.child("AnualCF").child("FoodConsumption").setValue(food);
            userRef.child("AnualCF").child("Shopping").setValue(consumption);
            userRef.child("AnualCF").child("Transportation").setValue(transportation);
            userRef.child("Location").child("Region").setValue(newcountry);
            userRef.child("Location").child("Country").setValue("Canada");
            //userRef.child("Location").child("Region").setValue("Wordle");

            // Mark the quiz as completed
            userRef.child("isQuestionsCompleted").setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(FinishActivity.this, "User status updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FinishActivity.this, "Failed to update user status.", Toast.LENGTH_SHORT).show();
                }
            });

            //userRef.child("Location").child("Country").setValue(newcountry);
        }
    }
}
