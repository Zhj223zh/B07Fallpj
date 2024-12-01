package com.example.b07fall2024;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b072024gr2.ecoproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.habit.Habit;
import com.habit.HabitAdapter;
import com.habit.MainPage;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ActivityMainLayout extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private List<Habit> hList;
    private TextView tvAdoptedSummary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
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

        tvAdoptedSummary = findViewById(R.id.tvAdoptedSummary);
        loadAdoptedSummary();

        TextView tvReminder = findViewById(R.id.tvReminder);
        hList = MainPage.getDummyHabits();
        loadAdoptedHabits(tvReminder);

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
        DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference().child("users").child("Emission").child(userId).child(year).child(month).child(week).child(day).child("categoryBreakdown");

        dayRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    EmissionStorage emissionStorage = EmissionStorage.getInstance();


                    double foodConsumption = snapshot.hasChild("FoodConsumption") ?
                            Double.parseDouble(Objects.requireNonNull(snapshot.child("FoodConsumption").getValue(String.class))) : 0.0;

                    double shopping = snapshot.hasChild("Shopping") ?
                            Double.parseDouble(Objects.requireNonNull(snapshot.child("Shopping").getValue(String.class))) : 0.0;

                    double transportation = snapshot.hasChild("Transportation") ?
                            Double.parseDouble(Objects.requireNonNull(snapshot.child("Transportation").getValue(String.class))) : 0.0;

                    double energyUse = snapshot.hasChild("EnergyUse") ?
                            Double.parseDouble(Objects.requireNonNull(snapshot.child("EnergyUse").getValue(String.class))) : 0.0;

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
                        EneryUseTextView.setText("EnergyUse: " + energyUse + " kg");
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

    private void loadAdoptedHabits(TextView tvReminder) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            tvReminder.setText("No user logged in.");
            return;
        }

        // 获取用户 ID 并构造 Firebase 数据路径
        String userId = user.getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId).child("Habit");

        // 从 Firebase 获取 Adopted Habits 数据
        dbRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                StringBuilder reminderMessage = new StringBuilder();

                // 遍历 Firebase 数据，检查 Adopted Habits
                for (Habit habit : hList) {
                    if (snapshot.hasChild(habit.getName())) {
                        habit.setAdopted(true); // 更新 Habit 状态
                        reminderMessage.append("- ").append(habit.getName()).append("\n");
                    }
                }

                // 更新提醒信息
                if (reminderMessage.length() > 0) {
                    tvReminder.setText("Don't forget to log your progress for:\n" + reminderMessage.toString());
                } else {
                    tvReminder.setText("You have not adopted any habits yet.");
                }
            } else {
                tvReminder.setText("Failed to load adopted habits.");
            }
        });
    }


    private void loadAdoptedSummary() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            tvAdoptedSummary.setText("No user logged in.");
            return;
        }

        // 使用用户 UID 作为路径
        String userId = user.getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId).child("Habit"); // 修改为包含用户 ID 的路径

        dbRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                StringBuilder summary = new StringBuilder("Adopted Habits Summary:\n");

                for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                    String habitName = habitSnapshot.getKey();

                    if (habitName != null) {
                        // 直接读取 progress 和 adoptDate
                        Long progress = habitSnapshot.child("progress").getValue(Long.class); // 读取 progress
                        String adoptDate = habitSnapshot.child("adoptDate").getValue(String.class); // 读取 adoptDate

                        summary.append("- ")
                                .append(habitName)
                                .append(" :")
                                .append(progress != null ? progress : 0)
                                .append(" times, from: ")
                                .append(adoptDate != null ? adoptDate : "N/A")
                                .append("\n");
                    }
                }

                tvAdoptedSummary.setText(summary.toString());
            } else {
                tvAdoptedSummary.setText("Failed to load adopted habits.");
            }
        });
    }


}
