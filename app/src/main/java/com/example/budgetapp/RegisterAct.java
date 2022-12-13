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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterAct extends AppCompatActivity {
    private EditText email, password;
    private Button registerbtn;
    private TextView registerqn;
    private FirebaseAuth mauth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        registerbtn = findViewById(R.id.registerbtn);
        registerqn = findViewById(R.id.registerqn);
        mauth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        registerqn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterAct.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
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
                    progressDialog.setMessage("Register in Progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mauth.createUserWithEmailAndPassword(emailstr, passstr).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(RegisterAct.this, MainActivity2nd.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(RegisterAct.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}








