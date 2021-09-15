package com.manu.criscatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        public vistaHolder(@NonNull View itemView){
            super(itemView);
            filaNombre = itemView.findViewById(R.id.filaNombre);
            filaAnioNacimiento = itemView.findViewById(R.id.filaAnioNacimiento);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

}
