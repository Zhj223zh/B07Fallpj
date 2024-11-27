package com.example.b07fall2024;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
public class ActivityMainLayout extends AppCompatActivity {
    private int year, month, day, week;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        // 初始化 Firebase Firestore 和 Auth
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();

        // 设置默认日期为当天并存储到 DateStorage
        updateDateStorage(calendar);
        fetchEmissionDataFromFirebase();

        // 设置 CalendarView 的日期选择监听器
        calendarView.setOnDateChangeListener((view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            // 更新 Calendar 对象
            calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);

            // 更新 DateStorage 中的年、月、日和星期
            updateDateStorage(calendar);
            fetchEmissionDataFromFirebase(); // 获取特定日期的数据
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
    }

    private void updateDateStorage(Calendar calendar) {
        DateStorage dateStorage = DateStorage.getInstance();
        dateStorage.setYear(calendar.get(Calendar.YEAR));
        dateStorage.setMonth(calendar.get(Calendar.MONTH) + 1); // 月份从0开始，需要加1
        dateStorage.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateStorage.setWeek(calendar.get(Calendar.WEEK_OF_YEAR));
    }

    private void fetchEmissionDataFromFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.d("ActivityMainLayout", "The user hasn't logged in");
            return;
        }

        // 获取日期信息
        DateStorage dateStorage = DateStorage.getInstance();
        String userId = user.getUid();
        String year = String.valueOf(dateStorage.getYear());
        String month = String.valueOf(dateStorage.getMonth());
        String week = String.valueOf(dateStorage.getWeek());
        String day = String.valueOf(dateStorage.getDay());

        // 定义数据库路径
        DocumentReference dayRef = firestore.collection("Emission")
                .document(userId)
                .collection(year)
                .document(month)
                .collection(week)
                .document(day)
                .collection("categoryBreakdown")
                .document("EmissionData");

        // 从 Firebase 获取数据并存储到 EmissionStorage
        dayRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    EmissionStorage emissionStorage = EmissionStorage.getInstance();

                    // 获取各个排放数据
                    double energyUse = task.getResult().getDouble("EnergyUse") != null ? task.getResult().getDouble("EnergyUse") : 0.0;
                    double foodConsumption = task.getResult().getDouble("FoodConsumption") != null ? task.getResult().getDouble("FoodConsumption") : 0.0;
                    double shopping = task.getResult().getDouble("Shopping") != null ? task.getResult().getDouble("Shopping") : 0.0;
                    double transportation = task.getResult().getDouble("Transportation") != null ? task.getResult().getDouble("Transportation") : 0.0;

                    // 存储到 EmissionStorage
                    emissionStorage.setEnergyUse(energyUse);
                    emissionStorage.setFoodConsumption(foodConsumption);
                    emissionStorage.setShopping(shopping);
                    emissionStorage.setTransportation(transportation);

                    // 更新 UI 上的 TextView
                    TextView energyUseTextView = findViewById(R.id.textViewEnergyUse);
                    TextView foodConsumptionTextView = findViewById(R.id.textViewFoodConsumption);
                    TextView shoppingTextView = findViewById(R.id.textViewShopping);
                    TextView transportationTextView = findViewById(R.id.textViewTransportation);

                    runOnUiThread(() -> {
                        energyUseTextView.setText("Energy Use: " + energyUse + " kg");
                        foodConsumptionTextView.setText("Food Consumption: " + foodConsumption + " kg");
                        shoppingTextView.setText("Shopping: " + shopping + " kg");
                        transportationTextView.setText("Transportation: " + transportation + " kg");
                    });

                    Log.d("ActivityMainLayout", "Emission data successfully fetched and stored");
                } else {
                    Log.d("ActivityMainLayout", "No data found for the selected date");
                }
            } else {
                Log.d("ActivityMainLayout", "Failed to fetch data: " + task.getException());
            }
        });
    }
}
