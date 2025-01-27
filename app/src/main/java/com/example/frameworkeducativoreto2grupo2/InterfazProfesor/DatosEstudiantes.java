package com.example.frameworkeducativoreto2grupo2.InterfazProfesor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Modelo.Users;

import com.bumptech.glide.Glide;
import com.example.frameworkeducativoreto2grupo2.Clases.UserAdapter;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatosEstudiantes extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private List<Users> listaUsers = new ArrayList<>();

    int IDUserLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_datos_estudiantes);

        //recoger el inten que ha comenzado este activity
        Intent intent = getIntent();
        //recoger los datos mandados con el intent
        IDUserLog = intent.getIntExtra("IDUserLog", -1); //-1 --> valor por defecto si no encuentra el getExtra


        try {
            oos = Cliente.getInstance().getObjectOutputStream();
            ois = Cliente.getInstance().getObjectInputStream();
            dos = Cliente.getInstance().getDataOutputStream();
            dis = Cliente.getInstance().getDataInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtrasMP2);
        Spinner spinnerGrupo = findViewById(R.id.spinnerGrupo);
        Spinner spinnerCiclo = findViewById(R.id.spinnerCiclo);
        Spinner spinnerCursoAcademico = findViewById(R.id.spinnerCursoAcademico);
        RecyclerView recyclerViewEstudiantes = findViewById(R.id.recyclerViewEstudiantes);

        recyclerViewEstudiantes.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            try {
                //Opcion seleccionada 10 recoger alumnos
                dos.writeInt(10);
                dos.flush();

                //leer el List
                listaUsers = (List<Users>) ois.readObject();

                //actualizamos la lista visual despues de obtener los datos
                runOnUiThread(() -> {
                    UserAdapter adapter = new UserAdapter(DatosEstudiantes.this, listaUsers);
                    recyclerViewEstudiantes.setAdapter(adapter);
                });

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();


        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuProfesor = new Intent(DatosEstudiantes.this, MenuProfesor.class);
            menuProfesor.putExtra("IDUserLog", IDUserLog);
            startActivity(menuProfesor);
        });
    }
}