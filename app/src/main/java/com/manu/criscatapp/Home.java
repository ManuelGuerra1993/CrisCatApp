package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manu.criscatapp.modelo.Mascota;
import com.manu.criscatapp.modelo.Usuario;

public class Home extends AppCompatActivity {

    Button btn_pet, btn_dates, btn_fares, btn_contact;

    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private String nombre;

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

        inicializarFirebase();
        listaUsuario();

        btn_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(Home.this,ListaMascotaActivity.class);
                startActivity(intent);
            }
        });

        btn_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(Home.this,CitasActivity.class);
                startActivity(intent);
            }
        });

        btn_fares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(Home.this,FareActivity.class);
                startActivity(intent);
            }
        });

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(Home.this,ContactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea cerrar sesión?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Home.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void listaUsuario(){
        Query q = dbReference.child("Usuario").child(userID);
        //q.addListenerForSingleValueEvent();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario u = snapshot.getValue(Usuario.class);
                nombre = u.getNombres().toString();
                Log.d("TAG","Usuario en Home: "+nombre);

                if (nombre.equals("manu99")){
                    btn_contact.setVisibility(View.VISIBLE);
                    btn_fares.setVisibility(View.VISIBLE);
                    btn_dates.setVisibility(View.VISIBLE);
                    btn_pet.setVisibility(View.VISIBLE);
                } else {
                    btn_contact.setVisibility(View.VISIBLE);
                    btn_fares.setVisibility(View.VISIBLE);
                    btn_dates.setVisibility(View.INVISIBLE);
                    btn_pet.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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