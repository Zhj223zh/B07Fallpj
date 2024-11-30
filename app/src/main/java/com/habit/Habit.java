package com.habit;

public class Habit {
    private String name;
    private String category;
    private String description;
    private int impactLevel;

    public Habit(String name, String category, String description, int impactLevel) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.impactLevel = impactLevel;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getImpactLevel() {
        return impactLevel;
    }
}
