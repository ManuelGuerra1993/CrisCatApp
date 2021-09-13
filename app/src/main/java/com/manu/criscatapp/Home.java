package com.manu.criscatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button btn_pet, btn_dates, btn_fares, btn_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        asignReferences();
    }

    private void asignReferences() {

        btn_pet = findViewById(R.id.btn_pet);
        btn_dates = findViewById(R.id.btn_dates);
        btn_fares = findViewById(R.id.btn_fares);
        btn_contact = findViewById(R.id.btn_contact);

        btn_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(Home.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btn_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(Home.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btn_fares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(Home.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(Home.this,MainActivity.class);
                startActivity(intent);
            }
        });



    }


}