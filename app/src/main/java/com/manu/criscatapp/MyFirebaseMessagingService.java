package com.manu.criscatapp;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    FirebaseDatabase fbDataBase;
    DatabaseReference dbReference;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.e("token", "mi token es: "+s);
        guardarToken(s);

    }

    private void guardarToken(String s){
        //inicializarFirebase();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("token");
        ref.child("manuel").setValue(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();

        Log.e("TAG","Mensaje recibido de: "+from);

        /*if (remoteMessage.getNotification() != null){
            Log.e("TAG","El titulo es: "+remoteMessage.getNotification().getTitle());
            Log.e("TAG","El detalle es: "+remoteMessage.getNotification().getBody());
        }*/

        if (remoteMessage.getData().size() > 0){
            /*Log.e("TAG","El titulo es: "+remoteMessage.getData().get("titulo"));
            Log.e("TAG","El detalle es: "+remoteMessage.getData().get("detalle"));
            Log.e("TAG","el color es: "+remoteMessage.getData().get("color"));*/

            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("detalle");

            mayorapi4(titulo,detalle);

        }


    }

    private void mayorapi4(String titulo, String detalle){
        String id = "mensaje";

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(id,"nuevo",NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert  nm != null;
            nm.createNotificationChannel(nc);
        }

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detalle)
                .setContentIntent(clickNoti())
                .setContentInfo("nuevo");
        Random random = new Random();
        int idNotify = random.nextInt(8000);

        assert nm != null;
        nm.notify(idNotify, builder.build());


    }

    public PendingIntent clickNoti(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.putExtra("color","rojo");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,intent,0);
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        fbDataBase = FirebaseDatabase.getInstance();
        dbReference = fbDataBase.getReference();
    }
}
