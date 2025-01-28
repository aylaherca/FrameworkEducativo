package com.example.frameworkeducativoreto2grupo2.InterfazEstudiante;

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

import com.bumptech.glide.Glide;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.PerfilProfesor;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Modelo.Users;

public class PerfilEstudiante extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    Users user = new Users();
    int IDUserLog;
    String tipoUserLogeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_estudiante);
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

        //recoger el inten que ha comenzado este activity
        Intent intent = getIntent();
        //recoger los datos mandados con el intent
        IDUserLog = intent.getIntExtra("IDUserLog", -1); //-1 --> valor por defecto si no encuentra el getExtra
        tipoUserLogeado = intent.getStringExtra("tipoUser");


        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtras);
        Button btnCambiarFoto = findViewById(R.id.btnCambiarFoto);

        TextView txtNombre = findViewById(R.id.txtNombrePerfil);
        TextView txtApellidos = findViewById(R.id.txtApellidosPerfil);
        TextView txtUsuario = findViewById(R.id.txtUsuarioPerfil);
        TextView txtEmail = findViewById(R.id.txtEmailPerfil);
        TextView txtDNI = findViewById(R.id.txtDNIPerfil);

        TextView txtCicloCursado = findViewById(R.id.txtCicloCursadoPerfil);
        TextView txtNumCurso = findViewById(R.id.txtNumCursoPerfil);
        TextView txtModalidadDUAL = findViewById(R.id.txtModalidadDUALPerfil);

        ImageView imageViewFotoPerfil = findViewById(R.id.imageViewFotoPerfil);

        if (IDUserLog != -1) {
            new Thread(() -> {
                try {
                    //Opcion seleccionada 9 recoger datos
                    dos.writeInt(9);
                    dos.flush();

                    //mandamos el id del user
                    dos.writeInt(IDUserLog);
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
                        Glide.with(PerfilEstudiante.this)
                                .load(user.getArgazkia())
                                .into(imageViewFotoPerfil);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else {
            //toast algo salio mal
            runOnUiThread(() -> Toast.makeText(this, "Algo salió mal.", Toast.LENGTH_SHORT).show());
        }

        //listener boton cambiar foto ------------------------------------------------------------------------------- BOTON CAMBIAR FOTO
        btnCambiarFoto.setOnClickListener(view -> {
            //abrir camara + guardar foto nueva en bd *******
        });

        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuEstudiante = new Intent(PerfilEstudiante.this, MenuEstudiante.class);
            menuEstudiante.putExtra("IDUserLog", IDUserLog);
            menuEstudiante.putExtra("tipoUser", tipoUserLogeado);
            startActivity(menuEstudiante);
        });
    }

    //Metodo para comprobar los datos
    //comprueba si el dato recogido es null o esta vacio y si es asi le setea un string por defecto (-)
    private String comprobarDatos(String dato) {
        if (dato == null || dato.isEmpty()) {
            // Return "Dato no encontrado" if the data is null or empty
            return getString(R.string.datoNoEncontrado);
        } else {
            // Return the original data if it's not null/empty
            return dato;
        }
    }

}