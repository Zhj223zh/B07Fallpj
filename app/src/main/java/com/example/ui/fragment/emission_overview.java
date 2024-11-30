package com.example.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.b072024gr2.ecoproj.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class emission_overview extends Fragment {

    private TextView totalEmissionsText;
    protected Spinner timePeriodSpinner;
    protected DatabaseReference userRef;
    private int selectedTimePeriod;
    private String currentUserId;

    /** @noinspection DataFlowIssue*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emission_overview, container, false);
        totalEmissionsText = rootView.findViewById(R.id.totalEmissionsText);
        timePeriodSpinner = rootView.findViewById(R.id.timePeriodSpinner);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {currentUserId = currentUser.getUid();}
        else
        {currentUserId = "0";}
        userRef = FirebaseDatabase.getInstance("https://b07ecoproject-default-rtdb.firebaseio.com/").getReference();
        @SuppressLint("UseRequireInsteadOfGet") ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_period_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timePeriodSpinner.setAdapter(adapter);
        timePeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTimePeriod = position; // 0 -> Weekly, 1 -> Monthly, 2 -> Yearly
                fetchAndDisplayTotalEmissions(currentUserId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        return rootView;
    }

    private void fetchAndDisplayTotalEmissions(String userId) {
        DatabaseReference userEmissionRef = FirebaseDatabase.getInstance("https://b07ecoproject-default-rtdb.firebaseio.com/")
                .getReference()
                .child("Users")
                .child(userId)
                .child("Emission");
        userEmissionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalEmissions = 0.0;
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                    int year = Integer.parseInt(Objects.requireNonNull(yearSnapshot.getKey()));
                    if (selectedTimePeriod == 2 && year == currentYear) {
                        totalEmissions += calculateEmissionsForYear(yearSnapshot);
                    }
                    if (selectedTimePeriod <= 1 && year == currentYear) {
                        for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                            int month = Integer.parseInt(Objects.requireNonNull(monthSnapshot.getKey()));
                            if (selectedTimePeriod == 1 && month == currentMonth) {
                                totalEmissions += calculateEmissionsForMonth(monthSnapshot);
                            }
                            if (selectedTimePeriod == 0 && month == currentMonth) {
                                for (DataSnapshot weekSnapshot : monthSnapshot.getChildren()) {
                                    int week = Integer.parseInt(Objects.requireNonNull(weekSnapshot.getKey()));
                                    if (week == currentWeek) {
                                        totalEmissions += calculateEmissionsForWeek(weekSnapshot);
                                    }
                                }
                            }
                        }
                    }
                }
                totalEmissionsText.setText("You’ve emitted " + totalEmissions + " kg CO2e this " + getPeriodString(selectedTimePeriod));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private double calculateEmissionsForMonth(DataSnapshot monthSnapshot) {
        double totalEmissions = 0.0;
        for (DataSnapshot weekSnapshot : monthSnapshot.getChildren()) {
            totalEmissions += calculateEmissionsForWeek(weekSnapshot);
        }
        return totalEmissions;
    }

    private double calculateEmissionsForWeek(DataSnapshot weekSnapshot) {
        double totalEmissions = 0.0;
        for (DataSnapshot daySnapshot : weekSnapshot.getChildren()) {
            totalEmissions += calculateEmissionsForDay(daySnapshot);
        }
        return totalEmissions;
    }

    private double calculateEmissionsForYear(DataSnapshot yearSnapshot) {
        double totalEmissions = 0.0;
        for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
            totalEmissions += calculateEmissionsForMonth(monthSnapshot);
        }
        return totalEmissions;
    }

    /** @noinspection DataFlowIssue*/
    private double calculateEmissionsForDay(DataSnapshot daySnapshot) {
        double totalEmissions = 0.0;
        DataSnapshot categorySnapshot = daySnapshot.child("categoryBreakdown");
        if (categorySnapshot.exists()) {
            for (DataSnapshot category : categorySnapshot.getChildren()) {
                totalEmissions += category.getValue(Double.class);
            }
        }
        return totalEmissions;
    }

    private String getPeriodString(int period) {
        switch (period) {
            case 0: return "week";
            case 1: return "month";
            case 2: return "year";
            default: return "time period";
        }
    }
}