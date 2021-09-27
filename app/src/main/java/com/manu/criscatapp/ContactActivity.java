package com.manu.criscatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manu.criscatapp.modelo.Cita;
import com.manu.criscatapp.modelo.Contacto;

import java.util.UUID;

public class ContactActivity extends AppCompatActivity {

    EditText txtTitulo, txtDetalle;
    Button btnGuardar;

    FirebaseDatabase fbDataBase;
    DatabaseReference dbReference;

    String titulo, detalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        inicializarFirebase();
        asignarReferencias();


    }

    private void asignarReferencias(){
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDetalle = findViewById(R.id.txtDetalle);
        btnGuardar = findViewById(R.id.btnSendContact);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registro();
            }
        });

    }

    private void Registro(){
        titulo = txtTitulo.getText().toString();
        detalle = txtDetalle.getText().toString();

        if (validarCampos()==true){
            Contacto c = new Contacto();
            c.setId(UUID.randomUUID().toString());
            c.setTitulo(titulo);
            c.setDetalle(detalle);
            dbReference.child("Contacto").child(c.getId()).setValue(c);
            Toast.makeText(this, "Registro Completado", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ContactActivity.this, Home.class);
            startActivity(intent);
        } else {}

    }

    private boolean validarCampos(){
        boolean retorno = true;
        if (titulo.isEmpty()){
            txtTitulo.setError("El título es obligatorio");
            retorno = false;
        }
        if (detalle.isEmpty()){
            txtDetalle.setError("El Detalle es obligatorio");
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