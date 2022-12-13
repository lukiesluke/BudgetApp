package com.example.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.budgetapp.data.BaseActivity;

public class AnalyticActivity extends BaseActivity {
    private CardView todayCardView, weeklyCardView, monthCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analact);

        TextView appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setText("Analytics");
        appBarTitle.setTypeface(tfRegular);

        todayCardView = findViewById(R.id.todaycardview);
        monthCardView = findViewById(R.id.monthcardview);
        weeklyCardView = findViewById(R.id.weeklcardview);

        todayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnalyticActivity.this, DailyAnalActivity.class);
                startActivity(intent);
            }
        });
        monthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnalyticActivity.this, MonthlyAnalytic.class);
                startActivity(intent);
            }
        });

        weeklyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnalyticActivity.this, WeeklyAnalytic.class);
                startActivity(intent);
            }
        });
    }
}