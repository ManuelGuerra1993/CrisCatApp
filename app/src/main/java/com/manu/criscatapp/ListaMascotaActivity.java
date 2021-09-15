package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manu.criscatapp.modelo.Mascota;

import java.util.ArrayList;
import java.util.List;

public class ListaMascotaActivity extends AppCompatActivity {

    FloatingActionButton btnNuevo;
    RecyclerView recyclerMascota;

    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;

    List<Mascota> listaMascota = new ArrayList<>();
    //ArrayAdapter<Mascota> adaptador;
    AdaptadorPersonalizado adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mascota);
        inicializarFirebase();
        agregarMascota();
        listarDatos();
    }

    private void listarDatos(){
        dbReference.child("Mascota").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaMascota.clear();
                for (DataSnapshot item:snapshot.getChildren()){
                    Mascota m = item.getValue(Mascota.class);
                    listaMascota.add(m);
                }
                //adaptador = new ArrayAdapter<Mascota>(ListaMascotaActivity.this, android.R.layout.simple_list_item_1,listaMascota);
                //lstMascota.setAdapter(adaptador);
                adaptador = new AdaptadorPersonalizado(ListaMascotaActivity.this,listaMascota);
                recyclerMascota.setAdapter(adaptador);
                recyclerMascota.setLayoutManager(new LinearLayoutManager(ListaMascotaActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        fbDatabase = FirebaseDatabase.getInstance();
        dbReference = fbDatabase.getReference();
    }

    public void agregarMascota(){
        recyclerMascota = findViewById(R.id.recyclerMascotas);
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