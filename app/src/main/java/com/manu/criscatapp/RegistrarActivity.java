package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

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

        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(RegistrarActivity.this, "Suscrito a enviar a todos", Toast.LENGTH_SHORT).show();
            }
        });

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
            mAuth.createUserWithEmailAndPassword(correo,contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                        llamadaEspecifica(nombre,apellidos);
                    }else{
                        Toast.makeText(RegistrarActivity.this, "Usuario no registrado"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void llamadaEspecifica(String nombre, String apellido){
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();

        try {
            String token = "fCW2QxxvSYK5MTApT-0Voz:APA91bEREGm_Z10vT38CkqZYg14lYgHI48QDIGUumUlRFobjhV4oj_E02oJ3ejiO_nzGrjibASCze-2MaNinWr79j7e5N5dQeeLylg2P8URvB-KmH7GHrY9sGYLpBpeq91cp6KL7hvi-";

            json.put("to",token);
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo","BIENVENIDO");
            notificacion.put("detalle","Es momento de que inicies sesion "+nombre+" "+apellido);

            json.put("data",notificacion);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();

                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAff9hW18:APA91bG1vwHpPwDx2988V458z6qOVKiXRpprWUHSf1oCUy7O3_8PKCUJ7QpU72tdJChlfSxNqPkP8JqeUZHvZbGkChV3rjI_gUptqXJ54u5DIEfyC4Wzgz55yfvLkt9mJM_83tAM_r4R");
                    return header;
                }
            };

            myrequest.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void llamadaProgramada(){
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();

        try {
            //String token = "fCW2QxxvSYK5MTApT-0Voz:APA91bEREGm_Z10vT38CkqZYg14lYgHI48QDIGUumUlRFobjhV4oj_E02oJ3ejiO_nzGrjibASCze-2MaNinWr79j7e5N5dQeeLylg2P8URvB-KmH7GHrY9sGYLpBpeq91cp6KL7hvi-";

            json.put("to","/topics/"+"enviaratodos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo","soy un titulo :)");
            notificacion.put("detalle","soy un detalle");

            json.put("data",notificacion);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();

                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAff9hW18:APA91bG1vwHpPwDx2988V458z6qOVKiXRpprWUHSf1oCUy7O3_8PKCUJ7QpU72tdJChlfSxNqPkP8JqeUZHvZbGkChV3rjI_gUptqXJ54u5DIEfyC4Wzgz55yfvLkt9mJM_83tAM_r4R");
                    return header;
                }
            };

            myrequest.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}