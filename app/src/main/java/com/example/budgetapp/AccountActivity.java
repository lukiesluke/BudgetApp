package com.example.budgetapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.budgetapp.data.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends BaseActivity {
    private TextView usermail;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setText("My Account");
        appBarTitle.setTypeface(tfRegular);

        logout = findViewById(R.id.logoutbtn);
        usermail = findViewById(R.id.useremail);
        usermail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("My Money")
                        .setMessage("Are you sure you want to log out?")
                        .setCancelable(false)
                        .setPositiveButton("yes", (dialog, id) -> {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }).setNegativeButton("No", null).show();
            }
        });
    }
}
