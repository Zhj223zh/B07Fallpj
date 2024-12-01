package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.b072024gr2.ecoproj.R;
import com.example.ui.fragment.emission_breakdown;
import com.example.ui.fragment.emission_comparison;
import com.example.ui.fragment.emission_overview;
import com.example.ui.fragment.emission_trend;
import com.google.firebase.database.FirebaseDatabase;

public class Emission_Dashboard extends AppCompatActivity {

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emission_dashboard);
        db = FirebaseDatabase.getInstance("https://b07ecoproject-default-rtdb.firebaseio.com/");
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadFragment_overview(new emission_overview());
        loadFragment_breakdown(new emission_breakdown());
        loadFragment_trend(new emission_trend());
        loadFragment_comparison(new emission_comparison());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    private void loadFragment_overview(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_emission_overview, fragment);
        fragmentTransaction.commit();
    }
    private void loadFragment_breakdown(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_emission_breakdown, fragment);
        fragmentTransaction.commit();
    }

    private void loadFragment_trend(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_emission_trend, fragment);
        fragmentTransaction.commit();
    }

    private void loadFragment_comparison(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_emission_comparison, fragment);
        fragmentTransaction.commit();
    }
    /** @noinspection deprecation*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}