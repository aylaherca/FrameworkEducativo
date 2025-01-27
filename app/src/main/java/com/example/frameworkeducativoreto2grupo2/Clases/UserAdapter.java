package com.example.frameworkeducativoreto2grupo2.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import Modelo.Users;
import com.example.frameworkeducativoreto2grupo2.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    //CLASE PARA PODER INSERTAR LOS DATOS EN EL RECYCLERVIEW
    private final List<Users> listaUSer;
    private final Context context;
    private final OnUserClickListener onUserClickListener;

    public interface OnUserClickListener {
        void onUserClick(Users user);
    }

    //constructor con listener para DatosProfesores
    public UserAdapter(Context context, List<Users> listaUSer, OnUserClickListener onUserClickListener) {
        this.context = context;
        this.listaUSer = listaUSer;
        this.onUserClickListener = onUserClickListener;
    }

    //constructor sin listener para DatosEstudiantes
    public UserAdapter(Context context, List<Users> listaUSer) {
        this.context = context;
        this.listaUSer = listaUSer;
        this.onUserClickListener = null;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_item_personas, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = listaUSer.get(position);

        //info del usuario
        holder.nombrePersona.setText(user.getNombre());
        holder.apellidoPersona.setText(user.getApellidos());

        //cargar la imagen
        Glide.with(context)
                .load(user.getArgazkia()) //geteamos la imagen
                .placeholder(R.drawable.placeholder) //sino: imagen placeholder
                .error(R.drawable.error_image) //y si da error: imagen error
                .into(holder.fotoPersona);

        //onclick listener de cada elemento de la lista
        //solo hacer si el listener no es null
        if (onUserClickListener != null) {
            holder.itemView.setOnClickListener(v -> onUserClickListener.onUserClick(user));
        }
    }

    @Override
    public int getItemCount() {
        return listaUSer.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePersona, apellidoPersona;
        ImageView fotoPersona;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrePersona = itemView.findViewById(R.id.nombrePersona);
            apellidoPersona = itemView.findViewById(R.id.apellidoPersona);
            fotoPersona = itemView.findViewById(R.id.fotoPersona);
        }
    }



}
