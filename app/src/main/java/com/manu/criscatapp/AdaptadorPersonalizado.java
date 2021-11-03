package com.manu.criscatapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manu.criscatapp.modelo.Mascota;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorPersonalizado extends RecyclerView.Adapter<AdaptadorPersonalizado.vistaHolder> {
    private Context context;
    private List<Mascota> listaMascota = new ArrayList<>();

    public  AdaptadorPersonalizado(Context c, List<Mascota> lista){
        this.context = c;
        this.listaMascota = lista;
    }

    @NonNull
    @Override
    public AdaptadorPersonalizado.vistaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.diseno_fila, parent, false);
        return new vistaHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPersonalizado.vistaHolder holder, final int position){
        holder.filaNombre.setText(String.valueOf(listaMascota.get(position).getNombre()));
        holder.filaAnioNacimiento.setText(String.valueOf(listaMascota.get(position).getAnioNacimiento()));
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MantenedorMascotaActivity.class);
                intent.putExtra("id", listaMascota.get(position).getId()+"");
                intent.putExtra("nombre",listaMascota.get(position).getNombre()+"");
                intent.putExtra("especie",listaMascota.get(position).getEspecie()+"");
                intent.putExtra("raza",listaMascota.get(position).getRaza()+"");
                intent.putExtra("sexo",listaMascota.get(position).getSexo()+"");
                intent.putExtra("anioNacimiento",listaMascota.get(position).getAnioNacimiento()+"");
                intent.putExtra("propietario",listaMascota.get(position).getPropietario()+"");
                intent.putExtra("estado",listaMascota.get(position).getEstado()+"");
                context.startActivity(intent);
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ventana = new AlertDialog.Builder(context);
                ventana.setMessage("¿Desea eliminar a la mascota?");
                ventana.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase fbDatabase;
                        DatabaseReference dbReference;
                        FirebaseApp.initializeApp(context);
                        fbDatabase = FirebaseDatabase.getInstance();
                        dbReference = fbDatabase.getReference();

                        dbReference.child("Mascota").child(listaMascota.get(position).getId()).removeValue();
                    }
                });
                ventana.setNegativeButton("NO",null);
                ventana.create().show();
            }
        });

        holder.btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetalleMascotaActivity.class);
                intent.putExtra("id", listaMascota.get(position).getId()+"");
                intent.putExtra("nombre",listaMascota.get(position).getNombre()+"");
                intent.putExtra("especie",listaMascota.get(position).getEspecie()+"");
                intent.putExtra("raza",listaMascota.get(position).getRaza()+"");
                intent.putExtra("sexo",listaMascota.get(position).getSexo()+"");
                intent.putExtra("anioNacimiento",listaMascota.get(position).getAnioNacimiento()+"");
                intent.putExtra("propietario",listaMascota.get(position).getPropietario()+"");
                intent.putExtra("estado",listaMascota.get(position).getEstado()+"");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return listaMascota.size();
    }

    public class vistaHolder extends RecyclerView.ViewHolder{
        TextView filaNombre, filaAnioNacimiento;
        ImageButton btnEditar, btnEliminar;
        CardView btnDetalle;
        public vistaHolder(@NonNull View itemView){
            super(itemView);
            filaNombre = itemView.findViewById(R.id.filaNombre);
            filaAnioNacimiento = itemView.findViewById(R.id.filaAnioNacimiento);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnDetalle = itemView.findViewById(R.id.btnDetalleMascota);
        }


    }

}
