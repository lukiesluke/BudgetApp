package com.example.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.budgetapp.data.BaseActivity;

public class AnalyticActivity extends BaseActivity {
    private CardView todayCardView, weeklyCardView, monthCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}