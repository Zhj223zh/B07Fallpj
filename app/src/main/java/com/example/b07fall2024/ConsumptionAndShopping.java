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
import com.example.b07fall2024.UpdateToFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ConsumptionAndShopping extends AppCompatActivity {

    private double Shopping, Bill;
    private LinearLayout clothesContainer, electronicsContainer, otherPurchasesContainer, energyBillsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumption_and_shopping); // 假设XML文件名是 `consumption_and_shopping.xml`

        // 获取各个容器的引用
        electronicsContainer = findViewById(R.id.electronicsContainer);
        otherPurchasesContainer = findViewById(R.id.otherPurchasesContainer);
        energyBillsContainer = findViewById(R.id.energyBillsContainer);

        // 添加按钮点击监听器
        findViewById(R.id.addElectronicsButton).setOnClickListener(this::onAddElectronicsClicked);
        findViewById(R.id.addOtherPurchasesButton).setOnClickListener(this::onAddOtherPurchasesClicked);
        findViewById(R.id.addBillButton).setOnClickListener(this::onAddBillClicked);

        // jump to main
        Button nextButton = findViewById(R.id.activityListButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsumptionAndShopping.this, ActivityMainLayout.class);
                startActivity(intent);

                UpdateToFirebase.getInstance().uploadDataToFirebase(ConsumptionAndShopping.this);


            }
        });


        Button submitButton = findViewById(R.id.submitButton);
        // onclicked button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmissionStorage emissionStorage = EmissionStorage.getInstance();
                Shopping = calculateTotalEmissions();
                Bill = calculateBillEmissions();
                emissionStorage.setShopping(Shopping);
                emissionStorage.setEnergyUse(Bill);
                Toast.makeText(ConsumptionAndShopping.this, "Shopping: " + Shopping + " kg CO2e" + "Bill: " + Bill + " kg CO2e", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 添加电子产品购买条目
    public void onAddElectronicsClicked(View view) {
        LinearLayout newElectronicsLayout = new LinearLayout(this);
        newElectronicsLayout.setOrientation(LinearLayout.HORIZONTAL);
        newElectronicsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newElectronicsLayout.setPadding(0, 8, 0, 8);

        // 创建新的 Spinner
        Spinner newElectronicsTypeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> electronicsAdapter = ArrayAdapter.createFromResource(this,
                R.array.electronics_types, android.R.layout.simple_spinner_item);
        electronicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newElectronicsTypeSpinner.setAdapter(electronicsAdapter);
        newElectronicsLayout.addView(newElectronicsTypeSpinner);

        // 创建新的 EditText 用于输入数量
        EditText newElectronicsInput = new EditText(this);
        newElectronicsInput.setHint("Number of devices");
        newElectronicsInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        newElectronicsInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newElectronicsLayout.addView(newElectronicsInput);

        electronicsContainer.addView(newElectronicsLayout);
    }

    // 添加其他购买条目
    public void onAddOtherPurchasesClicked(View view) {
        LinearLayout newOtherPurchasesLayout = new LinearLayout(this);
        newOtherPurchasesLayout.setOrientation(LinearLayout.HORIZONTAL);
        newOtherPurchasesLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newOtherPurchasesLayout.setPadding(0, 8, 0, 8);

        // 创建新的 EditText 用于输入购买类型
        Spinner newPurchaseTypeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> otherPurchaseAdapter = ArrayAdapter.createFromResource(this,
                R.array.other_purchase_type, android.R.layout.simple_spinner_item);
        otherPurchaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newPurchaseTypeSpinner.setAdapter(otherPurchaseAdapter);
        newOtherPurchasesLayout.addView(newPurchaseTypeSpinner);


        // 创建新的 EditText 用于输入数量
        EditText newPurchaseCountInput = new EditText(this);
        newPurchaseCountInput.setHint("Number of items");
        newPurchaseCountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        newPurchaseCountInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newOtherPurchasesLayout.addView(newPurchaseCountInput);

        otherPurchasesContainer.addView(newOtherPurchasesLayout);
    }

    // 添加能源账单条目
    public void onAddBillClicked(View view) {
        LinearLayout newBillLayout = new LinearLayout(this);
        newBillLayout.setOrientation(LinearLayout.HORIZONTAL);
        newBillLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newBillLayout.setPadding(0, 8, 0, 8);

        // 创建新的 Spinner 用于选择账单类型
        Spinner newBillTypeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> billAdapter = ArrayAdapter.createFromResource(this,
                R.array.bill_types, android.R.layout.simple_spinner_item);
        billAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newBillTypeSpinner.setAdapter(billAdapter);
        newBillLayout.addView(newBillTypeSpinner);

        // 创建新的 EditText 用于输入账单金额
        EditText newBillAmountInput = new EditText(this);
        newBillAmountInput.setHint("Bill amount (e.g., $150)");
        newBillAmountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newBillAmountInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newBillLayout.addView(newBillAmountInput);

        energyBillsContainer.addView(newBillLayout);
    }

    public double calculateTotalEmissions() {
        // Emission factors for predefined categories
        Map<String, Double> electronicsEmissionFactors = new HashMap<>();
        electronicsEmissionFactors.put("Smartphone", 50.0);
        electronicsEmissionFactors.put("Laptop", 200.0);
        electronicsEmissionFactors.put("TV", 150.0);

        Map<String, Double> otherPurchaseEmissionFactors = new HashMap<>();
        otherPurchaseEmissionFactors.put("furniture", 100.0);
        otherPurchaseEmissionFactors.put("appliances", 300.0);

        double totalEmissions = 0.0;
        // Emission factor for clothes (single value)
        double clothesEmissionFactor = 10.0; //
        EditText numClothesInput = findViewById(R.id.numClothesInput);
        String numClothesStr = numClothesInput.getText().toString();
        int numClothes = numClothesStr.isEmpty() ? 0 : Integer.parseInt(numClothesStr);
        totalEmissions += clothesEmissionFactor * numClothes;


        // Calculate emissions from electronics
        for (int i = 0; i < electronicsContainer.getChildCount(); i++) {
            LinearLayout electronicsEntry = (LinearLayout) electronicsContainer.getChildAt(i);
            Spinner typeSpinner = (Spinner) electronicsEntry.getChildAt(0);
            EditText quantityInput = (EditText) electronicsEntry.getChildAt(1);

            String type = typeSpinner.getSelectedItem().toString();
            String quantityStr = quantityInput.getText().toString();
            int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);

            if (electronicsEmissionFactors.containsKey(type)) {
                totalEmissions += quantity * electronicsEmissionFactors.get(type);
            }
        }

        // Calculate emissions from other purchases
        for (int i = 0; i < otherPurchasesContainer.getChildCount(); i++) {
            LinearLayout purchaseEntry = (LinearLayout) otherPurchasesContainer.getChildAt(i);
            EditText typeInput = (EditText) purchaseEntry.getChildAt(0);
            EditText quantityInput = (EditText) purchaseEntry.getChildAt(1);

            String type = typeInput.getText().toString().toLowerCase();
            String quantityStr = quantityInput.getText().toString();
            int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);

            if (otherPurchaseEmissionFactors.containsKey(type)) {
                totalEmissions += quantity * otherPurchaseEmissionFactors.get(type);
            }
        }

        // Log the total emissions for debugging
        Log.d("Emissions", "Total Emissions: " + totalEmissions + " kg CO2e");

        return totalEmissions;
    }

    public double calculateBillEmissions() {
        // Emission factors for predefined bill types
        Map<String, Double> billEmissionFactors = new HashMap<>();
        billEmissionFactors.put("Electricity", 0.5); // kg CO2e per dollar
        billEmissionFactors.put("Gas", 2.0);        // kg CO2e per dollar
        billEmissionFactors.put("Water", 0.2);      // kg CO2e per dollar

        double totalBillEmissions = 0.0;

        // Loop through each bill entry in the container
        for (int i = 0; i < energyBillsContainer.getChildCount(); i++) {
            LinearLayout billEntry = (LinearLayout) energyBillsContainer.getChildAt(i);

            // Get the type of bill from the spinner
            Spinner billTypeSpinner = (Spinner) billEntry.getChildAt(0);
            String billType = billTypeSpinner.getSelectedItem().toString();

            // Get the bill amount from the EditText
            EditText billAmountInput = (EditText) billEntry.getChildAt(1);
            String billAmountStr = billAmountInput.getText().toString();
            double billAmount = billAmountStr.isEmpty() ? 0.0 : Double.parseDouble(billAmountStr);

            // Calculate emissions for this bill if the type is in the emission factors
            if (billEmissionFactors.containsKey(billType)) {
                double emissionFactor = billEmissionFactors.get(billType);
                double billEmissions = billAmount * emissionFactor;
                totalBillEmissions += billEmissions;

                // Log for debugging
                Log.d("BillEmissions", "Bill Type: " + billType + ", Amount: $" + billAmount +
                        ", Emissions: " + billEmissions + " kg CO2e");
            }
        }

        // Log the total emissions for debugging
        Log.d("BillEmissions", "Total Bill Emissions: " + totalBillEmissions + " kg CO2e");

        return totalBillEmissions;
    }


}
