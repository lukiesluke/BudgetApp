
package com.example.budgetapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.adapter.TodayItemsAdapter;
import com.example.budgetapp.data.Data;
import com.example.budgetapp.data.DataBudget;
import com.example.budgetapp.data.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class History extends BaseExpenses implements DatePickerDialog.OnDateSetListener {
    private DataBudget budgetExpense;

    private RecyclerView recyclerView;
    private TodayItemsAdapter todayItemsAdapter;
    private List<Data> mydatalist;
    private Button search, searchAll;
    private TextView historyAmount;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setText("History");
        appBarTitle.setTypeface(tfRegular);

        search = findViewById(R.id.search);
        searchAll = findViewById(R.id.searchAll);
        search.setTypeface(tfRegular);
        searchAll.setTypeface(tfRegular);

        historyAmount = findViewById(R.id.Totalhistoryamountspent);
        historyAmount.setTypeface(tfLight);

        budgetExpense = new DataBudget();

        recyclerView = findViewById(R.id.recycler_view_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        reference = expensesReference;

        mydatalist = new ArrayList<>();
        todayItemsAdapter = new TodayItemsAdapter(History.this, mydatalist, oluserid);
        recyclerView.setAdapter(todayItemsAdapter);

        search.setOnClickListener(v -> showDatePickerDialog());
        searchAll.setOnClickListener(view -> searchAllDate());
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        historyAmount.setVisibility(View.GONE);
        String date = dayOfMonth + "-" + (month + 1) + "-" + year;
        queryDatabase(date);
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

    }

    private void searchAllDate() {
        historyAmount.setVisibility(View.GONE);
        queryDatabase("");
    }

    private void queryDatabase(String date) {
        mydatalist.clear();
        Query query;
        if (!TextUtils.isEmpty(date)) {
            query = reference.orderByChild("date").equalTo(date);
        } else {
            query = reference;
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mydatalist.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Data data = ds.getValue(Data.class);
                        mydatalist.add(data);
                    }
                    recyclerView.setVisibility(View.VISIBLE);

                    budgetExpense = Utils.dataItemSnapshot(snapshot);
                    if (budgetExpense.getTotal() > 0) {
                        historyAmount.setVisibility(View.VISIBLE);
                        historyAmount.setText("This day you spent: " + setAmountFormat(budgetExpense.getTotal()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        todayItemsAdapter.notifyDataSetChanged();
    }
}
