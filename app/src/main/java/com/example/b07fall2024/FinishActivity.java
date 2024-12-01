package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        final AppCompatButton restartBtn = findViewById(R.id.restart_quiz);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final AppCompatButton endQuizBtn = findViewById(R.id.end_quiz);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
                finish();
            }

        });
    }

    private void submitData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("com.example.b07fall2024");
        HashMap<String, Float> emissionsByCategory = questionbank.getEmissionsByCategory();

        ref.child("users").child(userId).child("AnualCF").child("EnergyUse").setValue(emissionsByCategory.get("housing"));
        ref.child("users").child(userId).child("AnualCF").child("FoodConsumption").setValue(emissionsByCategory.get("food"));
        ref.child("users").child(userId).child("AnualCF").child("Shopping").setValue(emissionsByCategory.get("consumption"));
        ref.child("users").child(userId).child("AnualCF").child("Transportation").setValue(emissionsByCategory.get("transportation"));

        ref.child("users").child(userId).child("Location").child("Country").setValue(country);

    }
}
