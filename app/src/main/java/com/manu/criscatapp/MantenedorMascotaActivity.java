package com.manu.criscatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manu.criscatapp.modelo.Mascota;

import java.util.HashMap;
import java.util.UUID;

public class MantenedorMascotaActivity extends AppCompatActivity {

    FirebaseDatabase fbDataBase;
    DatabaseReference dbReference;

    EditText txtNombre, txtRaza, txtanioNacimiento, txtPropietario, txtEstado;
    RadioButton rbCanino, rbFelino, rbMacho, rbHembra;
    Button btnGuardar;

    String nombres, raza, propietario, estado, id;
    int anionacimiento, especie, sexo;

    Boolean registrar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenedor_mascota);
        inicializarFirebase();
        asignarReferencias();
        verificarRegistrar();
    }

    private void verificarRegistrar(){
        if (getIntent().hasExtra("id")){
            registrar = false;
            id = getIntent().getStringExtra("id");
            txtNombre.setText(getIntent().getStringExtra("nombre"));
            //rbCanino.setText(getIntent().getStringExtra("especie"));
            //rbFelino.setText(getIntent().getStringExtra("especie"));
            String e1 = getIntent().getStringExtra("especie");
            if (e1.equals("1")){
                rbCanino.setSelected(true);
            } else {rbFelino.setSelected(true);}

            txtRaza.setText(getIntent().getStringExtra("raza"));
            rbMacho.setText(getIntent().getStringExtra("sexo"));
            rbHembra.setText(getIntent().getStringExtra("sexo"));
            txtanioNacimiento.setText(getIntent().getStringExtra("anioNacimiento"));
            txtPropietario.setText(getIntent().getStringExtra("propietario"));
            txtEstado.setText(getIntent().getStringExtra("estado"));
        }
    }

    private void asignarReferencias(){
        txtNombre = findViewById(R.id.txtNombreMascota);
        txtRaza = findViewById(R.id.txtRaza);
        txtanioNacimiento = findViewById(R.id.txtanionacimiento);
        txtPropietario = findViewById(R.id.txtPropietario);
        txtEstado = findViewById(R.id.txtEstado);
        rbCanino = findViewById(R.id.rbCanino);
        rbFelino = findViewById(R.id.rbFelino);
        rbMacho = findViewById(R.id.rbMacho);
        rbHembra = findViewById(R.id.rbHembra);
        btnGuardar = findViewById(R.id.btnRegistrar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });
    }

    private void registrar(){
        nombres = txtNombre.getText().toString();
        raza = txtRaza.getText().toString();
        propietario = txtPropietario.getText().toString();
        estado = txtEstado.getText().toString();
        anionacimiento = Integer.parseInt(txtanioNacimiento.getText().toString());
        if (rbFelino.isChecked()){
            especie = 2;
        } if (rbCanino.isChecked()){
            especie = 1;
        }
        if (rbMacho.isChecked()){
            sexo = 1;
        } if (rbHembra.isChecked()){
            sexo = 2;
        }

        /*felino = Integer.parseInt(rbFelino.getText().toString());
        canino = Integer.parseInt(rbCanino.getText().toString());
        macho = Integer.parseInt(rbMacho.getText().toString());
        hembra = Integer.parseInt(rbHembra.getText().toString());*/

        if (registrar == true) {
            Mascota mascota = new Mascota();
            mascota.setId(UUID.randomUUID().toString());
            mascota.setNombre(nombres);
            mascota.setRaza(raza);
            mascota.setPropietario(propietario);
            mascota.setEstado(estado);
            mascota.setAnioNacimiento(anionacimiento);
            mascota.setEspecie(especie);
            mascota.setSexo(sexo);
            dbReference.child("Mascota").child(mascota.getId()).setValue(mascota);
            Toast.makeText(this, "Mascota registrada", Toast.LENGTH_SHORT).show();
        } else {
            HashMap map = new HashMap();
            map.put("nombre",nombres);
            map.put("especie",especie);
            map.put("raza",raza);
            map.put("sexo",sexo);
            map.put("anioNacimiento", anionacimiento);
            map.put("propietario",propietario);
            map.put("estado",estado);
            dbReference.child("Mascota").child(id).updateChildren(map);
            Toast.makeText(MantenedorMascotaActivity.this, "Mascota actualizada", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MantenedorMascotaActivity.this, ListaMascotaActivity.class);
            startActivity(intent);
        }
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        fbDataBase = FirebaseDatabase.getInstance();
        dbReference = fbDataBase.getReference();
    }
}