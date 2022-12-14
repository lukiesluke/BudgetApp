package com.example.budgetapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import androidx.annotation.Nullable;

import com.example.budgetapp.data.BaseActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BaseAnalytic extends BaseActivity {
    private final static String DATE_FORMAT = "dd-MM-yyyy";
    private final static String DATE_FORMAT_MONTH_NAME = "MMM";

    protected SimpleDateFormat dateFormat;
    protected SimpleDateFormat dateFormatMonth;

    protected Calendar calendar;
    protected String calendarDate;
    protected String calendarWeek;
    protected String calendarMonth;
    protected String calendarDayMonth;
    protected String calendarNameMonth;

    protected final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    protected DatabaseReference expensesReference, personalTotalExpenses, budgetReference;
    protected FirebaseAuth mauth;
    protected String oluserid = "";
    protected PieChart chart;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormatMonth = new SimpleDateFormat(DATE_FORMAT_MONTH_NAME);

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

    public SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("Today\nAnalytics");
        s.setSpan(new RelativeSizeSpan(2.17f), 0, 5, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 5, s.length() - 9, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 5, s.length() - 9, 0);
        s.setSpan(new RelativeSizeSpan(1.7f), 5, s.length() - 9, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 5, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 9, s.length(), 0);
        return s;
    }

    public SpannableString generateCenterSpannableTextWeek() {
        SpannableString s = new SpannableString("Weekly\nAnalytics");
        s.setSpan(new RelativeSizeSpan(2.17f), 0, 6, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 6, s.length() - 9, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 6, s.length() - 9, 0);
        s.setSpan(new RelativeSizeSpan(1.7f), 6, s.length() - 9, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 6, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 9, s.length(), 0);
        return s;
    }

    public SpannableString generateCenterSpannableTextMonthly() {
        SpannableString s = new SpannableString("Monthly\nAnalytics");
        s.setSpan(new RelativeSizeSpan(2.17f), 0, 7, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 7, s.length() - 9, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 7, s.length() - 9, 0);
        s.setSpan(new RelativeSizeSpan(1.7f), 7, s.length() - 9, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 7, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 9, s.length(), 0);
        return s;
    }
}
