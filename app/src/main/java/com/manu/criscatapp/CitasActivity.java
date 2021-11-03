package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private int dia, mes, anio, hora, minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);
        inicializarFirebase();
        cboHorario = findViewById(R.id.cboHorario);
        cboDoctor = findViewById(R.id.cboDoctor);
        Fecha = findViewById(R.id.txtFecha);
        Fecha.setFocusable(true);
        Fecha.setFocusableInTouchMode(true);
        Fecha.setInputType(InputType.TYPE_NULL);
        Notas = findViewById(R.id.txtNotas);
        btnGuardar = findViewById(R.id.btnRegistrarCita);

        //mDatabase = FirebaseDatabase.getInstance().getReference();

        /*Fecha.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CitasActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Fecha.setText(day+"/"+(month+1)+"/"+year);
                    }
                }
                ,dia,mes,anio);
                datePickerDialog.show();
            }
        });*/


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
        if (validarFecha()==false){
            Fecha.setError("Formato incorrecto");
            retorno = false;
        }

        if (notas.isEmpty()){
            Notas.setError("Ingrese las notas");
            retorno = false;
        }
        return retorno;
    }

    public boolean validarFecha() {
        boolean correcto = false;

        try {
            //Formato de fecha (día/mes/año)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.setLenient(false);
            //Comprobación de la fecha
            formatoFecha.parse(this.dia + "/" + this.mes + "/" + this.anio);
            correcto = true;
        } catch (ParseException e) {
            //Si la fecha no es correcta, pasará por aquí
            correcto = false;
        }

        return correcto;
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        fbDataBase = FirebaseDatabase.getInstance();
        dbReference = fbDataBase.getReference();
    }

    public void abrirCalendario(View view) {
        Calendar cal = Calendar.getInstance();
        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH);
        anio = cal.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(CitasActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String fecha = day+"/"+(month+1)+"/"+year;
                Fecha.setText(fecha);
            }
        },dia,mes,anio);
        dialog.show();
    }
}