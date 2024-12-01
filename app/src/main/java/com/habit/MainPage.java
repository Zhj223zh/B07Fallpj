package com.habit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AlertDialog;

import com.b072024gr2.ecoproj.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.Intent;

import com.example.b07fall2024.ActivityMainLayout;
import com.example.b07fall2024.DateStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPage extends AppCompatActivity {
    private RecyclerView recyclerViewHabits;
    private HabitAdapter habitAdapter;
    private List<Habit> hList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        recyclerViewHabits = findViewById(R.id.recyclerViewHabits);
        SearchView searchView = findViewById(R.id.searchView);
        Button btnFilterType = findViewById(R.id.btnFilterType);
        Button btnFilterImpact = findViewById(R.id.btnFilterImpact);
        Button btnReset = findViewById(R.id.btnReset);

        hList = getDummyHabits();
        habitAdapter = new HabitAdapter(this, hList);
        recyclerViewHabits.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHabits.setAdapter(habitAdapter);

        Button btnUploadAndReturn = findViewById(R.id.btnUploadAndReturn);
        btnUploadAndReturn.setOnClickListener(v -> {
            uploadDataToFirebase();
            Intent intent = new Intent(MainPage.this, ActivityMainLayout.class); // 跳转到主页面
            startActivity(intent);
        });

        // Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                filterHabitsByName(query);
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                filterHabitsByName(newText);
                return false;
            }
        });

        btnFilterType.setOnClickListener(v -> showFilterByType());
        btnFilterImpact.setOnClickListener(v -> showFilterByImpact());
        btnReset.setOnClickListener(v -> resetFilters());
    }

    public static List<Habit> getDummyHabits() {
        List<Habit> habits = new ArrayList<>();
        habits.add(new Habit("Walk to school", "Transportation", "Reduce driving, choose cycling and walking instead.", 5));
        habits.add(new Habit("Reduce Food Waste", "Food", "Avoid wasting food", 2));
        habits.add(new Habit("Recycling", "Consumption", "Recycle and reuse unneeded items", 3));
        habits.add(new Habit("Use Public Transportation", "Transportation", "Choose public transportation instead of private vehicle", 4));
        habits.add(new Habit("Turn Off Light", "EnergyUse", "Turn off light every time you leave a room", 2));
        return habits;
    }

    private void filterHabitsByName(String query) {
        List<Habit> filteredList = new ArrayList<>();
        for (Habit habit : hList) {
            if (habit.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(habit);
            }
        }
        updateRecyclerView(filteredList);
    }

    private void filterByType(String category) {
        List<Habit> filteredList = new ArrayList<>();
        for (Habit habit : hList) {
            if (habit.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(habit);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No habits found for category: " + category, Toast.LENGTH_SHORT).show();
        }
        updateRecyclerView(filteredList);
    }

    private void filterByImpact(int minImpact) {
        List<Habit> filteredList = new ArrayList<>();
        for (Habit habit : hList) {
            if (habit.getImpact() >= minImpact) {
                filteredList.add(habit);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No habits found with impact >= " + minImpact, Toast.LENGTH_SHORT).show();
        }
        updateRecyclerView(filteredList);
    }

    private void resetFilters() {
        updateRecyclerView(hList);
    }

    private void showFilterByType() {
        final String[] categories = {"Transportation", "Food", "Consumption", "EnergyUse"};
        new AlertDialog.Builder(this).setTitle("Select a category").setItems(categories, (dialog, which) -> filterByType(categories[which])).show();
    }

    private void showFilterByImpact() {
        final String[] impacts = {"1", "2", "3", "4", "5"};
        new AlertDialog.Builder(this).setTitle("Select minimum impact").setItems(impacts, (dialog, which) -> filterByImpact(Integer.parseInt(impacts[which]))).show();
    }

    private void updateRecyclerView(List<Habit> newList) {
        habitAdapter = new HabitAdapter(this, newList);
        recyclerViewHabits.setAdapter(habitAdapter);
    }

    private void uploadDataToFirebase() {
        String year = String.valueOf(DateStorage.getInstance().getYear());
        String month = String.valueOf(DateStorage.getInstance().getMonth());
        String day = String.valueOf(DateStorage.getInstance().getDay());
        String currentDate = year + "/" + month + "/" + day;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in. Unable to upload data.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取当前用户的 UID
        String userId = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId).child("Habit");

        for (Habit habit : hList) {
            if (habit.isAdopted() && habit.getProgress() > 0) {
                String habitKey = habit.getName();
                DatabaseReference habitRef = databaseReference.child(habitKey);

                habitRef.child("progress").setValue(habit.getProgress());
                habitRef.child("adoptDate").setValue(currentDate).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Data uploaded for: " + habit.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to upload data for: " + habit.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }






}
