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
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Transportation extends AppCompatActivity {
    private LinearLayout vehicleSection,flightSection, cyclingWalkingSection,publicTransportSection;

    private double TransportationEmission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transportation);

        // 获取各个子部分的容器
        vehicleSection = findViewById(R.id.vehicleContainer);
        flightSection = findViewById(R.id.flightContainer);
        cyclingWalkingSection = findViewById(R.id.cyclingWalkingContainer);
        publicTransportSection = findViewById(R.id.publicTransportContainer);

        // 跳转到主活动页面
        Button activityListButton = findViewById(R.id.activityListButton);
        activityListButton.setOnClickListener(v -> {
            Intent intent = new Intent(Transportation.this, ActivityMainLayout.class);
            startActivity(intent);
            UpdateToFirebase.getInstance().uploadDataToFirebase(Transportation.this);

        });

        // 跳转到 FoodConsumption 活动
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(Transportation.this, FoodConsumption.class);
            startActivity(intent);
        });

        // 获取添加按钮并设置监听器
        findViewById(R.id.addVehicleButton).setOnClickListener(this::onAddVehicleClicked);
        findViewById(R.id.addTransportButton).setOnClickListener(this::onAddPublicClicked);
        findViewById(R.id.addFlightButton).setOnClickListener(this::onAddFlightClicked);
        findViewById(R.id.addCyclingButton).setOnClickListener(this::onAddCyclingWakingClicked);

        // 提交按钮监听器
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            TransportationEmission = calculateTransportationEmissions();
            Toast.makeText(Transportation.this, "碳排放量: " + TransportationEmission + " kg CO2e", Toast.LENGTH_SHORT).show();
            EmissionStorage emissionStorage = EmissionStorage.getInstance();
            emissionStorage.setTransportation(TransportationEmission);
        });
    }

    // 计算交通碳排放量
    public double calculateTransportationEmissions() {
        // 碳排放系数
        Map<String, Double> emissionFactors = new HashMap<>();
        emissionFactors.put("Gasoline(km)", 1.44); // kg CO2 per km
        emissionFactors.put("Diesel(km)", 1.66);
        emissionFactors.put("Hybrid(km)", 0.932);
        emissionFactors.put("Electric(km)", 0.311);
        emissionFactors.put("Gasoline(mile)", 2.31); // kg CO2 per mile
        emissionFactors.put("Diesel(mile)", 2.68);
        emissionFactors.put("Hybrid(mile)", 1.5);
        emissionFactors.put("Electric(mile)", 0.5);
        emissionFactors.put("Bus", 0.08); // kg CO2 per passenger mile
        emissionFactors.put("Train", 0.04);
        emissionFactors.put("Subway", 0.06);
        emissionFactors.put("(km)", 0.0); // No emissions for cycling
        emissionFactors.put("(mile)", 0.0);
        emissionFactors.put("Short-Haul (<1,500 km)", 0.25); // kg CO2 per km
        emissionFactors.put("Long-Haul (>1,500 km)", 0.15);


        double totalEmissions = 0.0;

        // 分别遍历各个部分
        totalEmissions += calculateSectionEmissions(vehicleSection, emissionFactors);
        totalEmissions += calculateSectionEmissions(publicTransportSection, emissionFactors);
        totalEmissions += calculateSectionEmissions(flightSection, emissionFactors);
        totalEmissions += calculateSectionEmissions(cyclingWalkingSection, emissionFactors);

        return totalEmissions;
    }

    private double calculateSectionEmissions(LinearLayout section, Map<String, Double> emissionFactors) {
        double sectionEmissions = 0.0;

        for (int i = 0; i < section.getChildCount(); i++) {
            View view = section.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) view;

                // 读取 Spinner 和 EditText 的值
                Spinner typeSpinner = (Spinner) layout.getChildAt(0);
                EditText inputField = (EditText) layout.getChildAt(1);

                String selectedType = typeSpinner.getSelectedItem().toString();
                String inputStr = inputField.getText().toString().trim(); // 使用 trim() 移除前后空格
                double value = 0;

                if (!inputStr.isEmpty()) {
                    try {
                        value = Double.parseDouble(inputStr);
                    } catch (NumberFormatException e) {
                        Log.e("EmissionCalculation", "Invalid input for " + selectedType + ": " + inputStr);
                        value = 0; // 如果转换失败，默认设置为 0
                    }
                }

                // 根据类型获取排放因子并计算排放
                if (emissionFactors.containsKey(selectedType)) {
                    double emissionFactor = emissionFactors.get(selectedType);
                    sectionEmissions += value * emissionFactor;
                    Log.d("EmissionCalculation", "Adding Emission: " + (value * emissionFactor) + " kg CO2e for type " + selectedType);
                } else {
                    Log.d("EmissionCalculation", "Emission factor not found for type: " + selectedType);
                }
            }
        }

        Log.d("EmissionCalculation", "Total emissions for this section: " + sectionEmissions);
        return sectionEmissions;
    }


    // 添加私人车辆条目
    public void onAddVehicleClicked(View view) {
        // 创建新的 LinearLayout
        LinearLayout newVehicleLayout = new LinearLayout(this);
        newVehicleLayout.setOrientation(LinearLayout.HORIZONTAL);
        newVehicleLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newVehicleLayout.setPadding(0, 8, 0, 8);

        // 创建新的 Spinner 用于车辆类型选择
        Spinner newVehicleTypeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> vehicleAdapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_types, android.R.layout.simple_spinner_item);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newVehicleTypeSpinner.setAdapter(vehicleAdapter);
        newVehicleLayout.addView(newVehicleTypeSpinner);

        // 创建新的 EditText 用于输入行驶距离
        EditText newDistanceInput = new EditText(this);
        newDistanceInput.setHint("Enter distance driven");
        newDistanceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newDistanceInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newVehicleLayout.addView(newDistanceInput);

        // 添加新的条目到当前部分的容器中
        vehicleSection.addView(newVehicleLayout);
    }

    // 添加公共交通条目
    public void onAddPublicClicked(View view) {
        // 创建新的 LinearLayout
        LinearLayout newPublicTransportLayout = new LinearLayout(this);
        newPublicTransportLayout.setOrientation(LinearLayout.HORIZONTAL);
        newPublicTransportLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newPublicTransportLayout.setPadding(0, 8, 0, 8);

        // 创建新的 Spinner 用于公共交通类型选择
        Spinner newTransportTypeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> publicTransportAdapter = ArrayAdapter.createFromResource(this,
                R.array.public_transport_types, android.R.layout.simple_spinner_item);
        publicTransportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newTransportTypeSpinner.setAdapter(publicTransportAdapter);
        newPublicTransportLayout.addView(newTransportTypeSpinner);

        // 创建新的 EditText 用于输入乘坐时间
        EditText newTransportTimeInput = new EditText(this);
        newTransportTimeInput.setHint("Time Spent (in hours)");
        newTransportTimeInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newTransportTimeInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newPublicTransportLayout.addView(newTransportTimeInput);

        // 添加新的条目到当前部分的容器中
        publicTransportSection.addView(newPublicTransportLayout);
    }

    // 添加航班条目
    public void onAddFlightClicked(View view) {
        // 创建新的 LinearLayout
        LinearLayout newFlightLayout = new LinearLayout(this);
        newFlightLayout.setOrientation(LinearLayout.HORIZONTAL);
        newFlightLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newFlightLayout.setPadding(0, 8, 0, 8);

        // 创建新的 Spinner 用于航班类型选择
        Spinner newFlightTypeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> flightAdapter = ArrayAdapter.createFromResource(this,
                R.array.flight_types, android.R.layout.simple_spinner_item);
        flightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newFlightTypeSpinner.setAdapter(flightAdapter);
        newFlightLayout.addView(newFlightTypeSpinner);

        // 创建新的 EditText 用于输入航班数量
        EditText newFlightCountInput = new EditText(this);
        newFlightCountInput.setHint("Enter distance or number of flights");
        newFlightCountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newFlightCountInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newFlightLayout.addView(newFlightCountInput);

        // 添加新的条目到当前部分的容器中
        flightSection.addView(newFlightLayout);

        // 检查是否添加成功
        if (flightSection.getChildCount() > 0) {
            Log.d("FlightSection", "Successfully added a flight entry");
        } else {
            Log.d("FlightSection", "Failed to add a flight entry");
        }
    }

    // 添加骑行/步行条目
    public void onAddCyclingWakingClicked(View view) {
        // 创建新的 LinearLayout
        LinearLayout newCyclingWalkingLayout = new LinearLayout(this);
        newCyclingWalkingLayout.setOrientation(LinearLayout.HORIZONTAL);
        newCyclingWalkingLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newCyclingWalkingLayout.setPadding(0, 8, 0, 8);

        // 创建新的 Spinner 用于选择骑行或步行
        Spinner newCyclingWalkingTypeSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> cyclingWalkingAdapter = ArrayAdapter.createFromResource(this,
                R.array.cycling_walking_types, android.R.layout.simple_spinner_item);
        cyclingWalkingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newCyclingWalkingTypeSpinner.setAdapter(cyclingWalkingAdapter);
        newCyclingWalkingLayout.addView(newCyclingWalkingTypeSpinner);

        // 创建新的 EditText 用于输入距离
        EditText newCyclingWalkingCountInput = new EditText(this);
        newCyclingWalkingCountInput.setHint("Distance cycled or walked");
        newCyclingWalkingCountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newCyclingWalkingCountInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newCyclingWalkingLayout.addView(newCyclingWalkingCountInput);

        // 添加新的条目到当前部分的容器中
        cyclingWalkingSection.addView(newCyclingWalkingLayout);
    }
}
