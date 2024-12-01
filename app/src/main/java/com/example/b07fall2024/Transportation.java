package com.example.b07fall2024;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.b072024gr2.ecoproj.R;
import java.util.HashMap;
import java.util.Map;
//detail change now
public class Transportation extends AppCompatActivity {
    private LinearLayout VeSec, flightSec, cWSec,pubTraSec;

    private double TransEmission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transportation);

        // container
        VeSec = findViewById(R.id.vehicleContainer);
        flightSec = findViewById(R.id.flightContainer);
        cWSec = findViewById(R.id.cyclingWalkingContainer);
        pubTraSec = findViewById(R.id.publicTransportContainer);

        // jump to main
        Button activityListButton = findViewById(R.id.activityListButton);
        activityListButton.setOnClickListener(v -> {
            Intent intent = new Intent(Transportation.this, ActivityMainLayout.class);
            startActivity(intent);
            UpdateToFirebase.getInstance().uploadDataToFirebase(Transportation.this);

        });

        // to foodConsumption
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(Transportation.this, FoodConsumption.class);
            startActivity(intent);
        });

        // add button listener
        findViewById(R.id.addVehicleButton).setOnClickListener(this::addVehicle);
        findViewById(R.id.addTransportButton).setOnClickListener(this::addPublic);
        findViewById(R.id.addFlightButton).setOnClickListener(this::addFlight);
        findViewById(R.id.addCyclingButton).setOnClickListener(this::addCyclingW);

        // submit button
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            //call cal
            TransEmission = calTransEmissions();
            Toast.makeText(Transportation.this, "emssion: " + TransEmission + " kg CO2e", Toast.LENGTH_SHORT).show();

            //store
            EmissionStorage emissionStorage = EmissionStorage.getInstance();
            emissionStorage.setTransportation(TransEmission);
        });
    }

    // cal
    public double calTransEmissions() {

        Map<String, Double> emissionFactors = new HashMap<>();
        emissionFactors.put("Gasoline(km)", 0.24); // kg CO2 per km
        emissionFactors.put("Diesel(km)", 0.27);
        emissionFactors.put("Hybrid(km)", 0.16);
        emissionFactors.put("Electric(km)", 0.05);
        emissionFactors.put("Gasoline(mile)", 2.31); // kg per mile
        emissionFactors.put("Diesel(mile)", 2.68);
        emissionFactors.put("Hybrid(mile)", 1.5);
        emissionFactors.put("Electric(mile)", 0.5);
        emissionFactors.put("Bus", 0.08); // kg per mile
        emissionFactors.put("Train", 0.04);
        emissionFactors.put("Subway", 0.06);
        emissionFactors.put("(km)", 0.0);
        emissionFactors.put("(mile)", 0.0);
        emissionFactors.put("Short-Haul (<1,500 km)", 225.0);
        emissionFactors.put("Long-Haul (>1,500 km)", 825.0);


        double totalEmissions = 0.0;

        //add each sec
        totalEmissions += calSecEmission(VeSec, emissionFactors);
        totalEmissions += calSecEmission(pubTraSec, emissionFactors);
        totalEmissions += calSecEmission(flightSec, emissionFactors);
        totalEmissions += calSecEmission(cWSec, emissionFactors);

        return totalEmissions;
    }

    //
    private double calSecEmission(LinearLayout section, Map<String, Double> emissionFactors) {
        double sectionEmissions = 0.0;

        for (int i = 0; i < section.getChildCount(); i++) {
            View view = section.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) view;

                Spinner typeSpinner = (Spinner) layout.getChildAt(0);
                EditText inputField = (EditText) layout.getChildAt(1);

                String Type = typeSpinner.getSelectedItem().toString();
                String input = inputField.getText().toString().trim();//empty
                double value = 0;

                if (!input.isEmpty()) {
                    try {
                        value = Double.parseDouble(input);

                    } catch (NumberFormatException e) {
                        value = 0;
                    }
                }


                double emissionFactor = emissionFactors.get(Type);
                sectionEmissions += value * emissionFactor;
            }
        }

        return sectionEmissions;
    }


    // vehicle add
    public void addVehicle(View view) {
        LinearLayout vehicleL = new LinearLayout(this);
        vehicleL.setOrientation(LinearLayout.HORIZONTAL);
        vehicleL.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        vehicleL.setPadding(0, 8, 0, 8);

        // Spinner
        Spinner vehS = new Spinner(this);
        ArrayAdapter<CharSequence> vehicleA = ArrayAdapter.createFromResource(this,
                R.array.vehicle_types, android.R.layout.simple_spinner_item);
        vehicleA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehS.setAdapter(vehicleA);
        vehicleL.addView(vehS);

        // EditText
        EditText dInput = new EditText(this);
        dInput.setHint("enter distance driven");
        dInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        vehicleL.addView(dInput);

        VeSec.addView(vehicleL);
    }

    // public trans
    public void addPublic(View view) {

        LinearLayout publicTransL = new LinearLayout(this);
        publicTransL.setOrientation(LinearLayout.HORIZONTAL);
        publicTransL.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        publicTransL.setPadding(0, 8, 0, 8);

        // Spinner
        Spinner tranS = new Spinner(this);
        ArrayAdapter<CharSequence> transA = ArrayAdapter.createFromResource(this,
                R.array.public_transport_types, android.R.layout.simple_spinner_item);
        transA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tranS.setAdapter(transA);
        publicTransL.addView(tranS);

        EditText transTimeInput = new EditText(this);
        transTimeInput.setHint("time Spent (in hours)");
        transTimeInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        transTimeInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        publicTransL.addView(transTimeInput);


        pubTraSec.addView(publicTransL);
    }

    //flight add
    public void addFlight(View view) {
        // LinearLayout
        LinearLayout flightL = new LinearLayout(this);
        flightL.setOrientation(LinearLayout.HORIZONTAL);
        flightL.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        flightL.setPadding(0, 8, 0, 8);

        // Spinner
        Spinner flightS = new Spinner(this);
        ArrayAdapter<CharSequence> flightA = ArrayAdapter.createFromResource(this,
                R.array.flight_types, android.R.layout.simple_spinner_item);
        flightA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flightS.setAdapter(flightA);
        flightL.addView(flightS);

        // EditText
        EditText flightCount = new EditText(this);
        flightCount.setHint("Enter number of flights");
        flightCount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        flightCount.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        flightL.addView(flightCount);

        flightSec.addView(flightL);
    }

    public void addCyclingW(View view) {
        LinearLayout cyclingWL = new LinearLayout(this);
        cyclingWL.setOrientation(LinearLayout.HORIZONTAL);
        cyclingWL.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        cyclingWL.setPadding(0, 8, 0, 8);

        // Spinner
        Spinner cyclingWS = new Spinner(this);
        ArrayAdapter<CharSequence> cyclingWA = ArrayAdapter.createFromResource(this,
                R.array.cycling_walking_types, android.R.layout.simple_spinner_item);
        cyclingWA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cyclingWS.setAdapter(cyclingWA);
        cyclingWL.addView(cyclingWS);

        //EditText
        EditText cyclingWCount = new EditText(this);
        cyclingWCount.setHint("Distance cycled or walked");
        cyclingWCount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        cyclingWCount.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        cyclingWL.addView(cyclingWCount);

        cWSec.addView(cyclingWL);
    }
}
