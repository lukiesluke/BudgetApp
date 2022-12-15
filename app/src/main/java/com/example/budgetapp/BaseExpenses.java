package com.example.budgetapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.example.budgetapp.data.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BaseExpenses extends BaseActivity {
    private final static String DATE_FORMAT = "dd-MM-yyyy";
    private final static String DATE_FORMAT_MONTH_NAME = "MMMM";

    protected SimpleDateFormat dateFormat;
    protected SimpleDateFormat dateFormatMonth;
    private DecimalFormat decimalFormat;

    protected Calendar calendar;
    protected String calendarDate;
    protected String calendarWeek;
    protected String calendarMonth;
    protected String calendarDayMonth;
    protected String calendarNameMonth;

    protected final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    protected DatabaseReference expensesReference, budgetReference, personalTotalExpenses;
    protected FirebaseAuth mauth;
    protected String oluserid;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormatMonth = new SimpleDateFormat(DATE_FORMAT_MONTH_NAME);
        decimalFormat = new DecimalFormat("#,##0.00");

        calendar = Calendar.getInstance();
        calendarDate = dateFormat.format(calendar.getTime());
        calendarWeek = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
        calendarMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        calendarDayMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        calendarNameMonth = dateFormatMonth.format(calendar.getTime());

        mauth = FirebaseAuth.getInstance();
        oluserid = mauth.getCurrentUser().getUid();

        expensesReference = firebaseDatabase.getReference("expenses").child(oluserid);
        personalTotalExpenses = firebaseDatabase.getReference("personal").child(oluserid);
        budgetReference = firebaseDatabase.getReference("Budget").child(oluserid);
    }

    protected String setAmountFormat(int totalAmount) {
        return decimalFormat.format(totalAmount);
    }

    protected String setAmountFormat(double totalAmount) {
        return decimalFormat.format(totalAmount);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
