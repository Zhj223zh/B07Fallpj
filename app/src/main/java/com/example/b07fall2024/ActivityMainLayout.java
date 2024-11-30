package com.example.b07fall2024;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.b072024gr2.ecoproj.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.habit.MainPage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
public class ActivityMainLayout extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();


        updateDateStorage(calendar);
        fetchEmissionDataFromFirebase();

        calendarView.setOnDateChangeListener((view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);

            updateDateStorage(calendar);
            fetchEmissionDataFromFirebase();
        });

        // to transportation page
        Button btnTransportation = findViewById(R.id.btnTransportationActivity);
        btnTransportation.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityMainLayout.this, Transportation.class);
            startActivity(intent);
        });

        // to food consumption page
        Button btnFoodConsumption = findViewById(R.id.btnFoodConsumptionActivities);
        btnFoodConsumption.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityMainLayout.this, FoodConsumption.class);
            startActivity(intent);
        });

        // to food consumption page
        Button btnConsumptionShopping = findViewById(R.id.btnConsumptionShoppingActivities);
        btnConsumptionShopping.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityMainLayout.this, ConsumptionAndShopping.class);
            startActivity(intent);
        });

        Button btnHabit = findViewById(R.id.btnHabit);
        btnHabit.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityMainLayout.this, MainPage.class);
            startActivity(intent);
        });
    }

    private void updateDateStorage(Calendar calendar) {
        DateStorage dateStorage = DateStorage.getInstance();
        dateStorage.setYear(calendar.get(Calendar.YEAR));
        dateStorage.setMonth(calendar.get(Calendar.MONTH)); //+1?
        dateStorage.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateStorage.setWeek(calendar.get(Calendar.WEEK_OF_YEAR));
    }

    private void fetchEmissionDataFromFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e("ActivityMainLayout", "The user hasn't logged in");
            return;
        }

        // Get date info
        DateStorage dateStorage = DateStorage.getInstance();
        String userId = user.getUid();
        String year = String.valueOf(dateStorage.getYear());
        String month = String.valueOf(dateStorage.getMonth());
        String week = String.valueOf(dateStorage.getWeek());
        String day = String.valueOf(dateStorage.getDay());

        // Database path to "categoryBreakdown"
        DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child(year).child(month).child(week).child(day).child("categoryBreakdown");

        dayRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    EmissionStorage emissionStorage = EmissionStorage.getInstance();


                    double foodConsumption = snapshot.hasChild("FoodConsumption") ?
                            Double.parseDouble(snapshot.child("FoodConsumption").getValue(String.class)) : 0.0;

                    double shopping = snapshot.hasChild("Shopping") ?
                            Double.parseDouble(snapshot.child("Shopping").getValue(String.class)) : 0.0;

                    double transportation = snapshot.hasChild("Transportation") ?
                            Double.parseDouble(snapshot.child("Transportation").getValue(String.class)) : 0.0;

                    double energyUse = snapshot.hasChild("EnergyUse") ?
                            Double.parseDouble(snapshot.child("EnergyUse").getValue(String.class)) : 0.0;

                    double total = foodConsumption + shopping + transportation + energyUse;

                    // Store values in EmissionStorage
                    emissionStorage.setEnergyUse(energyUse);
                    emissionStorage.setFoodConsumption(foodConsumption);
                    emissionStorage.setShopping(shopping);
                    emissionStorage.setTransportation(transportation);

                    runOnUiThread(() -> {
                        TextView foodConsumptionTextView = findViewById(R.id.textViewFoodConsumption);
                        TextView shoppingTextView = findViewById(R.id.textViewShopping);
                        TextView transportationTextView = findViewById(R.id.textViewTransportation);
                        TextView EneryUseTextView = findViewById(R.id.textViewEnergyUse);




                        TextView TotalTextView = findViewById(R.id.textViewDailyEmissions);

                        foodConsumptionTextView.setText("Food Consumption: " + foodConsumption + " kg");
                        shoppingTextView.setText("Shopping: " + shopping + " kg");
                        transportationTextView.setText("Transportation: " + transportation + " kg");
                        EneryUseTextView.setText("EneryUse: " + energyUse + " kg");
                        TotalTextView.setText("Daily CO2e Emissions: \n" + total + " kg" );
                    });

//                    Log.d("ActivityMainLayout", "Emission data successfully fetched and stored");
//                } else {
//                    Log.e("ActivityMainLayout", "No data found for the selected date");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ActivityMainLayout", "Failed to fetch data: " + error.getMessage());
            }
        });
    }

}
