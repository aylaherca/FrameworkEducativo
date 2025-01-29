package com.example.frameworkeducativoreto2grupo2.InterfazProfesor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Modelo.Users;

import com.bumptech.glide.Glide;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PerfilProfesor extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    Users user = new Users();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_profesor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            oos = Cliente.getInstance().getObjectOutputStream();
            ois = Cliente.getInstance().getObjectInputStream();
            dos = Cliente.getInstance().getDataOutputStream();
            dis = Cliente.getInstance().getDataInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtras);
        Button btnCambiarFoto = findViewById(R.id.btnCambiarFoto);

        TextView txtNombre = findViewById(R.id.txtNombrePerfil);
        TextView txtApellidos = findViewById(R.id.txtApellidosPerfil);
        TextView txtUsuario = findViewById(R.id.txtUsuarioPerfil);
        TextView txtEmail = findViewById(R.id.txtEmailPerfil);
        TextView txtDNI = findViewById(R.id.txtDNIPerfil);

        ImageView imageViewFotoPerfil = findViewById(R.id.imageViewFotoPerfil);

            new Thread(() -> {
                try {
                    //Opcion seleccionada 9 recoger datos
                    dos.writeInt(9);
                    dos.flush();

                    //leer el usuario
                    user = (Users) ois.readObject();

                    //comprobar si el dato esta vacio desde la bd y cambiar los campos
                    txtNombre.setText(comprobarDatos(user.getNombre()));
                    txtApellidos.setText(comprobarDatos(user.getApellidos()));
                    txtUsuario.setText(comprobarDatos(user.getUsername()));
                    txtEmail.setText(comprobarDatos(user.getEmail()));
                    txtDNI.setText(comprobarDatos(user.getDni()));

                    //foto placeholder si la imagen es null/no tiene
                    if (user.getArgazkia() == null || user.getArgazkia().isEmpty()) {
                        imageViewFotoPerfil.setImageResource(R.drawable.placeholder);
                    } else { //ponemos la foto de la db
                        Glide.with(PerfilProfesor.this)
                                .load(user.getArgazkia())
                                .into(imageViewFotoPerfil);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();

        //listener boton cambiar foto ------------------------------------------------------------------------------- BOTON CAMBIAR FOTO
        btnCambiarFoto.setOnClickListener(view -> {
            //abrir camara + guardar foto nueva en bd *******
        });

        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuProfesor = new Intent(PerfilProfesor.this, MenuProfesor.class);
            startActivity(menuProfesor);
        });
    }

    //Metodo para comprobar los datos
    //comprueba si el dato recogido es null o esta vacio y si es asi le setea un string por defecto (-)
    private String comprobarDatos(String dato) {
        if (dato == null || dato.isEmpty()) {
            //devuelve el string de dato no encontrado
            return getString(R.string.datoNoEncontrado);
        } else {
            //devuelve el dato de la bd
            return dato;
        }
    }

}