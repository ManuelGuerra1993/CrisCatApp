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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText txtMail;
    private EditText txtPasswaord;
    private Button btnIngresar;
    private Button btnRegistrarC;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtMail = findViewById(R.id.txtMail);
        txtPasswaord = findViewById(R.id.txtPassword);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnRegistrarC = findViewById(R.id.btnRegistrarC);

        mAuth = FirebaseAuth.getInstance();


        btnIngresar.setOnClickListener( view ->{
            usuarioLogin();
        } );
        btnRegistrarC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRegistrarActivity();
            }
        });
    }

    private void abrirRegistrarActivity() {
        Intent intent = new Intent(this, RegistrarActivity.class);
        startActivity(intent);
    }//FinalizaRegistrarActivity

    private void usuarioLogin() {
        String mail = txtMail.getText().toString();
        String passwaord = txtPasswaord.getText().toString();

        if (TextUtils.isEmpty(mail)){
            txtMail.setError("Ingrese un correo");
            txtMail.requestFocus();
        }else if (TextUtils.isEmpty(passwaord)){
            Toast.makeText(LoginActivity.this, "Ingrese una Contraseña", Toast.LENGTH_SHORT).show();
            txtPasswaord.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(mail,passwaord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,EmpezarActivity.class));
                    }else {
                        Log.w("TAG","Error:",task.getException());
                    }
                }
            });
        }
    }

}