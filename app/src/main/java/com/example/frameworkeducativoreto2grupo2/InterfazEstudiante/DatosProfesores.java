package com.example.frameworkeducativoreto2grupo2.InterfazEstudiante;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    int IDUserLog;
    String tipoUserLogeado;

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

        //recoger el inten que ha comenzado este activity
        Intent intent = getIntent();
        //recoger los datos mandados con el intent
        IDUserLog = intent.getIntExtra("IDUserLog", -1); //-1 --> valor por defecto si no encuentra el getIntExtra
        tipoUserLogeado = intent.getStringExtra("tipoUser");

        try {
            oos = Cliente.getInstance().getObjectOutputStream();
            ois = Cliente.getInstance().getObjectInputStream();
            dos = Cliente.getInstance().getDataOutputStream();
            dis = Cliente.getInstance().getDataInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //variables
        ImageButton btnAtras = findViewById(R.id.imageButtonAtrasME2);
        Spinner spinnerNombre = findViewById(R.id.spinnerNombre);
        Spinner spinnerApeliido = findViewById(R.id.spinnerApellido);
        RecyclerView recyclerViewEstudiantes = findViewById(R.id.recyclerViewEstudiantes);

        recyclerViewEstudiantes.setLayoutManager(new LinearLayoutManager(this));


        new Thread(() -> {
            try {
                //Opcion seleccionada 11 recoger profesores
                dos.writeInt(11);
                dos.flush();

                //leer el List recibido
                listaUsers = (List<Users>) ois.readObject();

                //actualizamos la lista visual despues de obtener los datos
                runOnUiThread(() -> {
                    UserAdapter adapter = new UserAdapter(DatosProfesores.this, listaUsers, user -> {
                        //Log.d("DatosProfesores", "Profesor clikado: " + user.getNombre());
                        Intent intentDatosProfesorHorario = new Intent(DatosProfesores.this, DatosProfesorHorario.class);
                        intentDatosProfesorHorario.putExtra("IDUserLog", IDUserLog);
                        intentDatosProfesorHorario.putExtra("tipoUser", tipoUserLogeado); // Corrected
                        intentDatosProfesorHorario.putExtra("IDProfesorSelec", user.getId());
                        startActivity(intentDatosProfesorHorario);
                    });
                    recyclerViewEstudiantes.setAdapter(adapter);
                });

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //setear el adapter y los clicks
        UserAdapter adapter = new UserAdapter(this, listaUsers, user -> {
            Intent intentDatosProfesorHorario = new Intent(DatosProfesores.this, DatosProfesorHorario.class);
            intentDatosProfesorHorario.putExtra("IDUserLog", IDUserLog);
            intentDatosProfesorHorario.putExtra("tipoUser", tipoUserLogeado); // Corrected
            intentDatosProfesorHorario.putExtra("IDProfesorSelec", user.getId());
            startActivity(intentDatosProfesorHorario);
        });
        runOnUiThread(() -> recyclerViewEstudiantes.setAdapter(adapter));



        //listener boton atras ------------------------------------------------------------------------------- BOTON ATRAS
        btnAtras.setOnClickListener(view -> {
            Intent menuEstudiante = new Intent(DatosProfesores.this, MenuEstudiante.class);
            menuEstudiante.putExtra("IDUserLog", IDUserLog);
            menuEstudiante.putExtra("tipoUser", tipoUserLogeado);
            startActivity(menuEstudiante);
        });
    }
}