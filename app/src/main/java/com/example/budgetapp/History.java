
package com.example.budgetapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.adapter.TodayItemsAdapter;
import com.example.budgetapp.data.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class History extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private RecyclerView recyclerView;
    private TodayItemsAdapter todayItemsAdapter;
    private List<Data> mydatalist;
    private FirebaseAuth mauth;
    private String oluseid = "";
    private DatabaseReference expensesref, budgetref;
    private Toolbar settingsbar;
    private Button search;
    private TextView historyamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        settingsbar = findViewById(R.id.toolbar);
        setSupportActionBar(settingsbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History");

        search = findViewById(R.id.search);
        historyamount = findViewById(R.id.Totalhistoryamountspent);

        mauth = FirebaseAuth.getInstance();
        oluseid = mauth.getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recycler_view_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        mydatalist = new ArrayList<>();
        todayItemsAdapter = new TodayItemsAdapter(History.this, mydatalist, oluseid);
        recyclerView.setAdapter(todayItemsAdapter);
        search.setOnClickListener(v -> showdatepickerdialog());
    }

    private void showdatepickerdialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int months = month + 1;
        String padded = String.format("%02d", dayOfMonth);
        String date = dayOfMonth + " - " + "  " + month + " - " + year;
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(oluseid);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mydatalist.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Data data = snapshot.getValue(Data.class);
                        mydatalist.add(data);
                    }
                    todayItemsAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);

                    int totalamount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("Amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalamount += ptotal;
                        if (totalamount > 0) {
                            historyamount.setVisibility(View.VISIBLE);
                            historyamount.setText("This day you spent: " + totalamount);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
