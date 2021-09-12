package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtApellidos;
    private EditText txtCorreo;
    private EditText txtTelefono;
    private EditText txtContrasenia;

    private Button btnRegistrar;
    private Button btnIniciar;

    private String usuarioID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtContrasenia = findViewById(R.id.txtContraseña);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnIniciar = findViewById(R.id.btnIniciar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegistrar.setOnClickListener(view -> {
            createUsuario();
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirLoginActivity();

            }
        });
    }

    private void abrirLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }//finaliza abrirLogin Activity

    private void createUsuario() {
        String nombre = txtNombre.getText().toString();
        String apellidos = txtApellidos.getText().toString();
        String correo = txtCorreo.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String contrasenia = txtContrasenia.getText().toString();

        if (TextUtils.isEmpty(nombre)){
            txtNombre.setError("Ingrese nombre");
            txtNombre.requestFocus();
        }else if (TextUtils.isEmpty(apellidos)){
            txtApellidos.setError("Ingrese apellidos");
            txtApellidos.requestFocus();
        }else if (TextUtils.isEmpty(correo)){
            txtCorreo.setError("Ingrese correo");
            txtCorreo.requestFocus();
        }else if (TextUtils.isEmpty(telefono)){
            txtTelefono.setError("Ingrese teléfono");
            txtTelefono.requestFocus();
        }else if (TextUtils.isEmpty(contrasenia)){
            txtContrasenia.setError("Ingrese contraseña");
            txtContrasenia.requestFocus();
        }else {
            mAuth.signInWithEmailAndPassword(correo,contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        usuarioID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("usuario").document(usuarioID);

                        Map<String,Object> usuario = new HashMap<>();
                        usuario.put("Nombre",nombre);
                        usuario.put("Apellidos",apellidos);
                        usuario.put("correo",correo);
                        usuario.put("telefono",telefono);
                        usuario.put("contraseña",contrasenia);

                        documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG","OnSuccess: Datos Registrados"+usuarioID);
                            }
                        });
                        Toast.makeText(RegistrarActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegistrarActivity.this, "Usuario no registrado"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


}