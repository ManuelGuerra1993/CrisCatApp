package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.manu.criscatapp.modelo.Mascota;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class MantenedorMascotaActivity extends AppCompatActivity {

    FirebaseDatabase fbDataBase;
    DatabaseReference dbReference;
    StorageReference storageReference;
    private Uri imageUri;
    private Bitmap bitmap;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    EditText txtNombre, txtRaza, txtanioNacimiento, txtPropietario, txtEstado;
    RadioButton rbCanino, rbFelino, rbMacho, rbHembra;
    Button btnGuardar;
    TextView lblTitulo, lblNombreMascota;
    ImageView imgFoto;

    Bitmap photo;
    Uri downloadUri;
    String uri;
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
            String img = getIntent().getStringExtra("imageURL");
            //imgFoto.setImageURI(Uri.parse(img));
            Glide.with(this).load(img).into(imgFoto);
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
            //txtPropietario.setText(getIntent().getStringExtra("propietario"));
            txtEstado.setText(getIntent().getStringExtra("estado"));
            lblTitulo = findViewById(R.id.lblTitulo);
            lblTitulo.setText("ACTUALIZA LA INFORMACIÓN DE TU MASCOTA");
            btnGuardar.setText("ACTUALIZAR");
        }
    }

    private void asignarReferencias(){
        imgFoto = findViewById(R.id.btnFoto);
        txtNombre = findViewById(R.id.txtNombreMascota);
        txtRaza = findViewById(R.id.txtRaza);
        txtanioNacimiento = findViewById(R.id.txtanionacimiento);
        //txtPropietario = findViewById(R.id.txtPropietario);
        txtEstado = findViewById(R.id.txtEstado);
        rbCanino = findViewById(R.id.rbCanino);
        rbFelino = findViewById(R.id.rbFelino);
        rbMacho = findViewById(R.id.rbMacho);
        rbHembra = findViewById(R.id.rbHembra);
        btnGuardar = findViewById(R.id.btnRegistrar);
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });
    }

    private void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,1);
        }
    }


    /*private String firebaseUploadBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        StorageReference imageStorage = storageReference;
        StorageReference imageRef = imageStorage.child("mascotas/");

        Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            Log.d("TAG","Obtenemos url: "+imageRef.getDownloadUrl());
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                downloadUri = task.getResult();
                uri = downloadUri.toString();
                //sendMessageWithFile(uri);
            } else {
                // Handle failures
                // ...
            }
            //progressBar.setVisibility(View.GONE);
        });

        return String.valueOf(imageRef.getDownloadUrl());
        //return uri;
    }*/

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            //Bundle extras = data.getExtras();
            //bitmap = (Bitmap) extras.get("data");
            //imgFoto.setImageBitmap(bitmap);
            //
            if (requestCode == 1 && resultCode == RESULT_OK) {
                //      Bitmap imageBitmap = data.getData() ;
                photo = (Bitmap) data.getExtras().get("data");
                imgFoto.setImageBitmap(photo);
                /*if (photo != null)
                    firebaseUploadBitmap(photo);*/

            } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();
                if (uri != null) {
                    //firebaseUploadImage(uri);
                }
            }
       // }
    }

    private void registrar(){
        nombres = txtNombre.getText().toString();

        raza = txtRaza.getText().toString();
        //propietario = txtPropietario.getText().toString();
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

                //imgFoto.setImageBitmap(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] data = stream.toByteArray();
                StorageReference imageStorage = storageReference;
                StorageReference imageRef = imageStorage.child("mascotas/" + new Date().toString());

                Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    Log.d("TAG","Obtenemos url: "+imageRef.getDownloadUrl());
                    return imageRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        uri = downloadUri.toString();
                        Mascota mascota = new Mascota();
                        mascota.setImageURL(uri);
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
                        //sendMessageWithFile(uri);
                    } else {
                        // Handle failures
                        // ...
                    }
                    //progressBar.setVisibility(View.GONE);
                });



            }else{}
        } else {
            if (validarCampos()==true) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] data = stream.toByteArray();
                StorageReference imageStorage = storageReference;
                StorageReference imageRef = imageStorage.child("mascotas/" + new Date().toString());

                Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    Log.d("TAG","Obtenemos url: "+imageRef.getDownloadUrl());
                    return imageRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        uri = downloadUri.toString();
                        HashMap map = new HashMap();
                        map.put("imageURL",uri);
                        map.put("nombre", nombres);
                        map.put("especie", especie);
                        map.put("raza", raza);
                        map.put("sexo", sexo);
                        map.put("anioNacimiento", anionacimiento);
                        //map.put("propietario", propietario);
                        map.put("estado", estado);
                        dbReference.child("Mascota").child(id).updateChildren(map);
                        Toast.makeText(MantenedorMascotaActivity.this, "Mascota actualizada", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MantenedorMascotaActivity.this, ListaMascotaActivity.class);
                        startActivity(intent);
                        //sendMessageWithFile(uri);
                    } else {
                        // Handle failures
                        // ...
                    }
                    //progressBar.setVisibility(View.GONE);
                });


            } else {}
        }
    }

    /*private void UploadToFirebase(Bitmap bitmap){
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "."+getFileExtension(bitmap));

    }*/

    /*private String getFileExtension(Bitmap mbitmap){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(mbitmap));
    }*/

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
        storageReference = FirebaseStorage.getInstance().getReference();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MantenedorMascotaActivity.this,ListaMascotaActivity.class);
        startActivity(intent);
    }
}