package com.example.budgetapp.data;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class DataBudget {
    private String item;
    private int amount;
    private int total;
    private int totalTransport = 0;
    private int totalFood = 0;
    private int totalHouse = 0;
    private int totalEntertainment = 0;
    private int totalEducation = 0;
    private int totalCharity = 0;
    private int totalHApparel = 0;
    private int totalHealth = 0;
    private int totalPersonal = 0;
    private int totalOther = 0;

    public DataBudget() {
    }

    public DataBudget(String item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public String getItem() {
        return item;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("item", item);
        result.put("amount", amount);
        return result;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalTransport() {
        return totalTransport;
    }

    public void setTotalTransport(int totalTransport) {
        this.totalTransport = totalTransport;
    }

    public int getTotalFood() {
        return totalFood;
    }

    public void setTotalFood(int totalFood) {
        this.totalFood = totalFood;
    }

    public int getTotalHouse() {
        return totalHouse;
    }

    public void setTotalHouse(int totalHouse) {
        this.totalHouse = totalHouse;
    }

    public int getTotalEntertainment() {
        return totalEntertainment;
    }

    public void setTotalEntertainment(int totalEntertainment) {
        this.totalEntertainment = totalEntertainment;
    }

    public int getTotalEducation() {
        return totalEducation;
    }

    public void setTotalEducation(int totalEducation) {
        this.totalEducation = totalEducation;
    }

    public int getTotalCharity() {
        return totalCharity;
    }

    public void setTotalCharity(int totalCharity) {
        this.totalCharity = totalCharity;
    }

    public int getTotalHApparel() {
        return totalHApparel;
    }

    public void setTotalHApparel(int totalHApparel) {
        this.totalHApparel = totalHApparel;
    }

    public int getTotalHealth() {
        return totalHealth;
    }

    public void setTotalHealth(int totalHealth) {
        this.totalHealth = totalHealth;
    }

    public int getTotalPersonal() {
        return totalPersonal;
    }

    public void setTotalPersonal(int totalPersonal) {
        this.totalPersonal = totalPersonal;
    }

    public int getTotalOther() {
        return totalOther;
    }

    public void setTotalOther(int totalOther) {
        this.totalOther = totalOther;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
