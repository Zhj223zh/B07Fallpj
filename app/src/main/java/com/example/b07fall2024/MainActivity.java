package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerCategory;
    // stores the selected category of country
    String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Button and Spinner
        final Button startBtn = findViewById(R.id.start_quiz);
        spinnerCategory = findViewById(R.id.spinnerCategory);  // Corrected this line

        // Load categories from assets (replace static array)
        List<String> categoriesList = loadCategoriesFromAssets();

        // Check if the file was empty
        if (categoriesList.isEmpty()) {
            Toast.makeText(this, "No categories available in the file", Toast.LENGTH_SHORT).show();
            return;  // Exit the method if no categories are found
        }

        // Create an adapter for the Spinner using the categories from the text file
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set OnClickListener for the Button
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected category from the Spinner
                selectedCategory = spinnerCategory.getSelectedItem().toString();

                // Check if the user has selected a category
                if (selectedCategory.isEmpty() || selectedCategory.equals("Select Category")) {
                    // Show a message if no category is selected
                    Toast.makeText(MainActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an Intent to start the QuizActivity and pass the selected category
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra("selected category", selectedCategory); // Pass the selected category
                    startActivity(intent);
                }
            }
        });
    }

    // Method to load categories from assets
    private List<String> loadCategoriesFromAssets() {
        List<String> categoriesList = new ArrayList<>();
        try {
            // Open the text file from the assets folder
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("country.csv")));
            String line;
            while ((line = reader.readLine()) != null) {
                // Add each line to the list if it's not empty
                if (!line.trim().isEmpty()) {
                    categoriesList.add(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading categories from file", Toast.LENGTH_SHORT).show();
        }
        return categoriesList;
    }
}
