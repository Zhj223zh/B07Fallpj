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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.b072024gr2.ecoproj.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.b072024gr2.ecoproj.R;

public class FoodConsumption extends AppCompatActivity{
    private LinearLayout mealContainer;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private List<LinearLayout> mealEntries = new ArrayList<>();
    private double FoodEmission;

    // get date
    String year = String.valueOf(DateStorage.getInstance().getYear());
    String month = String.valueOf(DateStorage.getInstance().getMonth());
    String week = String.valueOf(DateStorage.getInstance().getWeek());
    String day = String.valueOf(DateStorage.getInstance().getDay());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_consumption);

        // jump to activityMainLayout
        Button activityListButton = findViewById(R.id.activityListButton);
        activityListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodConsumption.this, ActivityMainLayout.class);
                startActivity(intent);
            }
        });

        //create new meal
        mealContainer = findViewById(R.id.mealContainer);


        Button submitButton = findViewById(R.id.submitButton);
        // onclicked button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmissionStorage emissionStorage = EmissionStorage.getInstance();
                FoodEmission = onSubmitButtonClicked();
                Toast.makeText(FoodConsumption.this, "碳排放量: " + FoodEmission + " kg CO2e", Toast.LENGTH_SHORT).show();
                emissionStorage.setFoodConsumption(FoodEmission);
            }
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

        // check all the items to see the input
        for (LinearLayout mealLayout : mealEntries) {
            // gain the input of type and number
            Spinner mealTypeSpinner = (Spinner) mealLayout.getChildAt(0);
            EditText servingsInput = (EditText) mealLayout.getChildAt(1);

            // translate the type
            String mealType = mealTypeSpinner.getSelectedItem().toString();
            String servingsStr = servingsInput.getText().toString();

            int numberOfServings = 0;
            if (!servingsStr.isEmpty()) {
                numberOfServings = Integer.parseInt(servingsStr);
            }

            // calculate the carbon emission
            if (emissionFactors.containsKey(mealType)) {
                double emissionFactor = emissionFactors.get(mealType);
                double mealEmissions = numberOfServings * servingWeightKg * emissionFactor;
                totalEmissions += mealEmissions;

                // print
                Log.d("MealData", "Meal Type: " + mealType + ", Servings: " + numberOfServings +
                        ", Emissions: " + mealEmissions + " kg CO2e");
            }
        }
        return totalEmissions;
    }


    //create a new meal
    public void onAddClicked(View view) {
        // Create a new LinearLayout for the new meal entry
        LinearLayout newMealLayout = new LinearLayout(this);
        newMealLayout.setOrientation(LinearLayout.HORIZONTAL);
        newMealLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newMealLayout.setPadding(0, 8, 0, 8);

        // Create a new Spinner for meal type
        Spinner newMealTypeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newMealTypeSpinner.setAdapter(adapter);
        newMealLayout.addView(newMealTypeSpinner);

        // Create a new EditText for number of servings
        EditText newServingsInput = new EditText(this);
        newServingsInput.setHint("Number of Servings");
        newServingsInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        newServingsInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newMealLayout.addView(newServingsInput);

        // Add the new meal layout to the container
        // LinearLayout mealcontainer = findViewById(R.id.mealContainer);
        mealContainer.addView(newMealLayout);

        mealEntries.add(newMealLayout);
    }

    // upload data to Firebase
    private void uploadDataToFirebase(String FoodEmission) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(FoodConsumption.this, "the user hasn't logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        //  gain the user ID
        String userId = user.getUid();


        Map<String, Object> data = new HashMap<>();
        data.put("totalEmission", FoodEmission);
        data.put("type", "FoodConsumption");

        // define the path of saving the data
        DatabaseReference categoryBreakdownRef = databaseRef.child("users").child(userId).child(year).child(month).child(week).child(day).child("categoryBreakdown").child("FoodConsumption");
        // check if the path exist
        categoryBreakdownRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // same date, exists FoodConsumption：alternate FoodConsumption subclass
                    categoryBreakdownRef.setValue(data)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(FoodConsumption.this, "succeed to upload data", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FoodConsumption.this, "fail to upload data", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // same date, no FoodConsumption：create FoodConsumption and add it to subclass
                    categoryBreakdownRef.setValue(data)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(FoodConsumption.this, "succeed to upload data", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FoodConsumption.this, "fail to upload data", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodConsumption.this, "fail to check data " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }
}