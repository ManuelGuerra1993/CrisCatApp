package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class CambiarContraActivity extends AppCompatActivity {

    private EditText txtCorreoR;
    private Button btnEnviar;
    private Button btnIniciarS;

    private String email = "";
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contra);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        txtCorreoR = findViewById(R.id.txtCorreoR);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnIniciarS= findViewById(R.id.btnIniciarS);

        btnIniciarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirSesion();
            }
        });
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = txtCorreoR.getText().toString();
                if (!email.isEmpty()){
                    mDialog.setMessage("Espere un momento...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetearPassword();
                }else {
                    Toast.makeText(CambiarContraActivity.this, "Ingrese su correo", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();

            }
        });
    }

    private void resetearPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CambiarContraActivity.this, "Se ha enviado correo",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(CambiarContraActivity.this, "No se envio correo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirSesion() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}