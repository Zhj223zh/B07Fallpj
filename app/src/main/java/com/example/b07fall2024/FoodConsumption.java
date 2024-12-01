package com.example.b07fall2024;

import android.content.Intent;
import android.text.InputType;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodConsumption extends AppCompatActivity{
    private LinearLayout mealContainer;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private final List<LinearLayout> mealEntries = new ArrayList<>();
    private double FoodEmission;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_consumption);

        // jump
        Button activityListButton = findViewById(R.id.activityListButton);
        activityListButton.setOnClickListener(v -> {
            Intent intent = new Intent(FoodConsumption.this, ActivityMainLayout.class);
            startActivity(intent);
            UpdateToFirebase.getInstance().uploadDataToFirebase(FoodConsumption.this);
        });

        // jump to c&s
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
        Intent intent = new Intent(FoodConsumption.this, ConsumptionAndShopping.class);
        startActivity(intent);
        });

        //container
        mealContainer = findViewById(R.id.mealContainer);
        Button submitButton = findViewById(R.id.submitButton);
        // onclicked button
        submitButton.setOnClickListener(v -> {
            EmissionStorage emissionStorage = EmissionStorage.getInstance();
            FoodEmission = onSubmitButtonClicked();
            Toast.makeText(FoodConsumption.this, "碳排放量: " + FoodEmission + " kg CO2e", Toast.LENGTH_SHORT).show();
            emissionStorage.setFoodConsumption(FoodEmission);
        });
    }
    // private
    public double onSubmitButtonClicked() {
        // the assumption of CO2
        Map<String, Double> emissionFactors = new HashMap<>();
        emissionFactors.put("Beef", 60.0);
        emissionFactors.put("Pork", 7.0);
        emissionFactors.put("Chicken", 6.0);
        emissionFactors.put("Fish", 5.0);
        emissionFactors.put("Plant-Based", 2.0);
        double servingWeightKg = 0.25;
        double totalEmissions = 0.0;


        EditText originMealNum = findViewById(R.id.mealServingsInput);
        Spinner orginMealType = findViewById((R.id.mealTypeSpinner));
        String originMealTypeStr = orginMealType.getSelectedItem().toString();
        String originMealNumStr = originMealNum.getText().toString();
        int originNumberOfServings = 0;
        if (!originMealNumStr.isEmpty()) {
            originNumberOfServings = Integer.parseInt(originMealNumStr);
        }


        if (emissionFactors.containsKey(originMealTypeStr)) {
            double OrgineMissionFactor = emissionFactors.get(originMealTypeStr);
            double originMealEmissions = originNumberOfServings * servingWeightKg * OrgineMissionFactor;
            totalEmissions += originMealEmissions;
        }

        for (LinearLayout mealLayout : mealEntries) {
            Spinner mealTypeSpinner = (Spinner) mealLayout.getChildAt(0);
            EditText servingsInput = (EditText) mealLayout.getChildAt(1);
            // translate the type
            String mealType = mealTypeSpinner.getSelectedItem().toString();
            String servingsStr = servingsInput.getText().toString();
            int numberOfServings = 0;
            if (!servingsStr.isEmpty()) {
                numberOfServings = Integer.parseInt(servingsStr);
            }
            // calculate
            if (emissionFactors.containsKey(mealType)) {
                double emissionFactor = emissionFactors.get(mealType);
                double mealEmissions = numberOfServings * servingWeightKg * emissionFactor;
                totalEmissions += mealEmissions;
            }
        }
        return totalEmissions;
    }


    public void onAdd(View view) {
        LinearLayout newMealLayout = new LinearLayout(this)
                ;
        newMealLayout.setOrientation(LinearLayout.HORIZONTAL);
        newMealLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newMealLayout.setPadding(0, 8, 0, 8);

        Spinner spinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        newMealLayout.addView(spinner);

        EditText editText = new EditText(this);
        editText.setHint("Number of Servings");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newMealLayout.addView(editText);
        mealContainer.addView(newMealLayout);
        mealEntries.add(newMealLayout);
    }
}