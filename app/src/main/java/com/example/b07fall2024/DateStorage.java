package com.example.b07fall2024;

public class DateStorage {
    private static DateStorage instance;

    private int year;
    private int month;
    private int day;
    private int week;

    public static DateStorage getInstance() {
        if (instance == null) {
            instance = new DateStorage();
        }
        return instance;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month + 1;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
