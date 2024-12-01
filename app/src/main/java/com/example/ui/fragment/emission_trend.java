package com.example.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
public class emission_trend extends Fragment {

    private LineChart lineChart;
    private Spinner timePeriodSpinner;
    private DatabaseReference db;
    private String currentUserId;
    private int selectedTimePeriod = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emission_trend, container, false);
        lineChart = view.findViewById(R.id.lineChart);
        timePeriodSpinner = view.findViewById(R.id.timePeriodSpinner);
        db = FirebaseDatabase.getInstance("https://b07ecoproject-default-rtdb.firebaseio.com/").getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {currentUserId = currentUser.getUid();}
        else
        {currentUserId = "0";}
        setupSpinner();
        return view;
    }
    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_period_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timePeriodSpinner.setAdapter(adapter);
        timePeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTimePeriod = position; // 0 -> Daily, 1 -> Weekly, 2 -> Monthly
                fetchAndDisplayTrendData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void fetchAndDisplayTrendData() {
        db.child("Users").child(currentUserId).child("Emission")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Entry> entries = new ArrayList<>();
                        int xIndex = 0;
                        if (selectedTimePeriod == 0) {
                            int currentYear = getCurrentYear();
                            int currentMonth = getCurrentMonth();
                            int currentWeek = getCurrentWeek();
                            DataSnapshot weekSnapshot = dataSnapshot.child(String.valueOf(currentYear))
                                    .child(String.valueOf(currentMonth))
                                    .child(String.valueOf(currentWeek));
                            if (weekSnapshot.exists()) {
                                for (DataSnapshot daySnapshot : weekSnapshot.getChildren()) {
                                    float dailyEmission = getTotalEmissionFromDay(daySnapshot);
                                    entries.add(new Entry(xIndex++, dailyEmission));
                                }
                            }
                        } else if (selectedTimePeriod == 1) {
                            int currentYear = getCurrentYear();
                            int currentMonth = getCurrentMonth();
                            DataSnapshot monthSnapshot = dataSnapshot.child(String.valueOf(currentYear))
                                    .child(String.valueOf(currentMonth));
                            if (monthSnapshot.exists()) {
                                for (DataSnapshot weekSnapshot : monthSnapshot.getChildren()) {
                                    float weeklyEmission = getTotalEmissionFromWeek(weekSnapshot);
                                    entries.add(new Entry(xIndex++, weeklyEmission));
                                }
                            }
                        } else if (selectedTimePeriod == 2) {
                            int currentYear = getCurrentYear();
                            DataSnapshot yearSnapshot = dataSnapshot.child(String.valueOf(currentYear));
                            if (yearSnapshot.exists()) {
                                for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                                    float monthlyEmission = getTotalEmissionFromMonth(monthSnapshot);
                                    entries.add(new Entry(xIndex++, monthlyEmission));
                                }
                            }
                        }
                        displayLineChart(entries);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    /** @noinspection DataFlowIssue*/
    private float getTotalEmissionFromDay(DataSnapshot daySnapshot) {
        float total = 0;
        DataSnapshot categoryBreakdown = daySnapshot.child("categoryBreakdown");
        if (categoryBreakdown.exists()) {
            for (DataSnapshot category : categoryBreakdown.getChildren()) {
                total += category.getValue(Float.class);
            }
        }
        return total;
    }

    private float getTotalEmissionFromWeek(DataSnapshot weekSnapshot) {
        float total = 0;
        for (DataSnapshot daySnapshot : weekSnapshot.getChildren()) {
            total += getTotalEmissionFromDay(daySnapshot);
        }
        return total;
    }

    private float getTotalEmissionFromMonth(DataSnapshot monthSnapshot) {
        float total = 0;
        for (DataSnapshot weekSnapshot : monthSnapshot.getChildren()) {
            total += getTotalEmissionFromWeek(weekSnapshot);
        }
        return total;
    }

    private int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    private int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    private int getCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /** @noinspection deprecation*/
    private void displayLineChart(List<Entry> entries) {
        LineDataSet lineDataSet = new LineDataSet(entries, "CO2e Emissions Trend");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setColor(getResources().getColor(R.color.black));
        lineDataSet.setCircleColor(getResources().getColor(R.color.black));
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.invalidate();
    }
}