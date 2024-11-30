package com.example.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
public class emission_comparison extends Fragment {

    private DatabaseReference db;
    private BarChart comparisonChart;
    private String currentUserId;
    private String country;
    private String region;
    private float userAnnualEmission = 0;
    private float countryAverage = 0;
    private float regionAverage = 0;
    private int currentYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emission_comparison, container, false);
        comparisonChart = view.findViewById(R.id.comparison_chart);
        db = FirebaseDatabase.getInstance("https://b07ecoproject-default-rtdb.firebaseio.com/").getReference();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {currentUserId = currentUser.getUid();}
        else
        {currentUserId = "0";}
        fetchUserData();
        return view;
    }

    private void fetchUserData() {
        db.child("Users").child(currentUserId).child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                country = dataSnapshot.child("Country").getValue(String.class);
                region = dataSnapshot.child("Region").getValue(String.class);
                currentYear = Calendar.getInstance().get(Calendar.YEAR);
                fetchUserEmission();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void fetchUserEmission() {
        db.child("Users").child(currentUserId).child("Emission").child(String.valueOf(currentYear)).child("categoryBreakdown")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userAnnualEmission = getTotalEmissionFromCategoryBreakdown(dataSnapshot);

                        fetchAverageEmission();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    /** @noinspection DataFlowIssue*/
    private float getTotalEmissionFromCategoryBreakdown(DataSnapshot dataSnapshot) {
        float total = 0;
        for (DataSnapshot category : dataSnapshot.getChildren()) {
            total += category.getValue(Float.class);
        }
        return total;
    }

    private void fetchAverageEmission() {
        db.child("Global");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * @noinspection DataFlowIssue
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(country).exists()) {
                    countryAverage = dataSnapshot.child(country).getValue(Float.class);
                } else {
                    countryAverage = 0;
                }
                if (dataSnapshot.child(region).exists()) {
                    regionAverage = dataSnapshot.child(region).getValue(Float.class);
                } else {
                    regionAverage = 0;
                }
                displayComparisonChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void displayComparisonChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, userAnnualEmission));
        entries.add(new BarEntry(1, regionAverage));
        entries.add(new BarEntry(2, countryAverage));

        BarDataSet dataSet = new BarDataSet(entries, "CO2e Emissions");
        dataSet.setColors(new int[]{R.color.black, R.color.black, R.color.black}, getContext());

        BarData barData = new BarData(dataSet);
        comparisonChart.setData(barData);

        String[] labels = {"Your Emission", "Region Avg", "Country Avg"};
        comparisonChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        comparisonChart.getXAxis().setGranularity(1f);
        comparisonChart.getXAxis().setGranularityEnabled(true);

        comparisonChart.invalidate();
    }
}