package com.example.budgetapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.budgetapp.data.Constant;
import com.example.budgetapp.data.DataBudget;
import com.example.budgetapp.data.Personal;
import com.example.budgetapp.data.Utils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TimerTask;

public class WeeklyAnalytic extends BaseAnalytic {

    private DataBudget budgetExpense = new DataBudget();
    private DataBudget budgetTotal = new DataBudget();
    private ImageView trans, food, house, ent, edu, cha, app, heal, per, oth, monthimage;
    private TextView progtrans, progfood, proghouse, progent, progedu, progcha, progapp, progheal, progper, progoth;
    private RelativeLayout lltrans, llfood, llhouse, llent, lledu, llcha, llapp, llheal, llper, lloth, llanal;
    private TextView analtrans, analfood, analhouse, analent, analedu, analcha, analapp, analheal, analper, analoth, monthspent, totalweekamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_anal);

        TextView appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setText("Weekly Analytics");
        appBarTitle.setTypeface(tfRegular);

        totalweekamount = findViewById(R.id.totalamountspenton);
        monthspent = findViewById(R.id.monthspentamount);
        monthimage = findViewById(R.id.monthratiospending_imageview);
        llanal = findViewById(R.id.linearlayoutanalysis);

        analtrans = findViewById(R.id.analtransportamount);
        analapp = findViewById(R.id.analappamount);
        analfood = findViewById(R.id.analfoodamount);
        analoth = findViewById(R.id.analotheramount);
        analper = findViewById(R.id.analpersonalamount);
        analheal = findViewById(R.id.analhealthamount);
        analhouse = findViewById(R.id.analhomeamount);
        analedu = findViewById(R.id.analeducamount);
        analent = findViewById(R.id.analentamount);
        analcha = findViewById(R.id.analcharityamount);

        progtrans = findViewById(R.id.analtransportamount);
        progfood = findViewById(R.id.analfoodamount);
        proghouse = findViewById(R.id.analhomeamount);
        progent = findViewById(R.id.analentamount);
        progedu = findViewById(R.id.analeducamount);
        progcha = findViewById(R.id.analcharityamount);
        progapp = findViewById(R.id.analappamount);
        progheal = findViewById(R.id.analhealthamount);
        progper = findViewById(R.id.analpersonalamount);
        progoth = findViewById(R.id.analotheramount);

        lltrans = findViewById(R.id.relativelayouttransport);
        llfood = findViewById(R.id.relativefood);
        llhouse = findViewById(R.id.relativehome);
        llent = findViewById(R.id.relativeentertain);
        lledu = findViewById(R.id.relativeeducation);
        llcha = findViewById(R.id.relativecharity);
        llapp = findViewById(R.id.relativeapparel);
        llheal = findViewById(R.id.relativehealth);
        llper = findViewById(R.id.relativepersonal);
        lloth = findViewById(R.id.relativeother);

        trans = findViewById(R.id.transport_status);
        food = findViewById(R.id.food_status);
        house = findViewById(R.id.home_status);
        ent = findViewById(R.id.ent_status);
        edu = findViewById(R.id.educ_status);
        cha = findViewById(R.id.charity_status);
        app = findViewById(R.id.app_status);
        heal = findViewById(R.id.health_status);
        per = findViewById(R.id.personal_status);
        oth = findViewById(R.id.other_status);

        chart = findViewById(R.id.chart1);

        weekExpenses();
        new java.util.Timer().schedule(new TimerTask() {
                                           @Override
                                           public void run() {
                                               setImageAndResources();
                                           }
                                       }, 2000
        );
    }

    private void pieChart(ArrayList<PieEntry> entries) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setCenterTextTypeface(tfLight);
        chart.setCenterText(generateCenterSpannableTextWeek());
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(2400, Easing.EaseInOutQuad);

//        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(((float) budget.getTotalTransport()), "Transport", null));
//        entries.add(new PieEntry(((float) budget.getTotalFood()), "Food", null));
//        entries.add(new PieEntry(((float) budget.getTotalHouse()), "Home", null));
//        entries.add(new PieEntry(((float) budget.getTotalEntertainment()), "Entertainment", null));
//        entries.add(new PieEntry(((float) budget.getTotalEducation()), "Education", null));
//        entries.add(new PieEntry(((float) budget.getTotalCharity()), "Charity", null));
//        entries.add(new PieEntry(((float) budget.getTotalHApparel()), "Apparel", null));
//        entries.add(new PieEntry(((float) budget.getTotalHealth()), "Health", null));
//        entries.add(new PieEntry(((float) budget.getTotalPersonal()), "Personal", null));
//        entries.add(new PieEntry(((float) budget.getTotalOther()), "Others", null));

        PieDataSet dataSet = new PieDataSet(entries, "Weekly Analytic Results");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tfLight);

        chart.setData(data);
        chart.invalidate();
    }

    private void weekExpenses() {
        expensesReference.keepSynced(true);
        Query query = expensesReference.orderByChild("nweek").equalTo(calendarWeek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    budgetExpense = Utils.dataItemSnapshot(snapshot);

                    analfood.setText("Spent " + budgetExpense.getTotalFood());
                    analtrans.setText("Spent " + budgetExpense.getTotalTransport());
                    analhouse.setText("Spent " + budgetExpense.getTotalHouse());
                    analent.setText("Spent " + budgetExpense.getTotalEntertainment());
                    analedu.setText("Spent " + budgetExpense.getTotalEducation());
                    analcha.setText("Spent " + budgetExpense.getTotalCharity());
                    analapp.setText("Spent " + budgetExpense.getTotalHApparel());
                    analheal.setText("Spent " + budgetExpense.getTotalHealth());
                    analoth.setText("Spent " + budgetExpense.getTotalOther());
                    analper.setText("Spent " + budgetExpense.getTotalPersonal());

                    personalTotalExpenses.child("dayfood").setValue(budgetExpense.getTotalFood());
                    personalTotalExpenses.child("daytrans").setValue(budgetExpense.getTotalTransport());
                    personalTotalExpenses.child("dayhome").setValue(budgetExpense.getTotalHouse());
                    personalTotalExpenses.child("dayent").setValue(budgetExpense.getTotalEntertainment());
                    personalTotalExpenses.child("dayedu").setValue(budgetExpense.getTotalEducation());
                    personalTotalExpenses.child("daychar").setValue(budgetExpense.getTotalCharity());
                    personalTotalExpenses.child("dayapparel").setValue(budgetExpense.getTotalHApparel());
                    personalTotalExpenses.child("dayhealth").setValue(budgetExpense.getTotalHealth());
                    personalTotalExpenses.child("dayother").setValue(budgetExpense.getTotalOther());
                    personalTotalExpenses.child(Constant.ITEM_DAY_PERSONAL).setValue(budgetExpense.getTotalPersonal());

                    DecimalFormat df = new DecimalFormat("#,###.00");
                    String strTotal = df.format(budgetExpense.getTotal());

                    totalweekamount.setText("Total Week's Spending: P" + strTotal);
                    monthspent.setText("Total Spent : P " + strTotal);

                    llfood.setVisibility(View.VISIBLE);
                    lltrans.setVisibility(View.VISIBLE);
                    llhouse.setVisibility(View.VISIBLE);
                    llent.setVisibility(View.VISIBLE);
                    lledu.setVisibility(View.VISIBLE);
                    llcha.setVisibility(View.VISIBLE);
                    llapp.setVisibility(View.VISIBLE);
                    llheal.setVisibility(View.VISIBLE);
                    lloth.setVisibility(View.VISIBLE);
                    llper.setVisibility(View.VISIBLE);
                    budgetReference();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void budgetReference() {
        budgetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                budgetTotal = Utils.dataItemSnapshot(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setImageAndResources() {
        personalTotalExpenses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    Personal personal = Utils.personalBudget(snapshot);

                    float transTotal = (float) personal.getDaytrans();
                    float foodTotal = (float) personal.getDayfood();
                    float houseTotal = (float) personal.getDayhome();
                    float entTotal = (float) personal.getDayent();
                    float eduTotal = (float) personal.getDayedu();
                    float charTotal = (float) personal.getDaychar();
                    float appTotal = (float) personal.getDayapparel();
                    float healTotal = (float) personal.getDayhealth();
                    float perTotal = (float) personal.getDaypersonal();
                    float otherTotal = (float) personal.getDayother();

                    float monthSpentTotal;
                    if (snapshot.hasChild("Today")) {
                        monthSpentTotal = Integer.parseInt(snapshot.child("today").getValue().toString());
                    } else {
                        monthSpentTotal = 0;
                    }

                    float transRatio = budgetTotal.getTotalTransport();
                    float foodRatio = budgetTotal.getTotalFood();
                    float homeRatio = budgetTotal.getTotalHouse();
                    float educRatio = budgetTotal.getTotalEducation();
                    float entRatio = budgetTotal.getTotalEntertainment();
                    float appRatio = budgetTotal.getTotalHApparel();
                    float charRatio = budgetTotal.getTotalCharity();
                    float healthRatio = budgetTotal.getTotalHealth();
                    float personRatio = budgetTotal.getTotalPersonal();
                    float otherRatio = budgetTotal.getTotalOther();

                    float monthTotalPercentRatio;
                    if (snapshot.hasChild("daily budget")) {
                        monthTotalPercentRatio = Integer.parseInt(snapshot.child("dailybudget").getValue().toString());
                    } else {
                        monthTotalPercentRatio = 0;
                    }

                    float monthPercent = (monthSpentTotal / monthTotalPercentRatio) * 100;
                    if (monthPercent < 50) {
                        monthspent.setText(monthPercent + " %" + " used of " + monthTotalPercentRatio + ". Status: ");
                        monthimage.setImageResource(R.drawable.green);
                    } else if (monthPercent >= 50 && monthPercent < 100) {
                        monthspent.setText(monthPercent + "  %" + "used of " + monthTotalPercentRatio + ". Status: ");
                        monthimage.setImageResource(R.drawable.brown);
                    } else {
                        monthspent.setText(monthPercent + " %" + " used of " + monthTotalPercentRatio + ". Status: ");
                        monthimage.setImageResource(R.drawable.red);
                    }

                    float transPercent = (transTotal / transRatio) * 100;
                    if (transPercent > 0) {
                        entries.add(new PieEntry(transPercent, "Transport", null));
                    }
                    if (transPercent < 50) {
                        progtrans.setText(transPercent + " %" + " used of " + transRatio + ". Status: ");
                        trans.setImageResource(R.drawable.green);
                    } else if (transPercent >= 50 && transPercent < 100) {
                        progtrans.setText(transPercent + " %" + " used of " + transRatio + ". Status: ");
                        trans.setImageResource(R.drawable.brown);
                    } else {
                        progtrans.setText(transPercent + " %" + "used of " + transRatio + ". Status: ");
                        trans.setImageResource(R.drawable.red);
                    }

                    float foodPercent = (foodTotal / foodRatio) * 100;
                    if (foodPercent > 0) {
                        entries.add(new PieEntry(foodPercent, "Food", null));
                    }
                    if (foodPercent < 50) {
                        progfood.setText(foodPercent + " %" + " used of " + foodRatio + ". Status: ");
                        food.setImageResource(R.drawable.green);
                    } else if (foodPercent >= 50 && foodPercent < 100) {
                        progfood.setText(foodPercent + " %" + " used of " + foodRatio + ". Status: ");
                        food.setImageResource(R.drawable.brown);
                    } else {
                        progfood.setText(foodPercent + " %" + " used of " + foodRatio + ". Status: ");
                        food.setImageResource(R.drawable.red);
                    }

                    float homePercent = (houseTotal / homeRatio) * 100;
                    if (homePercent > 0) {
                        entries.add(new PieEntry(homePercent, "Home", null));
                    }
                    if (homePercent < 50) {
                        proghouse.setText(homePercent + " %" + " used of " + homeRatio + ". Status: ");
                        house.setImageResource(R.drawable.green);
                    } else if (homePercent >= 50 && homePercent < 100) {
                        proghouse.setText(homePercent + " %" + " used of " + homeRatio + ". Status: ");
                        house.setImageResource(R.drawable.brown);
                    } else {
                        proghouse.setText(homePercent + " %" + "used of " + homeRatio + ". Status: ");
                        house.setImageResource(R.drawable.red);
                    }

                    float educPercent = (eduTotal / educRatio) * 100;
                    if (educPercent > 0) {
                        entries.add(new PieEntry(educPercent, "Education", null));
                    }
                    if (educPercent < 50) {
                        progedu.setText(educPercent + " %" + " used of " + educRatio + ". Status: ");
                        edu.setImageResource(R.drawable.green);
                    } else if (educPercent >= 50 && educPercent < 100) {
                        progedu.setText(educPercent + " %" + " used of " + educRatio + ". Status: ");
                        edu.setImageResource(R.drawable.brown);
                    } else {
                        progedu.setText(educPercent + " %" + " used of " + educRatio + ". Status: ");
                        edu.setImageResource(R.drawable.red);
                    }

                    float entPercent = (entTotal / entRatio) * 100;
                    if (entPercent > 0) {
                        entries.add(new PieEntry(entPercent, "Entertainment", null));
                    }
                    if (entPercent < 50) {
                        progent.setText(entPercent + " %" + " used of " + entRatio + ". Status: ");
                        ent.setImageResource(R.drawable.green);
                    } else if (entPercent >= 50 && entPercent < 100) {
                        progent.setText(entPercent + " %" + " used of " + entRatio + ". Status: ");
                        ent.setImageResource(R.drawable.brown);
                    } else {
                        progent.setText(entPercent + " %" + " used of " + entRatio + ". Status: ");
                        ent.setImageResource(R.drawable.red);
                    }

                    float charityPercent = (charTotal / charRatio) * 100;
                    if (charityPercent > 0) {
                        entries.add(new PieEntry(charityPercent, "Charity", null));
                    }
                    if (charityPercent < 50) {
                        progcha.setText(charityPercent + " %" + " used of " + charRatio + ". Status: ");
                        cha.setImageResource(R.drawable.green);
                    } else if (charityPercent >= 50 && charityPercent < 100) {
                        progcha.setText(charityPercent + " %" + " used of " + charRatio + ". Status: ");
                        cha.setImageResource(R.drawable.brown);
                    } else {
                        progcha.setText(charityPercent + " %" + " used of " + charRatio + ". Status: ");
                        cha.setImageResource(R.drawable.red);
                    }

                    float appPercent = (appTotal / appRatio) * 100;
                    if (appPercent > 0) {
                        entries.add(new PieEntry(appPercent, "Apparel", null));
                    }
                    if (appPercent < 50) {
                        progapp.setText(appPercent + " %" + " used of " + appRatio + ". Status: ");
                        app.setImageResource(R.drawable.green);
                    } else if (appPercent >= 50 && appPercent < 100) {
                        progapp.setText(appPercent + " %" + " used of " + appRatio + ". Status: ");
                        app.setImageResource(R.drawable.brown);
                    } else {
                        progapp.setText(appPercent + " %" + " used of " + appRatio + ". Status: ");
                        app.setImageResource(R.drawable.red);
                    }

                    float healthPercent = (healTotal / healthRatio) * 100;
                    if (healthPercent > 0) {
                        entries.add(new PieEntry((healthPercent), "Health", null));
                    }
                    if (healthPercent < 50) {
                        progheal.setText(healthPercent + " %" + " used of " + healthRatio + ". Status: ");
                        heal.setImageResource(R.drawable.green);
                    } else if (healthPercent >= 50 && healthPercent < 100) {
                        progheal.setText(healthPercent + " %" + " used of " + healthRatio + ". Status: ");
                        heal.setImageResource(R.drawable.brown);
                    } else {
                        progheal.setText(healthPercent + " %" + " used of " + healthRatio + ". Status: ");
                        heal.setImageResource(R.drawable.red);
                    }

                    float personPercent = (perTotal / personRatio) * 100;
                    if (personPercent > 0) {
                        entries.add(new PieEntry((personPercent), "Personal", null));
                    }
                    if (personPercent < 50) {
                        progper.setText(personPercent + " %" + " used of " + personRatio + ". Status: ");
                        per.setImageResource(R.drawable.green);
                    } else if (personPercent >= 50 && personPercent < 100) {
                        progper.setText(personPercent + " %" + " used of " + personRatio + ". Status: ");
                        per.setImageResource(R.drawable.brown);
                    } else {
                        progper.setText(personPercent + " %" + " used of " + personRatio + ". Status: ");
                        per.setImageResource(R.drawable.red);
                    }

                    float otherPercent = (otherTotal / otherRatio) * 100;
                    if (otherPercent > 0) {
                        entries.add(new PieEntry(otherPercent, "Others", null));
                    }
                    if (transPercent < 50) {
                        progoth.setText(otherPercent + " %" + " used of " + otherRatio + ". Status: ");
                        oth.setImageResource(R.drawable.green);
                    } else if (otherPercent >= 50 && otherPercent < 100) {
                        progoth.setText(otherPercent + " %" + " used of " + otherRatio + ". Status: ");
                        oth.setImageResource(R.drawable.brown);
                    } else {
                        progoth.setText(otherPercent + " %" + " used of " + otherRatio + ". Status: ");
                        oth.setImageResource(R.drawable.red);
                    }
                    pieChart(entries);
                } else {
                    Toast.makeText(WeeklyAnalytic.this, "SetStatusAndImageResource Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
