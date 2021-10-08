package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaMascotaActivity extends AppCompatActivity {

    FloatingActionButton btnNuevo;
    RecyclerView recyclerMascota;

    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    List<Mascota> listaMascota = new ArrayList<>();
    List<Usuario> listaUsuario = new ArrayList<>();
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

    private void listaUsuario(){

        Query q = dbReference.child("Usuario").equalTo(userID);
        //q.addListenerForSingleValueEvent();
        Log.d("TAG","pRUEBAS: "+   q);

        dbReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaUsuario.clear();
                for (DataSnapshot item:snapshot.getChildren()){
                    Usuario u = item.getValue(Usuario.class);
                    listaUsuario.add(u);

                    //Query q1 = u.equals(userID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listarDatos(){
        /*Query correo = dbReference.child("Usuario").child("correo").equalTo(userID).get();
        Log.d("TAG","El correo es: "+correo);*/
        //final String correo="";
        /*dbReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot snapshot1 : snapshot.getChildren()){
                    dbReference.child("Usuario").child(snapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Usuario user = snapshot.getValue(Usuario.class);
                            String correo = user.getCorreo();

                            Log.d("TAG","el correo es: "+correo);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/



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
                filtrar(userID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void filtrar(String id){
        ArrayList<Mascota> milista = new ArrayList<>();
        for (Mascota obj: listaMascota){
            if (obj.getPropietario().toLowerCase().contains(id.toLowerCase())){
                milista.add(obj);
            }
        }
        AdaptadorPersonalizado adapter = new AdaptadorPersonalizado(ListaMascotaActivity.this,milista);
        recyclerMascota.setAdapter(adapter);
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