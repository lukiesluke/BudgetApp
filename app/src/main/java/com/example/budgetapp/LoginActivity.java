package com.example.budgetapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginbtn;
    private TextView loginqn;

    private FirebaseAuth mauth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mauth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity2nd.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        loginbtn = findViewById(R.id.loginbtn);
        loginqn = findViewById(R.id.loginqn);


        mauth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        loginqn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterAct.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstr = email.getText().toString();
                String passstr = password.getText().toString();

                if (TextUtils.isEmpty(emailstr)) {
                    email.setError("Email is Required!");
                }
                if (TextUtils.isEmpty(passstr)) {
                    password.setError("Password is Required!");
                } else {
                    progressDialog.setMessage("Log in Progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mauth.signInWithEmailAndPassword(emailstr, passstr).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity2nd.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mauth.removeAuthStateListener(authStateListener);
    }
}