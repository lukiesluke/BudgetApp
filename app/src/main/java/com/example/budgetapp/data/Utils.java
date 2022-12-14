package com.example.budgetapp.data;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

public class Utils {

    public static Personal personalBudget(DataSnapshot snapshot) {
        float transTotal = 0;
        float foodTotal = 0;
        float houseTotal = 0;
        float entTotal = 0;
        float eduTotal = 0;
        float charTotal = 0;
        float appTotal = 0;
        float healTotal = 0;
        float perTotal = 0;
        float otherTotal = 0;

        if (snapshot.hasChild(Personal.str_daytrans)) {
            transTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_daytrans).getValue()).toString());
        }

        if (snapshot.hasChild(Personal.str_dayfood)) {
            foodTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_dayfood).getValue()).toString());
        }

        if (snapshot.hasChild(Personal.str_dayhome)) {
            houseTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_dayhome).getValue()).toString());
        }

        if (snapshot.hasChild(Personal.str_dayent)) {
            entTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_dayent).getValue()).toString());
        }

        if (snapshot.hasChild(Personal.str_dayedu)) {
            eduTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_dayedu).getValue()).toString());
        }

        if (snapshot.hasChild(Personal.str_daychar)) {
            charTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_daychar).getValue()).toString());
        }

        if (snapshot.hasChild(Personal.str_dayapparel)) {
            appTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_dayapparel).getValue()).toString());
        }

        if (snapshot.hasChild(Personal.str_dayhealth)) {
            healTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_dayhealth).getValue()).toString());
        }
        if (snapshot.hasChild(Personal.str_daypersonal)) {
            perTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_daypersonal).getValue()).toString());
        }
        if (snapshot.hasChild(Personal.str_dayother)) {
            otherTotal = Float.parseFloat(Objects.requireNonNull(snapshot.child(Personal.str_dayother).getValue()).toString());
        }

        Log.d("lwg", "daytrans:" + transTotal);
        Log.d("lwg", "foodTotal:" + foodTotal);
        Log.d("lwg", "houseTotal:" + houseTotal);
        Log.d("lwg", "entTotal:" + entTotal);
        Log.d("lwg", "eduTotal:" + eduTotal);
        Log.d("lwg", "charTotal:" + charTotal);
        Log.d("lwg", "appTotal:" + appTotal);
        Log.d("lwg", "healTotal:" + healTotal);
        Log.d("lwg", "perTotal:" + perTotal);
        Log.d("lwg", "otherTotal:" + otherTotal);

        Personal personal = new Personal();
        personal.setDaytrans(transTotal);
        personal.setDayfood(foodTotal);
        personal.setDayhome(houseTotal);
        personal.setDayent(entTotal);
        personal.setDayedu(eduTotal);
        personal.setDaychar(charTotal);
        personal.setDayapparel(appTotal);
        personal.setDayhealth(healTotal);
        personal.setDaypersonal(perTotal);
        personal.setDayother(otherTotal);

        return personal;
    }

    public static DataBudget dataItemSnapshot(DataSnapshot snapshot) {
        int total = 0;
        int totalTransport = 0;
        int totalFood = 0;
        int totalHouse = 0;
        int totalEntertainment = 0;
        int totalEducation = 0;
        int totalCharity = 0;
        int totalHApparel = 0;
        int totalHealth = 0;
        int totalPersonal = 0;
        int totalOther = 0;

        DataBudget budget = new DataBudget();

        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
            for (DataSnapshot snap : snapshot.getChildren()) {
                Data data = snap.getValue(Data.class);
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_TRANSPORT)) {
                        totalTransport += data.getAmount();
                    }
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_FOOD)) {
                        totalFood += data.getAmount();
                    }
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_HOUSE)) {
                        totalHouse += data.getAmount();
                    }
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_ENTERTAINMENT)) {
                        totalEntertainment += data.getAmount();
                    }
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_EDUCATION)) {
                        totalEducation += data.getAmount();
                    }
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_CHARITY)) {
                        totalCharity += data.getAmount();
                    }
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_APPAREL)) {
                        totalHApparel += data.getAmount();
                    }
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_HEALTH)) {
                        totalHealth += data.getAmount();
                    }
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_PERSONAL)) {
                        totalPersonal += data.getAmount();
                    }
                    if (!TextUtils.isEmpty(data.getItem()) && data.getItem().equalsIgnoreCase(Constant.ITEM_OTHER)) {
                        totalOther += data.getAmount();
                    }
                    total += data.getAmount();
                }
            }
        }

        budget.setTotalTransport(totalTransport);
        budget.setTotalFood(totalFood);
        budget.setTotalHouse(totalHouse);
        budget.setTotalEntertainment(totalEntertainment);
        budget.setTotalEducation(totalEducation);
        budget.setTotalCharity(totalCharity);
        budget.setTotalHApparel(totalHApparel);
        budget.setTotalHealth(totalHealth);
        budget.setTotalPersonal(totalPersonal);
        budget.setTotalOther(totalOther);
        budget.setTotal(total);
        return budget;
    }
}
