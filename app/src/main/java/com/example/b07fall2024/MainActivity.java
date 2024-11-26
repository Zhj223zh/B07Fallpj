package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//Source: How to create a quiz app in android studio by Learnoset- learn coding online
public class MainActivity extends AppCompatActivity {
    private Spinner spinnerCategory;
    //stores the selected category of country
    String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Button and Spinner
        final Button startBtn = findViewById(R.id.start_quiz);
        spinnerCategory = findViewById(R.id.spinnerCategory);  // Corrected this line

        // Create an adapter for the Spinner using the categories array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
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
}