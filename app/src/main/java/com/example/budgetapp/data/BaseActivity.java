package com.example.budgetapp.data;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetapp.R;

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

    public final void displayDialog(Context cxt, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
        builder.setMessage(message);

        builder.setPositiveButton(cxt.getResources().getString(R.string.btn_ok), (dialog, which) -> {
            dialog.dismiss();
        });

        builder.create();
        builder.show();
    }
}
