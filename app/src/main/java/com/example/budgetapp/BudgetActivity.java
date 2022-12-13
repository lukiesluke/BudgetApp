package com.example.budgetapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.data.BaseActivity;
import com.example.budgetapp.data.Data;
import com.example.budgetapp.data.DataBudget;
import com.example.budgetapp.data.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class BudgetActivity extends BaseActivity {
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference budgetReference, personalReference;

    private FirebaseAuth mauth;
    private String oluserid;

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

        mauth = FirebaseAuth.getInstance();
        oluserid = mauth.getCurrentUser().getUid();

        budgetReference = firebaseDatabase.getReference().child("Budget").child(oluserid);
        personalReference = firebaseDatabase.getReference("Personal").child(oluserid);

        loader = new ProgressDialog(this);
        totalBudgetAmount = findViewById(R.id.totalbudgettv);
        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        budgetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

                    DataBudget budget = Utils.dataItemSnapshot(snapshot);
                    totalBudgetAmount.setText("My Budget: ₱" + setAmountFormat(budget.getTotal()));

                    int weeklybudget = budget.getTotal() / 4;
                    int daily = budget.getTotal() / 30;
                    personalReference.child("budget").setValue(budget.getTotal());
                    personalReference.child("weekly").setValue(weeklybudget);
                    personalReference.child("daily").setValue(daily);
                } else {
                    personalReference.child("budget").setValue(0);
                    personalReference.child("weekly").setValue(0);
                    personalReference.child("daily").setValue(0);
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
                additem();
            }
        });

        getmonthtransratio();
        getmonthfoodratio();
        getmonthhouseratio();
        getmonthentratio();
        getmontheduratio();
        getmonthcharratio();
        getmonthappratio();
        getmonthhealratio();
        getmonthperratio();
        getmonthotherratio();
    }

    private void additem() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myview = inflater.inflate(R.layout.input_layout, null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);

        final Spinner itemspinner = myview.findViewById(R.id.itemspinner);
        final EditText amount = myview.findViewById(R.id.amount);
        final Button cancel = myview.findViewById(R.id.cancel);
        final Button save = myview.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budam = amount.getText().toString();
                String budit = itemspinner.getSelectedItem().toString();
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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(budgetReference, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {
                holder.setItemAmount("Allocated amount: ₱" + model.getAmount());

                holder.setDate("On: " + model.getDate());
                holder.setItemName("Budget Item: " + model.getItem());
                holder.notes.setVisibility(View.GONE);
                switch (model.getItem()) {
                    case "Transport":
                        holder.imageView.setImageResource(R.drawable.ic_transport);
                        break;
                    case "Food":
                        holder.imageView.setImageResource(R.drawable.ic_food);
                        break;
                    case "House":
                        holder.imageView.setImageResource(R.drawable.ic_house);
                        break;
                    case "Entertainment":
                        holder.imageView.setImageResource(R.drawable.ic_entertainment);
                        break;
                    case "Education":
                        holder.imageView.setImageResource(R.drawable.ic_education);
                        break;
                    case "Charity":
                        holder.imageView.setImageResource(R.drawable.ic_consultancy);
                        break;
                    case "Apparel":
                        holder.imageView.setImageResource(R.drawable.ic_shirt);
                        break;
                    case "Health":
                        holder.imageView.setImageResource(R.drawable.ic_health);
                        break;
                    case "Personal":
                        holder.imageView.setImageResource(R.drawable.ic_personalcare);
                        break;
                    case "Other":
                        holder.imageView.setImageResource(R.drawable.ic_other);
                        break;
                }
                holder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key = getRef(position).getKey();
                        item = model.getItem();
                        amount = model.getAmount();
                        updateData();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final View myView;
        public ImageView imageView;
        public TextView notes, dates;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
            imageView = itemView.findViewById(R.id.imageview);
            notes = itemView.findViewById(R.id.note);
            dates = itemView.findViewById(R.id.date);
        }

        public void setItemName(String itemName) {
            TextView item = myView.findViewById(R.id.item);
            item.setText(itemName);
        }

        public void setItemAmount(String itemAmount) {
            TextView amount = myView.findViewById(R.id.amount);
            amount.setText(itemAmount);
        }

        public void setDate(String itemDate) {
            TextView date = myView.findViewById(R.id.date);
            date.setText(itemDate);
        }
    }

    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();
        final TextView mitem = myView.findViewById(R.id.itemname);
        final EditText mamount = myView.findViewById(R.id.amount);
        final EditText mnotes = myView.findViewById(R.id.note);

        mnotes.setVisibility(View.GONE);
        mitem.setText(item);
        mamount.setText(String.valueOf(amount));
        mamount.setSelection(String.valueOf(amount).length());

        Button deleteButton = myView.findViewById(R.id.btndelete);
        Button updateButton = myView.findViewById(R.id.btnupdate);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(mamount.getText().toString());
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Weeks week = Weeks.weeksBetween(epoch, now);
                Months months = Months.monthsBetween(epoch, now);
                String nday = item + date;
                String nweek = item + week.getWeeks();
                String nmonth = item + months.getMonths();

                Data data = new Data(item, date, post_key, nday, nweek, nmonth, amount, months.getMonths(), week.getWeeks(), null);
                budgetReference.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(BudgetActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budgetReference.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(BudgetActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getmonthotherratio() {
        Query query = budgetReference.orderByChild("item").equalTo("Other");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int dayothratio = ptotal / 30;
                    int weekothratio = ptotal / 4;
                    int montothratio = ptotal;

                    personalReference.child("dayothratio").setValue(dayothratio);
                    personalReference.child("weekothratio").setValue(weekothratio);
                    personalReference.child("montothratio").setValue(montothratio);
                } else {
                    personalReference.child("dayothratio").setValue(0);
                    personalReference.child("weekothratio").setValue(0);
                    personalReference.child("montothratio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmonthperratio() {
        Query query = budgetReference.orderByChild("item").equalTo("Personal");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int dayeperratio = ptotal / 30;
                    int weekperratio = ptotal / 4;
                    int monthperratio = ptotal;

                    personalReference.child("dayeperratio").setValue(dayeperratio);
                    personalReference.child("weekperratio").setValue(weekperratio);
                    personalReference.child("monthperratio").setValue(monthperratio);
                } else {
                    personalReference.child("dayeperratio").setValue(0);
                    personalReference.child("weekperratio").setValue(0);
                    personalReference.child("monthperratio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmonthhealratio() {
        Query query = budgetReference.orderByChild("item").equalTo("Health");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int dayhealratio = ptotal / 30;
                    int weekhealratio = ptotal / 4;
                    int monthealratio = ptotal;

                    personalReference.child("dayhealratio").setValue(dayhealratio);
                    personalReference.child("weekhealratio").setValue(weekhealratio);
                    personalReference.child("monthealratio").setValue(monthealratio);
                } else {
                    personalReference.child("dayhealratio").setValue(0);
                    personalReference.child("weekhealratio").setValue(0);
                    personalReference.child("monthealratio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmonthappratio() {
        Query query = budgetReference.orderByChild("item").equalTo("Apparel");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int dayappratio = ptotal / 30;
                    int weekappratio = ptotal / 4;
                    int monthappatio = ptotal;

                    personalReference.child("dayappratio").setValue(dayappratio);
                    personalReference.child("weekappratio").setValue(weekappratio);
                    personalReference.child("monthappatio").setValue(monthappatio);
                } else {
                    personalReference.child("dayappratio").setValue(0);
                    personalReference.child("weekappratio").setValue(0);
                    personalReference.child("monthappatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmonthcharratio() {
        Query query = budgetReference.orderByChild("item").equalTo("Charity");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int daycharratio = ptotal / 30;
                    int weekcharratio = ptotal / 4;
                    int monthcharratio = ptotal;

                    personalReference.child("daycharratio").setValue(daycharratio);
                    personalReference.child("weekcharratio").setValue(weekcharratio);
                    personalReference.child("monthcharratio").setValue(monthcharratio);
                } else {
                    personalReference.child("daycharratio").setValue(0);
                    personalReference.child("weekcharratio").setValue(0);
                    personalReference.child("monthcharratio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmontheduratio() {
        Query query = budgetReference.orderByChild("item").equalTo("Education");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int dayeducratio = ptotal / 30;
                    int weekeducratio = ptotal / 4;
                    int monthneducatio = ptotal;

                    personalReference.child("dayeducratio").setValue(dayeducratio);
                    personalReference.child("weekeducratio").setValue(weekeducratio);
                    personalReference.child("monthneducatio").setValue(monthneducatio);
                } else {
                    personalReference.child("dayeducratio").setValue(0);
                    personalReference.child("weekeducratio").setValue(0);
                    personalReference.child("monthneducatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmonthentratio() {
        Query query = budgetReference.orderByChild("item").equalTo("Entertainment");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int dayenttratio = ptotal / 30;
                    int weekentratio = ptotal / 4;
                    int monthentatio = ptotal;

                    personalReference.child("dayenttratio").setValue(dayenttratio);
                    personalReference.child("weekentratio").setValue(weekentratio);
                    personalReference.child("monthentatio").setValue(monthentatio);

                } else {
                    personalReference.child("dayenttratio").setValue(0);
                    personalReference.child("weekentratio").setValue(0);
                    personalReference.child("monthentatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmonthhouseratio() {
        Query query = budgetReference.orderByChild("item").equalTo("House");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int dayhousetratio = ptotal / 30;
                    int weekhouseratio = ptotal / 4;
                    int monthhouseratio = ptotal;

                    personalReference.child("dayhousetratio").setValue(dayhousetratio);
                    personalReference.child("weekhouseratio").setValue(weekhouseratio);
                    personalReference.child("monthhouseratio").setValue(monthhouseratio);
                } else {
                    personalReference.child("dayhousetratio").setValue(0);
                    personalReference.child("weekhouseratio").setValue(0);
                    personalReference.child("monthhouseratio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmonthfoodratio() {
        Query query = budgetReference.orderByChild("item").equalTo("Food");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int dayfoodtratio = ptotal / 30;
                    int weekfoodratio = ptotal / 4;
                    int monthfoodratio = ptotal;

                    personalReference.child("dayfoodtratio").setValue(dayfoodtratio);
                    personalReference.child("weekfoodratio").setValue(weekfoodratio);
                    personalReference.child("monthfoodratio").setValue(monthfoodratio);
                } else {
                    personalReference.child("dayfoodtratio").setValue(0);
                    personalReference.child("weekfoodratio").setValue(0);
                    personalReference.child("monthfoodratio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmonthtransratio() {

        Query query = budgetReference.orderByChild("item").equalTo("Transport");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int ptotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        ptotal = Integer.parseInt(String.valueOf(total));
                    }
                    int daytranstratio = ptotal / 30;
                    int weektransratio = ptotal / 4;
                    int monthtransratio = ptotal;

                    personalReference.child("daytransratio").setValue(daytranstratio);
                    personalReference.child("weektransratio").setValue(weektransratio);
                    personalReference.child("monthtransratio").setValue(monthtransratio);
                } else {
                    personalReference.child("daytransratio").setValue(0);
                    personalReference.child("weektransratio").setValue(0);
                    personalReference.child("monthtransratio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
