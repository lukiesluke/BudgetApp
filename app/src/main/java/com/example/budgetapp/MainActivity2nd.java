package com.example.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.budgetapp.data.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class MainActivity2nd extends BaseActivity {
    /* private ImageView todaybudgetimageview, weeklybudgetview, monthlybudgetview, analimageview; //*budgetbtnimageview*/;
    private CardView analyticscardview, todaycardview, homebtn, bdgttv, historycardview, weeklycard, monthlycardview;
    private TextView weektv, budgettv, todaytv, remaintv, monthtv;
    private FirebaseAuth mauth;
    private DatabaseReference expensesref, budgetref, personalref;
    private ImageView budget;
    private String oluserid = "";

    private int totalamonth = 0;
    private int totalbud = 0;
    private int totalbud0 = 0;
    private int totalbud1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2nd);

        TextView appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setText("My Money");
        appBarTitle.setTypeface(tfRegular);

        weektv = findViewById(R.id.weektv);
        bdgttv = findViewById(R.id.buttoncardview);
        budgettv = findViewById(R.id.budtv);
        todaytv = findViewById(R.id.todtv);
        remaintv = findViewById(R.id.savetv);
        monthtv = findViewById(R.id.montv);

        mauth = FirebaseAuth.getInstance();
        oluserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        budgetref = FirebaseDatabase.getInstance().getReference("budget").child(oluserid);
        personalref = FirebaseDatabase.getInstance().getReference("personal").child(oluserid);
        expensesref = FirebaseDatabase.getInstance().getReference("expenses").child(oluserid);

        analyticscardview = findViewById(R.id.analcardview);
        todaycardview = findViewById(R.id.todaybudgetcardview);
        budget = findViewById(R.id.budgetbtnimageview);
        historycardview = findViewById(R.id.historycardview);
        weeklycard = findViewById(R.id.weeklycardview);
        monthlycardview = findViewById(R.id.monthcardview);

        monthlycardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2nd.this, WeekSpendingAct.class);
                intent.putExtra("type", "month");
                startActivity(intent);
            }
        });

        weeklycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2nd.this, WeekSpendingAct.class);
                intent.putExtra("type", "week");
                startActivity(intent);
            }
        });

        historycardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2nd.this, History.class);
                startActivity(intent);
            }
        });

        todaycardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2nd.this, TodaySpendingAct.class);
                startActivity(intent);
            }
        });

        analyticscardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2nd.this, AnalyticActivity.class);
                startActivity(intent);
            }
        });

        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2nd.this, BudgetActivity.class);
                startActivity(intent);
            }
        });

        budgetref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("Amount");
                        int ptotal = Integer.getInteger(String.valueOf(total));
                        totalbud0 += ptotal;
                    }
                    totalbud1 = totalbud0;

                } else {
                    personalref.child("budget").setValue(0);
                    Toast.makeText(MainActivity2nd.this, "Please Set A budget", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getBudgetAmount();
        getTodayspentamount();
        getweekspend();
        getmonthspent();
        getsavings();
    }

    private void getsavings() {
        personalref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int budget;
                    if (snapshot.hasChild("Budget")) {
                        budget = Integer.parseInt(snapshot.child("budget").getValue().toString());
                    } else {
                        budget = 0;
                    }
                    int monthspending;
                    if (snapshot.hasChild("month")) {
                        monthspending = Integer.parseInt(Objects.requireNonNull(snapshot.child("Month").getValue().toString()));
                    } else {
                        monthspending = 0;
                    }
                    int saving = budget - monthspending;
                    remaintv.setText("P " + saving);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity2nd.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getmonthspent() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(oluserid);
        Query query = reference.orderByChild("months").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalamount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("Amount");
                    ;
                    int ptotal = Integer.getInteger(String.valueOf(total));
                    totalamount += ptotal;
                    monthtv.setText("P " + totalamount);
                }
                personalref.child("today").setValue(totalamount);
                totalamonth = totalamount;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getweekspend() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(oluserid);
        Query query = reference.orderByChild("months").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalamount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("Amount");
                    ;
                    int ptotal = Integer.getInteger(String.valueOf(total));
                    totalamount += ptotal;
                    weektv.setText("P " + totalamount);
                }
                personalref.child("week").setValue(totalamount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getTodayspentamount() {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(oluserid);
        Query query = reference.orderByChild("data").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalamount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("Amount");
                    ;
                    int ptotal = Integer.getInteger(String.valueOf(total));
                    totalamount += ptotal;
                    todaytv.setText("P " + totalamount);
                }
                personalref.child("today").setValue(totalamount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity2nd.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getBudgetAmount() {
        budgetref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("Amount");
                        ;
                        int ptotal = Integer.getInteger(String.valueOf(total));
                        totalbud0 += ptotal;
                        budgettv.setText("P " + String.valueOf(totalbud));
                    }

                } else {
                    totalbud1 = totalbud0;
                    budgettv.setText("P " + String.valueOf(0));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.account) {
            Intent intent = new Intent(MainActivity2nd.this, AccountActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}