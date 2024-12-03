package com.example.expensemanagerapp;

public class Category {
    private String monthName;

    public Category(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    @Override
    public String toString() {
        return monthName;  // This will ensure that the Spinner displays the month name
    }
}
