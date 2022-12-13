package com.example.budgetapp.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Data {
    private String item;
    private String date;
    private String id;
    private String nday;
    private String nweek;
    private String nmonth;
    private int amount;
    private int month;
    private int week;
    private String notes;

    public Data() {
    }

    public Data(String item, String date, String id, String nday, String nweek, String nmonth, int amount, int month, int week, String notes) {
        this.item = item;
        this.date = date;
        this.id = id;
        this.nday = nday;
        this.nweek = nweek;
        this.nmonth = nmonth;
        this.amount = amount;
        this.month = month;
        this.week = week;
        this.notes = notes;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("amount", amount);
        result.put("notes", notes);
        return result;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNday() {
        return nday;
    }

    public void setNday(String nday) {
        this.nday = nday;
    }

    public String getNweek() {
        return nweek;
    }

    public void setNweek(String nweek) {
        this.nweek = nweek;
    }

    public String getNmonth() {
        return nmonth;
    }

    public void setNmonth(String nmonth) {
        this.nmonth = nmonth;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
