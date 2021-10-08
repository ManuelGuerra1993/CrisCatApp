package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manu.criscatapp.modelo.Mascota;

import java.util.HashMap;
import java.util.UUID;

public class MantenedorMascotaActivity extends AppCompatActivity {

    FirebaseDatabase fbDataBase;
    DatabaseReference dbReference;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    EditText txtNombre, txtRaza, txtanioNacimiento, txtPropietario, txtEstado;
    RadioButton rbCanino, rbFelino, rbMacho, rbHembra;
    Button btnGuardar;
    TextView lblTitulo;

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
            String e1 = getIntent().getStringExtra("especie");
            if (e1.equals("1")){
                rbCanino.setChecked(true);
            } else {rbFelino.setChecked(true);}
            txtRaza.setText(getIntent().getStringExtra("raza"));
            String s1 = getIntent().getStringExtra("sexo");
            if (s1.equals("1")) {
                rbMacho.setChecked(true);
            } else {
                rbHembra.setChecked(true);
            }
            txtanioNacimiento.setText(getIntent().getStringExtra("anioNacimiento"));
            txtPropietario.setText(getIntent().getStringExtra("propietario"));
            txtEstado.setText(getIntent().getStringExtra("estado"));
            lblTitulo = findViewById(R.id.lblTitulo);
            lblTitulo.setText("ACTUALIZA LA INFORMACIÓN DE TU MASCOTA");
            btnGuardar.setText("ACTUALIZAR");
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
        if (txtanioNacimiento.getText().toString().equals("")){
            anionacimiento = 0;
        } else {
            anionacimiento = Integer.parseInt(txtanioNacimiento.getText().toString());
        }
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

        if (registrar == true) {
            if (validarCampos()==true) {
                Mascota mascota = new Mascota();
                mascota.setId(UUID.randomUUID().toString());
                mascota.setNombre(nombres);
                mascota.setRaza(raza);
                mascota.setPropietario(userID);
                mascota.setEstado(estado);
                mascota.setAnioNacimiento(anionacimiento);
                mascota.setEspecie(especie);
                mascota.setSexo(sexo);
                dbReference.child("Mascota").child(mascota.getId()).setValue(mascota);
                Toast.makeText(this, "Mascota registrada", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MantenedorMascotaActivity.this, ListaMascotaActivity.class);
                startActivity(intent);
            } else{}
        } else {
            if (validarCampos()==true) {
                HashMap map = new HashMap();
                map.put("nombre", nombres);
                map.put("especie", especie);
                map.put("raza", raza);
                map.put("sexo", sexo);
                map.put("anioNacimiento", anionacimiento);
                map.put("propietario", propietario);
                map.put("estado", estado);
                dbReference.child("Mascota").child(id).updateChildren(map);
                Toast.makeText(MantenedorMascotaActivity.this, "Mascota actualizada", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MantenedorMascotaActivity.this, ListaMascotaActivity.class);
                startActivity(intent);
            } else {}
        }
    }

    private boolean validarCampos(){
        boolean retorno = true;
        if (nombres.isEmpty()){
            txtNombre.setError("El nombre de la mascota es obligatorio");
            retorno=false;
        }
        if (raza.isEmpty()){
            txtRaza.setError("Es importante saber la raza");
            retorno=false;
        }
        if (anionacimiento == 0){
            txtanioNacimiento.setError("Es importante saber el Año de nacimiento");
            retorno = false;
        }
        /*if (propietario.isEmpty()){
            txtPropietario.setError("Es importante saber quien es el propietario");
            retorno = false;
        }*/
        return retorno;
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        fbDataBase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        dbReference = fbDataBase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d("TAG","ID del usuario: "+userID);
                }
            }
        };

        dbReference.child("Mascota").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //String ido = snapshot.child("raza").getValue().toString();
                    //txtPropietario.setText(userID);
                    //Log.d("TAG","uid" +ido);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showData(DataSnapshot dataSnapshot){
        for (DataSnapshot ds : dataSnapshot.getChildren()){

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}