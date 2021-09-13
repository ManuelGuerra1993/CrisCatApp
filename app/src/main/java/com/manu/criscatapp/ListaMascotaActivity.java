package com.manu.criscatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListaMascotaActivity extends AppCompatActivity {

    FloatingActionButton btnNuevo;
    RecyclerView recyclerMascotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mascota);
        agregarMascota();
    }

    public void agregarMascota(){
        recyclerMascotas = findViewById(R.id.RecyclerMascotas);
        btnNuevo = findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaMascotaActivity.this,MantenedorMascotaActivity.class);
                startActivity(intent);
            }
        });
    }

}