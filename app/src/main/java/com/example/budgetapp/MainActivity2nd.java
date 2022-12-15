package com.example.budgetapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.budgetapp.data.Data;
import com.example.budgetapp.data.DataBudget;
import com.example.budgetapp.data.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2nd extends BaseExpenses {
    /* private ImageView todaybudgetimageview, weeklybudgetview, monthlybudgetview, analimageview; //*budgetbtnimageview*/;
    private CardView analyticsCardView, todayCardView, homebtn, bdgttv, historycardview, weeklycard, monthlycardview;
    private TextView weektv, budgettv, todaytv, remaintv, monthtv, savetv;
    private DatabaseReference budgetReference;
    private ImageView budget;

    private DatabaseReference referenceToday, referenceWeek, referenceMonth;
    private DataBudget dataBudgetMonthly;

    private int totalamonth = 0;
    private int totalbud = 0;
    private int totalbud0 = 0;
    private int totalbud1 = 0;
    private int monthlyBudget = 0;
    private int monthExpenses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2nd);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView appBarTitle = findViewById(R.id.appBarTitle);

        appBarTitle.setText("My Money");
        appBarTitle.setTypeface(tfRegular);

        bdgttv = findViewById(R.id.buttoncardview);
        budgettv = findViewById(R.id.budtv);
        todaytv = findViewById(R.id.todtv);
        weektv = findViewById(R.id.weektv);
        monthtv = findViewById(R.id.montv);
        savetv = findViewById(R.id.savetv);

        budgettv.setTypeface(tfRegular);
        todaytv.setTypeface(tfRegular);
        weektv.setTypeface(tfRegular);
        monthtv.setTypeface(tfRegular);
        savetv.setTypeface(tfRegular);

        budgetReference = FirebaseDatabase.getInstance().getReference("Budget").child(oluserid);

        referenceToday = FirebaseDatabase.getInstance().getReference("expenses").child(oluserid);
        referenceWeek = FirebaseDatabase.getInstance().getReference("expenses").child(oluserid);
        referenceMonth = FirebaseDatabase.getInstance().getReference("expenses").child(oluserid);

        analyticsCardView = findViewById(R.id.analcardview);
        todayCardView = findViewById(R.id.todaybudgetcardview);
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

        todayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2nd.this, TodaySpendingAct.class);
                startActivity(intent);
            }
        });

        analyticsCardView.setOnClickListener(new View.OnClickListener() {
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

        getBudget();
        getTodaySpentAmount();
        getWeekSpend();
        getMonthSpent();
    }

    private void getBudget() {
        budgetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataBudgetMonthly = Utils.dataItemSnapshot(snapshot);
                monthlyBudget = dataBudgetMonthly.getTotal();

                int saving = monthlyBudget - monthExpenses;
                savetv.setText("P" + formatNumber(saving));
                budgettv.setText("P" + formatNumber(monthlyBudget));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity2nd.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMonthSpent() {
        referenceMonth.keepSynced(true);
        Log.d("lwg", "calendarMonth: " + Integer.parseInt(calendarMonth));
        Query query = referenceMonth.orderByChild("month").equalTo(Integer.parseInt(calendarMonth));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Data> myDataList = new ArrayList<>();
                DataBudget dataBudgetExpenseMonthly;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    myDataList.add(dataSnapshot.getValue(Data.class));
                }

                dataBudgetExpenseMonthly = Utils.dataItemSnapshot(snapshot);
                monthExpenses = dataBudgetExpenseMonthly.getTotal();
                monthtv.setText("P" + formatNumber(monthExpenses));

                int saving = monthlyBudget - monthExpenses;
                savetv.setText("P" + formatNumber(saving));
                budgettv.setText("P" + formatNumber(monthlyBudget));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getWeekSpend() {
        referenceWeek.keepSynced(true);
        Query query = referenceWeek.orderByChild("nweek").equalTo(calendarWeek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Data> myDataList = new ArrayList<>();
                DataBudget dataBudgetExpenseWeekly;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    myDataList.add(dataSnapshot.getValue(Data.class));
                }

                dataBudgetExpenseWeekly = Utils.dataItemSnapshot(snapshot);
                weektv.setText("P" + formatNumber(dataBudgetExpenseWeekly.getTotal()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getTodaySpentAmount() {
        referenceToday.keepSynced(true);
        Query query = referenceToday.orderByChild("date").equalTo(calendarDate);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Data> myDataList = new ArrayList<>();
                DataBudget dataBudgetExpenseToday;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    myDataList.add(dataSnapshot.getValue(Data.class));
                }

                dataBudgetExpenseToday = Utils.dataItemSnapshot(snapshot);
                monitorDailyExpenses(dataBudgetExpenseToday, dataBudgetMonthly);

                todaytv.setText("P" + formatNumber(dataBudgetExpenseToday.getTotal()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void monitorDailyExpenses(DataBudget dataBudgetExpenseToday, DataBudget dataBudgetMonthly) {
        StringBuilder strAlert = new StringBuilder();
        int lineNUmber = 0;
        if ((dataBudgetMonthly.getTotalTransport() - dataBudgetExpenseToday.getTotalTransport()) < 0) {
            strAlert.append("* Transport\n");
            lineNUmber++;
        }
        if ((dataBudgetMonthly.getTotalFood() - dataBudgetExpenseToday.getTotalFood()) < 0) {
            strAlert.append("* Food\n");
            lineNUmber++;
        }
        if ((dataBudgetMonthly.getTotalHouse() - dataBudgetExpenseToday.getTotalHouse()) < 0) {
            strAlert.append("* House\n");
            lineNUmber++;
        }
        if ((dataBudgetMonthly.getTotalEntertainment() - dataBudgetExpenseToday.getTotalEntertainment()) < 0) {
            strAlert.append("* Entertainment\n");
            lineNUmber++;
        }
        if ((dataBudgetMonthly.getTotalEducation() - dataBudgetExpenseToday.getTotalEducation()) < 0) {
            strAlert.append("* Education\n");
            lineNUmber++;
        }
        if ((dataBudgetMonthly.getTotalCharity() - dataBudgetExpenseToday.getTotalCharity()) < 0) {
            strAlert.append("* Charity\n");
            lineNUmber++;
        }
        if ((dataBudgetMonthly.getTotalHApparel() - dataBudgetExpenseToday.getTotalHApparel()) < 0) {
            strAlert.append("* Apparel\n");
            lineNUmber++;
        }
        if ((dataBudgetMonthly.getTotalHealth() - dataBudgetExpenseToday.getTotalHealth()) < 0) {
            strAlert.append("* Health\n");
            lineNUmber++;
        }
        if ((dataBudgetMonthly.getTotalPersonal() - dataBudgetExpenseToday.getTotalPersonal()) < 0) {
            strAlert.append("* Personal\n");
            lineNUmber++;
        }
        if ((dataBudgetMonthly.getTotalOther() - dataBudgetExpenseToday.getTotalOther()) < 0) {
            strAlert.append("* Other\n");
            lineNUmber++;
        }

        if (strAlert.toString().length() > 3) {
            customDialogBox(this, "Overspending Items:", strAlert.toString(), lineNUmber);
        }
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

    public static String formatNumber(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    private void customDialogBox(Context context, String title, String message, int lineNUmber) {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(R.layout.custom_dialog_layout, null);
        myDialog.setView(mView);

        AlertDialog dialog = myDialog.create();
        TextView mTitle = mView.findViewById(R.id.title);
        TextView mMessage = mView.findViewById(R.id.message);
        Button mOkay = mView.findViewById(R.id.btnOkay);

        mTitle.setText(title);
        mMessage.setLines(lineNUmber);
        mMessage.setText(message);

        mOkay.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
}