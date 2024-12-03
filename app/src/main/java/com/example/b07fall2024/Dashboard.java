package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.b072024gr2.ecoproj.R;

public class Dashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button carbonCalculatorButton = findViewById(R.id.carbon_calculator_button);
        Button ecoTrackerButton = findViewById(R.id.eco_tracker_button);
        Button ecoGaugeButton = findViewById(R.id.eco_gauge_button);
        Button ecoHubButton = findViewById(R.id.eco_hub_button);

        carbonCalculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, StartActivity.class);
                startActivity(intent);
            }
        });

        ecoTrackerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ActivityMainLayout.class);
                startActivity(intent);
            }
        });

        ecoGaugeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Emission_Dashboard.class);
                startActivity(intent);
            }
        });

        ecoHubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, EcoHubActivity.class);
                startActivity(intent);
            }
        });
    }
}
