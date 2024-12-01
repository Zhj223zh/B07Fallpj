package com.habit;

public class Habit {
    private final String name;
    private final String category;
    private final String description;
    private final int impact;
    private int progress;
    private boolean adopted;

    public Habit(String name, String category, String description, int impact) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.impact = impact;
        this.progress = 0;
    }

    public int getProgress() {
        return progress;
    }

    public void increaseProgress(int value) {
        progress += value;
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

    public int getImpact() {
        return impact;
    }

    public boolean isAdopted() {
        return adopted;
    }

    public void setAdopted(boolean adopted) {
        this.adopted = adopted;
    }
}
