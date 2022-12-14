package com.example.budgetapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.adapter.BudgetItemsAdapter;
import com.example.budgetapp.data.Data;
import com.example.budgetapp.data.DataBudget;
import com.example.budgetapp.data.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BudgetActivity extends BaseExpenses {
    private BudgetItemsAdapter budgetItemsAdapter;
    private List<Data> myDataList = new ArrayList<>();

    private TextView totalBudgetAmount;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProgressDialog loader;
    private String post_key = "";
    private String item = "";
    private int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        TextView appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setText("My Budget");
        appBarTitle.setTypeface(tfRegular);

        loader = new ProgressDialog(this);
        totalBudgetAmount = findViewById(R.id.totalbudgettv);
        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        myDataList = new ArrayList<>();
        budgetItemsAdapter = new BudgetItemsAdapter(BudgetActivity.this, myDataList, oluserid);
        recyclerView.setAdapter(budgetItemsAdapter);

        budgetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (myDataList != null) {
                        myDataList.clear();
                    }

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        myDataList.add(dataSnapshot.getValue(Data.class));
                    }

                    budgetItemsAdapter.setData(myDataList);

                    DataBudget budget = Utils.dataItemSnapshot(snapshot);
                    totalBudgetAmount.setText("My Budget: ₱" + setAmountFormat(budget.getTotal()));

                    int weeklyBudget = budget.getTotal() / 4;
                    int daily = budget.getTotal() / 30;
                    personalTotalExpenses.child("budget").setValue(budget.getTotal());
                    personalTotalExpenses.child("weekly").setValue(weeklyBudget);
                    personalTotalExpenses.child("daily").setValue(daily);
                } else {
                    personalTotalExpenses.child("budget").setValue(0);
                    personalTotalExpenses.child("weekly").setValue(0);
                    personalTotalExpenses.child("daily").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    private void addItem() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = myView.findViewById(R.id.itemspinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budam = amount.getText().toString();
                String budit = itemSpinner.getSelectedItem().toString();
                if (TextUtils.isEmpty(budam)) {
                    amount.setError("Amount is Required");
                    return;
                }
                if (budit.equals("Select Item")) {
                    Toast.makeText(BudgetActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();

                } else {
                    loader.setMessage("Adding a Budget item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    String id = budgetReference.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Weeks week = Weeks.weeksBetween(epoch, now);
                    Months months = Months.monthsBetween(epoch, now);
                    String nday = budit + date;
                    String nweek = budit + week.getWeeks();
                    String nmonth = budit + months.getMonths();

                    Data data = new Data(budit, date, id, nday, nweek, nmonth, Integer.parseInt(budam), months.getMonths(), week.getWeeks(), null);
                    budgetReference.keepSynced(true);
                    budgetReference.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(BudgetActivity.this, "Budget item added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });

                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
