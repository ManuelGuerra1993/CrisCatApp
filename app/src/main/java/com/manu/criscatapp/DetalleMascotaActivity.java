package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manu.criscatapp.modelo.Mascota;
import com.manu.criscatapp.modelo.Usuario;

public class DetalleMascotaActivity extends AppCompatActivity {
    TextView txtdetalleNombre;

    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mascota);
        inicializarFirebase();
        asignarReferencias();
        obtenerDatosMascota();
    }

    private void asignarReferencias(){
        txtdetalleNombre = findViewById(R.id.txtDetalleNombre);
    }

    private void obtenerDatosMascota(){
        if (getIntent().hasExtra("id")){
            txtdetalleNombre.setText(getIntent().getStringExtra("nombre"));
        }
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        fbDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        dbReference = fbDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d("TAG","ID del usuario: "+userID);
                }
            }
        };
    }
}