package com.example.data;

import java.util.HashMap;
import java.util.Map;

public class EmissionsData {
    public Map<String, EmissionCategory> emissionsByDate;
    public double totalEmissions;
    public EmissionCategory categoryTotals;

    public EmissionsData() {
        this.emissionsByDate = new HashMap<>();
        this.totalEmissions = 0;
        this.categoryTotals = new EmissionCategory(0, 0, 0, 0);
    }
}