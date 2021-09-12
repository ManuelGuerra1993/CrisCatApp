package com.manu.criscatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class EmpezarActivity extends AppCompatActivity {

    private Button btnEmpezar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empezar);

        btnEmpezar = findViewById(R.id.btnEmpezar);

        mAuth = FirebaseAuth.getInstance();

        btnEmpezar.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(this, ListaMascotaActivity.class));
        });
    }



}