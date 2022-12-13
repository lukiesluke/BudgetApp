package com.example.budgetapp.data;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class BaseActivity extends AppCompatActivity {
    protected Typeface tfRegular;
    protected Typeface tfLight;

    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        decimalFormat = new DecimalFormat("#,###.00");
    }

    protected String setAmountFormat(double totalAmount) {
        return decimalFormat.format(totalAmount);
    }
}
