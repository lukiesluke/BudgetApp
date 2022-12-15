package com.example.budgetapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.adapter.WeekSpendingAdapter;
import com.example.budgetapp.data.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WeekSpendingAct extends BaseExpenses {
    private TextView totalWeekAmount;
    private ProgressBar progressBar;
    private WeekSpendingAdapter weekSpendingAdapter;
    private List<Data> myDataList;
    private ProgressDialog loader;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_spending);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setTypeface(tfRegular);

        totalWeekAmount = findViewById(R.id.ttlweekam);
        progressBar = findViewById(R.id.progressBar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        loader = new ProgressDialog(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        myDataList = new ArrayList<>();
        weekSpendingAdapter = new WeekSpendingAdapter(WeekSpendingAct.this, myDataList, oluserid);
        recyclerView.setAdapter(weekSpendingAdapter);

        String actionBarTitle = "";
        if (getIntent().getExtras() != null) {
            type = getIntent().getStringExtra("type");
            if (type.equals("week")) {
                actionBarTitle = "Weekly Spending";
                readWeekSpendItems();
            } else if (type.equals("month")) {
                actionBarTitle = "Monthly Spending";
                readMonthSpendingItems();
            }
        }
        appBarTitle.setText(actionBarTitle);
    }

    private void readMonthSpendingItems() {
        expensesReference.keepSynced(true);
        Log.d("lwg", "calendarMonth: " + Integer.parseInt(calendarMonth));
        Query query = expensesReference.orderByChild("month").equalTo(Integer.parseInt(calendarMonth));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (myDataList != null) {
                    myDataList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    myDataList.add(dataSnapshot.getValue(Data.class));
                }

                weekSpendingAdapter.setData(myDataList);
                progressBar.setVisibility(View.GONE);

                double totalAmount = 0;
                for (Data dateValue : myDataList) {
                    totalAmount += dateValue.getAmount();
                }

                totalWeekAmount.setText("Total Month Spending: " + setAmountFormat(totalAmount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readWeekSpendItems() {
        expensesReference.keepSynced(true);
        Query query = expensesReference.orderByChild("nweek").equalTo(calendarWeek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (myDataList != null) {
                    myDataList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    myDataList.add(dataSnapshot.getValue(Data.class));
                }

                weekSpendingAdapter.setData(myDataList);
                progressBar.setVisibility(View.GONE);

                double totalAmount = 0;
                for (Data dateValue : myDataList) {
                    totalAmount += dateValue.getAmount();
                }

                totalWeekAmount.setText("Total Week Spending: " + setAmountFormat(totalAmount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
