package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetalleMascotaActivity extends AppCompatActivity {
    TextView txtdetalleNombre, txtdetalleEspecie, txtDetalleSexo, txtDetalleraza, txtDetalleAnioNacimiento, txtDetalleEstado;
    ImageView ivmascota;

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
        ivmascota = findViewById(R.id.ivMascota);
        txtdetalleNombre = findViewById(R.id.txtDetalleNombre);
        txtdetalleEspecie = findViewById(R.id.txtEspecie);
        txtDetalleSexo = findViewById(R.id.txtSexo);
        txtDetalleraza = findViewById(R.id.txtRaza);
        txtDetalleAnioNacimiento = findViewById(R.id.txtanioNacimiento);
        txtDetalleEstado = findViewById(R.id.txtEstado);
    }

    private void obtenerDatosMascota(){
        if (getIntent().hasExtra("id")){
            String img = getIntent().getStringExtra("imageURL");
            //imgFoto.setImageURI(Uri.parse(img));
            Glide.with(this).load(img).into(ivmascota);
            txtdetalleNombre.setText(getIntent().getStringExtra("nombre"));
            String e1 = getIntent().getStringExtra("especie");
            if (e1.equals("1")){
                txtdetalleEspecie.setText("Canino");
            } else {txtdetalleEspecie.setText("Felino");}
            txtDetalleraza.setText(getIntent().getStringExtra("raza"));
            String s1 = getIntent().getStringExtra("sexo");
            if (s1.equals("1")) {
                txtDetalleraza.setText("Macho");
            } else {
                txtDetalleraza.setText("Hembra");
            }
            txtDetalleAnioNacimiento.setText(getIntent().getStringExtra("anioNacimiento"));
            txtDetalleEstado.setText(getIntent().getStringExtra("estado"));
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