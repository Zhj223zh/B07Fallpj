package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
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

public class ConsumptionAndShopping extends AppCompatActivity {

    private double Shopping, Bill;
    private LinearLayout clothesContainer, elecContainer, othPurContainer, billsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumption_and_shopping);

        //container button
        elecContainer = findViewById(R.id.electronicsContainer);
        othPurContainer = findViewById(R.id.otherPurchasesContainer);
        billsContainer = findViewById(R.id.energyBillsContainer);

        findViewById(R.id.addElectronicsButton).setOnClickListener(this::addElectronics);
        findViewById(R.id.addOtherPurchasesButton).setOnClickListener(this::onAddOtherPurchases);
        findViewById(R.id.addBillButton).setOnClickListener(this::onAddBill);

        // jump to main
        Button nextButton = findViewById(R.id.activityListButton);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(ConsumptionAndShopping.this, ActivityMainLayout.class);
            startActivity(intent);

            UpdateToFirebase.getInstance().uploadDataToFirebase(ConsumptionAndShopping.this);


        });


        Button submitButton = findViewById(R.id.submitButton);
        //n
        submitButton.setOnClickListener(v -> {
            EmissionStorage emissionStorage = EmissionStorage.getInstance();
            Shopping = calShopping();
            Bill = calBill();
            emissionStorage.setShopping(Shopping);
            emissionStorage.setEnergyUse(Bill);
            Toast.makeText(ConsumptionAndShopping.this, "Shopping: " + Shopping + " kg CO2e" + "Bill: " + Bill + " kg CO2e", Toast.LENGTH_SHORT).show();
        });
    }

    public void addElectronics(View view) {
        LinearLayout newElectronicsLayout = new LinearLayout(this);
        newElectronicsLayout.setOrientation(LinearLayout.HORIZONTAL);
        newElectronicsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newElectronicsLayout.setPadding(0, 8, 0, 8);

        // Spinner
        Spinner newElectronicsType = new Spinner(this);
        ArrayAdapter<CharSequence> electronicsAdapter = ArrayAdapter.createFromResource(this,
                R.array.electronics_types, android.R.layout.simple_spinner_item);
        electronicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newElectronicsType.setAdapter(electronicsAdapter);
        newElectronicsLayout.addView(newElectronicsType);

        // EditText
        EditText newElectronicsInput = new EditText(this);
        newElectronicsInput.setHint("Number of devices");
        newElectronicsInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        newElectronicsInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        newElectronicsLayout.addView(newElectronicsInput);

        elecContainer.addView(newElectronicsLayout);
    }

    public void onAddOtherPurchases(View view) {
        LinearLayout newOtherPurchasesLayout = new LinearLayout(this);
        newOtherPurchasesLayout.setOrientation(LinearLayout.HORIZONTAL);
        newOtherPurchasesLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newOtherPurchasesLayout.setPadding(0, 8, 0, 8);

        // EitText
        Spinner spinner = new Spinner(this);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.other_purchase_type, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        newOtherPurchasesLayout.addView(spinner);


        // EditText
        EditText editText = new EditText(this);
        editText.setHint("Number of items");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newOtherPurchasesLayout.addView(editText);

        othPurContainer.addView(newOtherPurchasesLayout);
    }


    public void onAddBill(View view) {
        LinearLayout newBillLayout = new LinearLayout(this);
        newBillLayout.setOrientation(LinearLayout.HORIZONTAL);
        newBillLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newBillLayout.setPadding(0, 8, 0, 8);

        // Spinner
        Spinner newBillType = new Spinner(this);
        ArrayAdapter<CharSequence> billAdapter = ArrayAdapter.createFromResource(this,
                R.array.bill_types, android.R.layout.simple_spinner_item);
        billAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newBillType.setAdapter(billAdapter);
        newBillLayout.addView(newBillType);

        EditText editText = new EditText(this);
        editText.setHint("Bill amount (e.g., $150)");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newBillLayout.addView(editText);

        billsContainer.addView(newBillLayout);
    }

    public double calShopping() {
        // Emission factors for predefined categories
        Map<String, Double> electronicsEmissionFactors = new HashMap<>();
        electronicsEmissionFactors.put("Smartphone", 50.0);
        electronicsEmissionFactors.put("Laptop", 200.0);
        electronicsEmissionFactors.put("TV", 150.0);

        Map<String, Double> aDouble = new HashMap<>();
        aDouble.put("furniture", 100.0);
        aDouble.put("appliances", 300.0);

        double totalEmissions = 0.0;
        // Emission factor for clothes (single value)
        double clothesEmissionFactor = 10.0; //
        EditText numClothesInput = findViewById(R.id.numClothesInput);
        String numClothesStr = numClothesInput.getText().toString();
        int numClothes = numClothesStr.isEmpty() ? 0 : Integer.parseInt(numClothesStr);
        totalEmissions += clothesEmissionFactor * numClothes;


        // Calculate emissions from electronics
        for (int i = 0; i < elecContainer.getChildCount(); i++) {
            LinearLayout electronicsEntry = (LinearLayout) elecContainer.getChildAt(i);
            Spinner typeSpinner = (Spinner) electronicsEntry.getChildAt(0);
            EditText quantityInput = (EditText) electronicsEntry.getChildAt(1);

            String type = typeSpinner.getSelectedItem().toString();
            String quantityStr = quantityInput.getText().toString();
            int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);

            if (electronicsEmissionFactors.containsKey(type)) {
                totalEmissions += quantity * electronicsEmissionFactors.get(type);
            }
        }


        for (int i = 0; i < othPurContainer.getChildCount(); i++) {
            LinearLayout purchaseEntry = (LinearLayout) othPurContainer.getChildAt(i);
            EditText typeInput = (EditText) purchaseEntry.getChildAt(0);
            EditText quantityInput = (EditText) purchaseEntry.getChildAt(1);

            String type = typeInput.getText().toString().toLowerCase();
            String quantityStr = quantityInput.getText().toString();

            int quantity;
            if (quantityStr.isEmpty()) {
                quantity = 0;
            } else {
                quantity = Integer.parseInt(quantityStr);
            }


            if (aDouble.containsKey(type)) {
                totalEmissions += quantity * aDouble.get(type);
            }
        }

        // Log the total emissions for debugging
        Log.d("Emissions", "Total Emissions: " + totalEmissions + " kg CO2e");

        return totalEmissions;
    }

    public double calBill()
    {
        Map<String, Double> aDouble = new HashMap<>();
        aDouble.put("Electricity", 0.5); // kg CO2e per dollar
        aDouble.put("Gas", 2.0);        // kg CO2e per dollar
        aDouble.put("Water", 0.2);      // kg CO2e per dollar

        double total = 0.0;

        for (int i = 0; i < billsContainer.getChildCount(); i++) {
            LinearLayout billEntry = (LinearLayout) billsContainer.getChildAt(i);

            Spinner billTypeSpinner = (Spinner) billEntry.getChildAt(0);
            String billType = billTypeSpinner.getSelectedItem().toString();

            EditText billAmountInput = (EditText) billEntry.getChildAt(1);
            String billAmountStr = billAmountInput.getText().toString();
            double billAmount;

            if (billAmountStr.isEmpty()) {
                billAmount = 0.0;
            } else {
                billAmount = Double.parseDouble(billAmountStr);
            }


            // Calculate emissions for this bill if the type is in the emission factors
            if (aDouble.containsKey(billType)) {
                double emissionFactor = aDouble.get(billType);
                double billEmissions = billAmount * emissionFactor;
                total += billEmissions;

                Log.d("BillEmissions", "Bill Type: " + billType + ", Amount: $" + billAmount +
                        ", Emissions: " + billEmissions + " kg CO2e");
            }
        }

        Log.d("BillEmissions", "Total Bill Emissions: " + total + " kg CO2e");

        return total;
    }


}
