package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.b072024gr2.ecoproj.R;

public class EcoHubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecohub);

        Button btn_learning_resources = findViewById(R.id.btn_learning_resources);
        Button btn_market_trends = findViewById(R.id.btn_market_trends);
        Button btn_product_highlights = findViewById(R.id.btn_product_highlights);
        Button btn_backtoDashboard = findViewById(R.id.btn_backtoDashboard);

//        carbonCalculatorButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Dashboard.this, CarbonCalculatorActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        ecoTrackerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Dashboard.this, EcoTrackerActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        ecoGaugeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Dashboard.this, Emission_Dashboard.class);
//                startActivity(intent);
//            }
//        });

        btn_backtoDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EcoHubActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }
}
