package com.example.frameworkeducativoreto2grupo2.InterfazEstudiante;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Modelo.Users;
import com.example.frameworkeducativoreto2grupo2.Clases.UserAdapter;
import com.example.frameworkeducativoreto2grupo2.Cliente.Cliente;
import com.example.frameworkeducativoreto2grupo2.InterfazProfesor.DatosEstudiantes;
import com.example.frameworkeducativoreto2grupo2.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatosProfesores extends AppCompatActivity {
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private List<Users> listaUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_datos_profesores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtrasME2);
        Spinner spinnerNombre = findViewById(R.id.spinnerNombre);
        Spinner spinnerApeliido = findViewById(R.id.spinnerApellido);
        RecyclerView recyclerViewEstudiantes = findViewById(R.id.recyclerViewEstudiantes);

        recyclerViewEstudiantes.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            try {
                Cliente cliente = Cliente.getInstance();
                oos = cliente.getObjectOutputStream();
                ois = cliente.getObjectInputStream();
                dos = cliente.getDataOutputStream();
                dis = cliente.getDataInputStream();

                //opcion seleccionada 11 obtener profesores
                dos.writeInt(11);
                dos.flush();

                //leemos la lista de profesores
                listaUsers = (List<Users>) ois.readObject();

                //actualizamos el recycler
                runOnUiThread(() -> {
                    UserAdapter adapter = new UserAdapter(DatosProfesores.this, listaUsers, user -> {
                        Intent intentDatosProfesorHorario = new Intent(DatosProfesores.this, DatosProfesorHorario.class);
                        intentDatosProfesorHorario.putExtra("IDProfesorSelec", user.getId());
                        Toast.makeText(this, "Profesor seleccionado: " + user.getId(), Toast.LENGTH_SHORT).show();
                        startActivity(intentDatosProfesorHorario);
                    });
                    recyclerViewEstudiantes.setAdapter(adapter);
                });

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();


        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuEstudiante = new Intent(DatosProfesores.this, MenuEstudiante.class);
            startActivity(menuEstudiante);
        });
    }
}