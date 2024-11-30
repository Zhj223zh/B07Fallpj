package com.example.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.b072024gr2.ecoproj.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class emission_breakdown extends Fragment {

    private BarChart barChart;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emission_breakdown, container, false);

        barChart = rootView.findViewById(R.id.barChart);

        databaseReference = FirebaseDatabase.getInstance("https://b07ecoproject-default-rtdb.firebaseio.com/").getReference();

        fetchEmissionDataForCurrentWeek();

        return rootView;
    }

    private int getCurrentWeekNumber() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    private void fetchEmissionDataForCurrentWeek() {
        int currentWeek = getCurrentWeekNumber();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId;
        if(currentUser != null)
        {currentUserId = currentUser.getUid();}
        else
        {currentUserId = "0";}
        databaseReference.child("Users").child(currentUserId).child("Emission").child(String.valueOf(currentYear))
                .child(String.valueOf(currentWeek))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    /** @noinspection DataFlowIssue*/
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChild("categoryBreakdown")) {
                            float energyUse = snapshot.child("categoryBreakdown").child("EnergyUse").getValue(Float.class);
                            float foodConsumption = snapshot.child("categoryBreakdown").child("FoodConsumption").getValue(Float.class);
                            float shopping = snapshot.child("categoryBreakdown").child("Shopping").getValue(Float.class);
                            float transportation = snapshot.child("categoryBreakdown").child("Transportation").getValue(Float.class);

                            updateBarChart(energyUse, foodConsumption, shopping, transportation);
                        } else {
                            Toast.makeText(getContext(), "No data available for the current week", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateBarChart(float energyUse, float foodConsumption, float shopping, float transportation) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, energyUse, "Energy Use"));
        barEntries.add(new BarEntry(1, foodConsumption, "Food Consumption"));
        barEntries.add(new BarEntry(2, shopping, "Shopping"));
        barEntries.add(new BarEntry(3, transportation, "Transportation"));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Emissions by Category");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        Description description = new Description();
        description.setText("CO2 Emissions Breakdown");
        barChart.setDescription(description);

        barChart.animateY(1000);
        barChart.invalidate();
    }
}