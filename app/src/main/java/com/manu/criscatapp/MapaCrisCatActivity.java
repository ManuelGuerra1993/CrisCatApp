package com.manu.criscatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaCrisCatActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    float latitud, longitud;
    String titulo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cris_cat);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        recuperarDatos();

        LatLng marcador = new LatLng(latitud,longitud);
        mMap.addMarker(new MarkerOptions()
                .position(marcador)
                .title(titulo)
                .icon(cambiarIcono(MapaCrisCatActivity.this,R.drawable.icono))
        );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marcador,18));
    }

    private void recuperarDatos(){
        latitud = Float.parseFloat(getIntent().getStringExtra("latitud"));
        longitud = Float.parseFloat(getIntent().getStringExtra("longitud"));
        titulo = getIntent().getStringExtra("titulo");
    }

    private BitmapDescriptor cambiarIcono(Context context, int id){
        Drawable imagen = ContextCompat.getDrawable(context,id);
        imagen.setBounds(0,0,imagen.getIntrinsicWidth(),imagen.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(imagen.getIntrinsicWidth(), imagen.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        imagen.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}