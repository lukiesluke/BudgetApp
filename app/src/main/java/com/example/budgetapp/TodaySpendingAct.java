package com.example.budgetapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.adapter.TodayItemsAdapter;
import com.example.budgetapp.data.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TodaySpendingAct extends BaseExpenses {

    private TextView totalAmountSpentOn;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TodayItemsAdapter todayItemsAdapter;
    private List<Data> myDataList = new ArrayList<>();
    private String post_key = "";
    private String item = "";
    private int amount = 0;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_spending);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setText("Today Spending");
        appBarTitle.setTypeface(tfRegular);

        totalAmountSpentOn = findViewById(R.id.totalamountspenton);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        loader = new ProgressDialog(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        myDataList = new ArrayList<>();
        todayItemsAdapter = new TodayItemsAdapter(TodaySpendingAct.this, myDataList, oluserid);
        recyclerView.setAdapter(todayItemsAdapter);

        readItems();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemSpentOn();
            }
        });
    }

    private void readItems() {
        String date = dateFormat.format(calendar.getTime());
        expensesReference.keepSynced(true);
        Query query = expensesReference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (myDataList != null) {
                    myDataList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    myDataList.add(dataSnapshot.getValue(Data.class));
                }

                todayItemsAdapter.setData(myDataList);

                double totalAmount = 0;
                for (Data ds : myDataList) {
                    totalAmount += ds.getAmount();
                }

                totalAmountSpentOn.setText("Total Day's Spending: " + setAmountFormat(totalAmount));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addItemSpentOn() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = myView.findViewById(R.id.itemspinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final EditText note = myView.findViewById(R.id.note);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);

        note.setVisibility(View.VISIBLE);

        save.setOnClickListener(v -> {

            String Amount = amount.getText().toString();
            String item = itemSpinner.getSelectedItem().toString();
            String notes = note.getText().toString();

            if (TextUtils.isEmpty(Amount)) {
                amount.setError("Amount is Required");
                return;
            }
            if (item.equals("Select Item")) {
                Toast.makeText(TodaySpendingAct.this, "Select a valid item", Toast.LENGTH_SHORT).show();
            }

            if (TextUtils.isEmpty(notes)) {
                note.setError("Note is Required");
                return;
            } else {
                loader.setMessage("Adding a Budget item");
                loader.setCanceledOnTouchOutside(false);
                loader.show();
                String id = expensesReference.push().getKey();

                Log.d("lwg", "date: " + calendarDate);
                Log.d("lwg", "nWeek: " + calendarWeek);
                Log.d("lwg", "nMonth: " + calendarMonth);
                Log.d("lwg", "nDay: " + calendarDayMonth);
                Log.d("lwg", "calendarNameMonth: " + calendarNameMonth);

                Data data = new Data(item, calendarDate, id, calendarDayMonth, calendarWeek, calendarNameMonth,
                        Integer.parseInt(Amount), Integer.parseInt(calendarMonth),
                        Integer.parseInt(calendarWeek), notes);
                expensesReference.child(Objects.requireNonNull(id)).setValue(data).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(TodaySpendingAct.this,
                                "Budget item added Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TodaySpendingAct.this,
                                task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                    loader.dismiss();
                });
            }
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
