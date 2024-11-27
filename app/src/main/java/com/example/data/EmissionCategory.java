package com.example.data;

public class EmissionCategory {
    public double energyUse;
    public double foodConsumption;
    public double shopping;
    public double transportation;

    public EmissionCategory() {
    }

    public EmissionCategory(double energyUse, double foodConsumption, double shopping, double transportation) {
        this.energyUse = energyUse;
        this.foodConsumption = foodConsumption;
        this.shopping = shopping;
        this.transportation = transportation;
    }

    public double getEnergyUse() {
        return energyUse;
    }

    public void setEnergyUse(double energyUse) {
        this.energyUse = energyUse;
    }

    public double getFoodConsumption() {
        return foodConsumption;
    }

    public void setFoodConsumption(double foodConsumption) {
        this.foodConsumption = foodConsumption;
    }

    public double getShopping() {
        return shopping;
    }

    public void setShopping(double shopping) {
        this.shopping = shopping;
    }

    public double getTransportation() {
        return transportation;
    }

    public void setTransportation(double transportation) {
        this.transportation = transportation;
    }

    public double getTotalEmissions()
    {
        return transportation+shopping+foodConsumption+energyUse;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || getClass() != obj.getClass()) return false;
        EmissionCategory item = (EmissionCategory) obj;
        return this.transportation == item.transportation && this.shopping == item.shopping && this.foodConsumption == item.foodConsumption && this.energyUse == item.energyUse;
    }

    @Override
    public  int hashCode()
    {
        return (int) (transportation+shopping+foodConsumption+energyUse);
    }
}