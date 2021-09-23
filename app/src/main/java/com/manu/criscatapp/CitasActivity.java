package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manu.criscatapp.modelo.Cita;
import com.manu.criscatapp.modelo.Doctor;
import com.manu.criscatapp.modelo.Horario;
import com.manu.criscatapp.modelo.Mascota;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CitasActivity extends AppCompatActivity {

    Spinner cboHorario;
    Spinner cboDoctor;
    EditText Fecha, Notas;
    Button btnGuardar;

    FirebaseDatabase fbDataBase;
    DatabaseReference dbReference;

    String fecha, horario, doctor, notas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);
        inicializarFirebase();

        cboHorario = findViewById(R.id.cboHorario);
        cboDoctor = findViewById(R.id.cboDoctor);
        Fecha = findViewById(R.id.txtFecha);
        Notas = findViewById(R.id.txtNotas);
        btnGuardar = findViewById(R.id.btnRegistrarCita);

        //mDatabase = FirebaseDatabase.getInstance().getReference();

        loadDoctor();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registro();
            }
        });


    }

    public void loadDoctor(){
        List<Doctor> doctor = new ArrayList<>();
        dbReference.child("Doctores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        String id = ds.getKey();
                        String nombre = ds.child("Nombre").getValue().toString();
                        doctor.add(new Doctor(id,nombre));
                    }

                    ArrayAdapter<Doctor> ArrayAdapter = new ArrayAdapter<>(CitasActivity.this,android.R.layout.simple_dropdown_item_1line,doctor);
                    cboDoctor.setAdapter(ArrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void registro(){
        fecha = Fecha.getText().toString();
        horario = cboHorario.getSelectedItem().toString();
        doctor = cboDoctor.getSelectedItem().toString();
        notas = Notas.getText().toString();

        if (validarCampos()==true) {

            Cita cita = new Cita();
            cita.setId(UUID.randomUUID().toString());
            cita.setFecha(fecha);
            cita.setHorario(horario);
            cita.setDoctor(doctor);
            cita.setNota(notas);
            dbReference.child("Cita").child(cita.getId()).setValue(cita);
            Toast.makeText(this, "Cita registrada", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(CitasActivity.this, Home.class);
            startActivity(intent);
        } else {}

    }

    private boolean validarCampos(){
        boolean retorno = true;
        if (fecha.isEmpty()){
            Fecha.setError("La fecha es obligatoria");
            retorno = false;
        }
        if (notas.isEmpty()){
            Notas.setError("Ingrese las notas");
            retorno = false;
        }
        return retorno;
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        fbDataBase = FirebaseDatabase.getInstance();
        dbReference = fbDataBase.getReference();
    }
}