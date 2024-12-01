package com.example.b07fall2024;

public class EmissionStorage {
    private static EmissionStorage instance;

    private double EnergyUse;
    private double FoodConsumption;
    private double Shopping;
    private double Transportation;

    public static EmissionStorage getInstance() {
        if (instance == null) {
            instance = new EmissionStorage();
        }
        return instance;
    }

    public double getEnergyUse() {
        return EnergyUse;
    }

    public void setEnergyUse(double EnergyUse) {
        this.EnergyUse = EnergyUse;
    }

    public double getFoodConsumption() {
        return FoodConsumption;
    }

    public void setFoodConsumption(double FoodConsumption) {
        this.FoodConsumption = FoodConsumption;
    }

    public double getShopping() {
        return Shopping;
    }

    public void setShopping(double Shopping) {
        this.Shopping = Shopping;
    }

    public double getTransportation() {
        return Transportation;
    }

    public void setTransportation(double Transportation) {
        this.Transportation = Transportation;
    }

}
